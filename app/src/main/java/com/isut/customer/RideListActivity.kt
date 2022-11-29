package com.isut.customer

import android.app.ProgressDialog
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.widget.ImageView
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.cabs.CabsModel
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.isut.customer.model.cabs.DataItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RideListActivity : AppCompatActivity() {
    var rec_list: RecyclerView? = null
    var imgsback: ImageView? = null
    var rideListAdapter: RideListAdapter? = null
    var Header = "Bearer"

    var progress: ProgressDialog? = null
    var cablist: List<DataItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_list)
        rec_list = findViewById(R.id.rec_list)
        imgsback = findViewById(R.id.imgsback)
        imgsback!!.setOnClickListener {
            onBackPressed()
        }
        progress = ProgressDialog(this)
        progress!!.setTitle("Loading")
        progress!!.setMessage("Wait while loading...")
        progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
        progress!!.show()
        submit()
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")
        val id = sharedpreferences.getInt(FixValue.User_ID, 0)

        // RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
         val url = FixValue.baseurl.plus("customer/$id/bookings")
        val call = apiService?.gercablist(Header, url)
        call?.enqueue(object : Callback<CabsModel> {
            override fun onResponse(call: Call<CabsModel>, response: Response<CabsModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    val myObj = response.body()
                    cablist = response.body()!!.data
                    rideListAdapter = RideListAdapter(this@RideListActivity, cablist)
                    rec_list!!.adapter = rideListAdapter
                    rec_list!!.setHasFixedSize(true)
                    val linearLayoutManager1 = LinearLayoutManager(
                        this@RideListActivity,
                        LinearLayoutManager.VERTICAL,
                        true
                    )
                    rec_list!!.layoutManager = linearLayoutManager1

                    /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                    //getotp();
                    //userdatwe(response.body().getUserId(),phone);
                    // int ids = response.body().get();
                    Toast.makeText(
                        this@RideListActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@RideListActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<CabsModel>, t: Throwable) {}
        })
    }
}