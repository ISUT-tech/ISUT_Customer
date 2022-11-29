package com.isut.customer

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.Booking.BookingModel
import com.isut.customer.model.promo.PromoResponse
import kotlinx.android.synthetic.main.dialogschedule_car.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class BookActivity : AppCompatActivity(), View.OnClickListener {
    var dateSelected = Calendar.getInstance()
    private var datePickerDialog: DatePickerDialog? = null
    var strtpoit: String? = null
    var endpoint: String? = null
    var drivername: String? = null
    var cabno: String? = null
    var lincenseno: String? = null
    var mobile: String? = null
    var carlinxe: String? = null
    var timeStutas: Boolean? = false
    var carmodel: String? = null
    var promotscode: String? = ""
    var tv_strting: TextView? = null
    var tvpromocode: TextView? = null
    var tv_destination: TextView? = null
    var tv_name1: TextView? = null
    var tv_cab1: TextView? = null
    var tv_license1: TextView? = null
    var tv_gtotal: TextView? = null
    var tv_fair: TextView? = null
    var tv_grandtotal: TextView? = null
    var tv_discount: TextView? = null
    var tv_discounts: TextView? = null
    var jsonObject = JSONObject()
    var sharedPreferences: SharedPreferences? = null
    var id = 0
    var driveriid = 0
    var tot = 0
    var btnbooking: AppCompatButton? = null
    var btnscheduleCar: AppCompatButton? = null
    var progress: ProgressDialog? = null
    var Header = "Bearer"
    var fair = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        tv_destination = findViewById(R.id.tv_destination)
        tv_strting = findViewById(R.id.tv_strting)
        tv_gtotal = findViewById(R.id.tv_gtotal)
        tv_discount = findViewById(R.id.tv_discount)
        tv_discounts = findViewById(R.id.tv_discounts)
        tv_grandtotal = findViewById(R.id.tv_grandtotal)
        btnbooking = findViewById(R.id.btnbooking)
        btnscheduleCar = findViewById(R.id.btnscheduleCar)
        tv_fair = findViewById(R.id.tv_fair)
        tv_name1 = findViewById(R.id.tv_name1)
        tvpromocode = findViewById(R.id.tvpromocode)
        tv_license1 = findViewById(R.id.tv_license1)
        tv_cab1 = findViewById(R.id.tv_cab1)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Booking"
        setSupportActionBar(toolbar)
        btnbooking!!.setOnClickListener(this)
        btnscheduleCar!!.setOnClickListener(this)
        tvpromocode!!.setOnClickListener(this)
        sharedPreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedPreferences!!.edit()
        id = sharedPreferences!!.getInt(FixValue.User_ID, 0)
        mobile = sharedPreferences!!.getString(FixValue.MOBILE, "")
        val intent = intent
        if (intent != null) {
            strtpoit = intent.getStringExtra("startpoint")
            endpoint = intent.getStringExtra("endpoint")
            drivername = intent.getStringExtra("drivername")
            driveriid = intent.getIntExtra("dids", 0)
            carmodel = intent.getStringExtra("cabnumum")
            carlinxe = intent.getStringExtra("linsece")
            fair = intent.getIntExtra("fair", 0)
            tot = intent.getIntExtra("fair", 0)
            // lincenseno = intent.getStringExtra("lincenseno");
            tv_strting!!.setText(strtpoit)
            tv_license1!!.setText(carlinxe)
            tv_name1!!.setText(drivername)
            tv_cab1!!.setText(carmodel)
            tv_destination!!.setText(endpoint)
            tv_fair!!.setText("$ $fair")
            btnbooking!!.setText("Book Ride $ $tot")
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnbooking -> {
                progress = ProgressDialog(this@BookActivity)
                progress!!.setTitle("Loading")
                progress!!.setMessage("Wait while loading...")
                progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
                progress!!.show()
                submit()
            }
            R.id.btnscheduleCar -> {
                scheduleCab()
            }
            R.id.tvpromocode -> {
                promoCodeDialog()
            }
        }
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        try {
            jsonObject.put("destinationLocation", endpoint)
            jsonObject.put("driverId", driveriid)
            jsonObject.put("fair", tot)
            jsonObject.put("promoCode", promotscode)
            jsonObject.put("sourceLocation", strtpoit)
            jsonObject.put("userId", id)
            jsonObject.put("userMobileNumber", mobile)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.booking(Header, bodyRequest)
        call?.enqueue(object : Callback<BookingModel> {
            override fun onResponse(call: Call<BookingModel>, response: Response<BookingModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    val myObj = response.body()



                    Toast.makeText(
                        this@BookActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    val returnIntent = Intent()
                    returnIntent.putExtra("bookingId",response.body()?.data?.id.toString())
                    setResult(RESULT_OK, returnIntent)
                    FixValue.CheckBooking = "1"
                    finish()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@BookActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<BookingModel>, t: Throwable) {}
        })
    }

    fun scheduleCab() {
        val view: View = layoutInflater.inflate(R.layout.dialogschedule_car, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Update Pofile")
        alertDialog.setCancelable(false)
        val canclenderrview = view.findViewById(R.id.canclerview) as CalendarView
        val btnsave = view.findViewById(R.id.btnsave) as AppCompatButton
        val btncancel = view.findViewById(R.id.btncancel) as AppCompatButton
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat("HH:mm a")
// you can get seconds by adding  "...:ss" to it
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"))

        val localTime: String = date.format(currentLocalTime)
        var dates = cal.time.time.toString()
        val timePicker = view.findViewById(R.id.timePicker ) as TimePicker
        timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour

            var am_pm = ""
            // AM_PM decider logic
            when {hour == 0 -> { hour += 12
                am_pm = "AM"
            }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> { hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
                val hours = if (hour < 10) "0" + hour else hour
                val mins = if (minute < 10) "0" + minute else minute
                // display format of time
            val   checkOldDate = Calendar.getInstance();

            if (hour >= checkOldDate.get(Calendar.HOUR)) {
                if (hour == checkOldDate.get(Calendar.HOUR) && minute >= checkOldDate.get(Calendar.MINUTE)) {
                    timeStutas  = true
                    dates = checkOldDate.time.time.toString()
                    /*Toast.makeText(
                        this,
                        "hhhhhii",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    return@setOnTimeChangedListener;
                }
                else{
                    timeStutas  = true
                    dates = checkOldDate.time.time.toString()

                    /* Toast.makeText(
                         this,
                         "hello",
                         Toast.LENGTH_SHORT
                     ).show()*/
                }
                //select current after
            } else {
                timeStutas  = false

                /* Toast.makeText(
                     this,
                     "hii",
                     Toast.LENGTH_SHORT
                 ).show()*/
            }

        }






        btnsave.setOnClickListener {
           if (timeStutas!!) {
               progress = ProgressDialog(this)
               progress!!.setTitle("Loading")
               progress!!.setMessage("Wait while loading...")
               progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
               progress!!.show()
               Schedulesubmit(dates)
               alertDialog.cancel();
               //submit(tvname.text.toString(),etphone.text.toString(), tvemail.text.toString())
           }
            else{
               Toast.makeText(
                   this,
                   "Please select valid time.",
                   Toast.LENGTH_SHORT
               ).show()
           }
           }
        btncancel.setOnClickListener {
            alertDialog.cancel();
        }



        alertDialog.setView(view);
        alertDialog.show();
    }

    fun Schedulesubmit(date: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        try {
            jsonObject.put("destinationLocation", endpoint)
            jsonObject.put("driverId", driveriid)
            jsonObject.put("fair", tot)
            jsonObject.put("promoCode", promotscode)
            jsonObject.put("sourceLocation", strtpoit)
            jsonObject.put("userId", id)
            jsonObject.put("scheduleDate", date)
            jsonObject.put("userMobileNumber", mobile)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.schedulebooking(Header, bodyRequest)
        call?.enqueue(object : Callback<BookingModel> {
            override fun onResponse(call: Call<BookingModel>, response: Response<BookingModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    val myObj = response.body()


                    /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                    //getotp();
                    //userdatwe(response.body().getUserId(),phone);
                    // int ids = response.body().get();
                    Toast.makeText(
                        this@BookActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    /*   val returnIntent = Intent()
                    returnIntent.putExtra("bookingId",response.body()?.data?.id.toString())
                    setResult(RESULT_OK, returnIntent)
                    FixValue.CheckBooking = "1"
                    finish()*/
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@BookActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<BookingModel>, t: Throwable) {}
        })
    }

    fun promoCodeDialog() {
        val view: View = layoutInflater.inflate(R.layout.dailog_apply_coupons, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Update Pofile")
        alertDialog.setCancelable(false)
        val tvname = view.findViewById(R.id.etcode) as EditText
        val btnsave = view.findViewById(R.id.btnsave) as AppCompatButton
        val btncancel = view.findViewById(R.id.btncancel) as AppCompatButton



        btnsave.setOnClickListener {
            if (tvname?.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter Promo Code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()
            alertDialog.dismiss();
            submitPromo(tvname.text.toString())

        }
        btncancel.setOnClickListener {
            alertDialog.cancel();
        }



        alertDialog.setView(view);
        alertDialog.show();
    }

    fun submitPromo(promoCode: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        try {
            jsonObject.put("code", promoCode)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.applyCouponCode(Header, promoCode)
        call?.enqueue(object : Callback<PromoResponse> {
            override fun onResponse(call: Call<PromoResponse>, response: Response<PromoResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    if(tot >=response.body()?.data?.discount!! ) {
                        tv_grandtotal?.visibility = View.VISIBLE
                        tv_gtotal?.visibility = View.VISIBLE
                        tv_discount?.visibility = View.VISIBLE
                        tv_discounts?.visibility = View.VISIBLE
                        tv_discounts?.text = "$" + response.body()?.data?.discount.toString()
                        tot = fair - response.body()?.data?.discount!!
                        tv_grandtotal?.text = "$" + tot.toString()
                        btnbooking!!.setText("Book Ride $ $tot")
                        promotscode = promoCode
                        progress?.dismiss()
                        successDialog()
                    }
                    else{
                        Toast.makeText(this@BookActivity, "Coupon Not Applicable", Toast.LENGTH_SHORT).show()
                        progress?.dismiss()
                    }
                }
                else{
                    Toast.makeText(this@BookActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progress?.dismiss()
                }
            }

            override fun onFailure(call: Call<PromoResponse>, t: Throwable) {

                progress?.dismiss()

            }
        })
    }

    fun successDialog() {
        val view: View = layoutInflater.inflate(R.layout.dailog_success_coupons, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setCancelable(false)

        val btnsave = view.findViewById(R.id.btnsave) as AppCompatButton



        btnsave.setOnClickListener {

            alertDialog.dismiss();
            //submit(tvname.text.toString(),etphone.text.toString(), tvemail.text.toString())
        }



        alertDialog.setView(view);
        alertDialog.show();
    }
    fun showCustomTimePicker() {
        val myCalender = Calendar.getInstance()
        val hour = myCalender[Calendar.HOUR_OF_DAY]
        val minute = myCalender[Calendar.MINUTE]
        val myTimeListener =
            OnTimeSetListener { view, hourOfDay, minute ->
                if (view.isShown) {
                    myCalender[Calendar.HOUR_OF_DAY] = hourOfDay
                    myCalender[Calendar.MINUTE] = minute
                }
            }
        val timePickerDialog = TimePickerDialog(
           this,
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myTimeListener,
            hour,
            minute,
            true
        )
        timePickerDialog.setTitle("Choose hour:")
        timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }
}