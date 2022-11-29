package com.isut.customer

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import org.json.JSONObject
import com.hbb20.CountryCodePicker
import androidx.constraintlayout.widget.ConstraintLayout
import android.os.Bundle
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import org.json.JSONException
import okhttp3.RequestBody
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.register.UserModel
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener  {
    var etname: TextView? = null
    var spinners: Spinner? = null
    var tvsigns: TextView? = null
    var et_email: TextView? = null
    var etphone: TextView? = null
    var etdriver_password: TextView? = null
    var et_confirm_password: TextView? = null
    var btnlogin: AppCompatButton? = null
    var token: String? = null
    var selected_country_code: String? = null
    var progress: ProgressDialog? = null
    var jsonObject = JSONObject()
    var nameflag = false
    var codeList: ArrayList<String>? = ArrayList()
    var codeList2: ArrayList<String>? = ArrayList()
    var emailflag = false
    var passwordflag = false
    var cnfpasswordflag = false
    var matchpasswordflag = false
    var phoneflag = false
    var cabnoflag = false
    var cabmodelflag = false
    var licenselag = false
    var locationflag = false
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var ccp: CountryCodePicker? = null
    var mainout: ConstraintLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        etphone = findViewById(R.id.etphone)
        spinners = findViewById(R.id.spinners)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        etdriver_password = findViewById(R.id.etdriver_password)
        etphone = findViewById(R.id.etphone)
        et_email = findViewById(R.id.et_email)
        etname = findViewById(R.id.etname)
        tvsigns = findViewById(R.id.tvsigns)
        mainout = findViewById(R.id.mainout)
        btnlogin = findViewById(R.id.btnlogin)
        ccp = findViewById(R.id.ccp)
        tvsigns?.hint = "@ilstu.edu"
        codeList!!.add("USA:+1")
        codeList!!.add("IND:+91")
        codeList2!!.add("+1")
        codeList2!!.add("+91")
        spinners?.setOnItemSelectedListener(this);
        val aa: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, codeList!!)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinners!!.setAdapter(aa)

        btnlogin!!.setOnClickListener(this)
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
    }

    fun onCountryPickerClick(view: View?) {
        ccp!!.setOnCountryChangeListener { //Alert.showMessage(RegistrationActivity.this, ccp.getSelectedCountryCodeWithPlus());
          //  selected_country_code = ccp!!.selectedCountryCodeWithPlus

        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnlogin -> register()
        }
    }

    fun register() {
        val strname = etname!!.text.toString()
        val strphne = etphone!!.text.toString()
        val streml = et_email!!.text.toString()
        val strpass = etdriver_password!!.text.toString()
        val strcnfpass = et_confirm_password!!.text.toString()
        if (strname.isEmpty()) {
            // firstname.setError(getString(R.string.input_error_name));
            Snackbar.make(mainout!!, getString(R.string.enter_name), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            nameflag = false
            etname!!.requestFocus()
            return
        } else {
            nameflag = true
        }
        if (strphne.isEmpty()) {
            //editTextEmail.setError(getString(R.string.input_error_email));
            Snackbar.make(mainout!!, getString(R.string.enter_phone), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            phoneflag = false
            etphone!!.requestFocus()
            return
        } else {
            phoneflag = true
        }


        emailflag = if (streml.contains("@") && streml.isEmpty()  ) {
            Snackbar.make(mainout!!, getString(R.string.enter_mail), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            false
        } else {

            true
        }
        passwordflag = if (strpass.isEmpty()  ) {
            Snackbar.make(mainout!!, getString(R.string.enter_pass), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            false
        } else {

            true
        }
        cnfpasswordflag = if (strcnfpass.isEmpty()  ) {
            Snackbar.make(mainout!!, getString(R.string.enter_cnf), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            false
        } else {

            true
        }
        matchpasswordflag = if (!strcnfpass.equals(strcnfpass)  ) {
            Snackbar.make(mainout!!, getString(R.string.enter_cnf), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            false
        } else {

            true
        }



        if (nameflag && phoneflag && emailflag && passwordflag && cnfpasswordflag  && matchpasswordflag) {

            /*    progress = new ProgressDialog(this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();*/
            //submit();
        //    selected_country_code = ccp!!.selectedCountryCodeWithPlus
            val intent = Intent(this@SignupActivity, SignVerifivcationCodeActivity::class.java)
            intent.putExtra("mobile", strphne)
            intent.putExtra("name", strname)
            intent.putExtra("email", streml+tvsigns?.text.toString())
            intent.putExtra("code2", selected_country_code)
            intent.putExtra("key", "fromsignup")
            intent.putExtra("password", strpass)
            // progress.dismiss();
            startActivity(intent)
        }
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        try {
            jsonObject.put("fullName", etname!!.text.toString())
            jsonObject.put("mobileNumber", etphone!!.text.toString())
            jsonObject.put("role", 0)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.signup(bodyRequest)
        call?.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    val myObj = response.body()


                    /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                    //getotp();
                    //userdatwe(response.body().getUserId(),phone);
                    // int ids = response.body().get();
                    val i = Intent(this@SignupActivity, LogInActivity::class.java)
                    //edit.putInt(User_ID,myObj.getData().getId());
                    // edit.putString(IN_lOGIN,"IN_lOGIN");
                    // edit.apply();
                    startActivity(i)
                    finish()
                    Toast.makeText(
                        this@SignupActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {}
        })
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selected_country_code = codeList2?.get(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}