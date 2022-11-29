package com.isut.customer

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import com.hbb20.CountryCodePicker
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import org.json.JSONException
import okhttp3.RequestBody
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.login.LoginModel
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var btnloginButton: AppCompatButton? = null
    var etname: TextInputEditText? = null
    var etpass: TextInputEditText? = null
    var tv_singup: TextView? = null
    var tv_forgotpass: TextView? = null
    var progress: ProgressDialog? = null
    var token: String? = null
    var spinners: Spinner? = null

    var codeList: ArrayList<String>? = ArrayList()
    var codeList2: ArrayList<String>? = ArrayList()
    var selected_country_code: String? = null
    var jsonObject = JSONObject()
    var ccp: CountryCodePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        btnloginButton = findViewById(R.id.btnlogin)
        etname = findViewById(R.id.etname)
        spinners = findViewById(R.id.spinners)
        tv_singup = findViewById(R.id.tv_singup)
        etpass = findViewById(R.id.etpass)
        tv_forgotpass = findViewById(R.id.tv_forgotpass)
        ccp = findViewById(R.id.ccp)
        codeList!!.add("USA:+1")
        codeList!!.add("IND:+91")
        codeList2!!.add("+1")
        codeList2!!.add("+91")
        spinners?.setOnItemSelectedListener(this);
        val aa: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, codeList!!)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinners!!.setAdapter(aa)

        //  selected_country_code = ccp!!.getSelectedCountryCodeWithPlus()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
//To do//
                    return@OnCompleteListener
                }

// Get the Instance ID token//
                token = task.result.token
                // String msg = getString("Sty", token);
                Log.d("TAG", token!!)
            })

        tv_forgotpass?.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnloginButton!!.setOnClickListener(View.OnClickListener {
            val mobile = etname!!.getText().toString().trim { it <= ' ' }
            val password = etpass!!.getText().toString().trim { it <= ' ' }
            if (mobile.isEmpty() || mobile.length < 10) {
                etname!!.setError("Enter a valid mobile")
                etname!!.requestFocus()
                return@OnClickListener
            }
            if (mobile.isEmpty()) {
                etname!!.setError("Please enter Password")
                etname!!.requestFocus()
                return@OnClickListener
            }
            /*  progress = new ProgressDialog(LogInActivity.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();
                  submit();
    */


            //  progress.dismiss();
          //  selected_country_code = ccp!!.getSelectedCountryCodeWithPlus()

            val intent = Intent(this@LogInActivity, VerifivcationCodeActivity::class.java)
            intent.putExtra("mobile", mobile)
            intent.putExtra("password", password)
            intent.putExtra("code", +91)
            intent.putExtra("code2", selected_country_code)
            intent.putExtra("key", "fromlogin")
            startActivity(intent)
        })
        tv_singup!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LogInActivity, SignupActivity::class.java)
            startActivity(intent)
        })
    }

    fun onCountryPickerClick(view: View?) {
        ccp!!.setOnCountryChangeListener { //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
         //   selected_country_code = ccp!!.selectedCountryCodeWithPlus
            Log.d("TAG selected_country_code ", selected_country_code!!)

        }
    }

    override fun onStop() {
        super.onStop()
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        try {
            jsonObject.put("username", etname!!.text.toString().trim { it <= ' ' })
            jsonObject.put("password", etname!!.text.toString().trim { it <= ' ' })
            jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.login(bodyRequest)
        call?.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    val myObj = response.body()


                    /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                    //getotp();
                    //userdatwe(response.body().getUserId(),phone);
                    // int ids = response.body().get();
                    val i = Intent(this@LogInActivity, MainActivity::class.java)
                    edit.putInt(FixValue.User_ID, myObj!!.data.userDetails.id)
                    edit.putString(FixValue.MOBILE, myObj.data.userDetails.mobileNumber)
                    edit.putString(FixValue.TOKEN, response.body()!!.data.token)
                    edit.putString(FixValue.FIRSTNAME, response.body()!!.data.userDetails.fullName)
                    edit.putString(FixValue.EMAIL, response.body()!!.data.userDetails.email)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    edit.putString(FixValue.IN_lOGIN, "IN_lOGIN")
                    edit.apply()
                    startActivity(i)
                    finish()
                    Toast.makeText(
                        this@LogInActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@LogInActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {}
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selected_country_code = codeList2?.get(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}