package com.isut.customer

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.UpdateResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    var jsonObject = JSONObject()

    var etemaail: TextInputEditText? = null
    var btnlogin: AppCompatButton? = null
    var emailflag: Boolean? = null
    var mainout: ConstraintLayout? = null
    var progress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etemaail = findViewById(R.id.etemaail);
        btnlogin = findViewById(R.id.btnlogin);
        mainout = findViewById(R.id.mainout);

        btnlogin?.setOnClickListener {
            forgotpassword()
        }
    }

    fun forgotpassword() {

        val streml = etemaail!!.text.toString()


        if ( Patterns.EMAIL_ADDRESS.matcher(streml).matches() ) {
            // firstname.setError(getString(R.string.input_error_name));
            emailflag = true


        } else {
            Snackbar.make(mainout!!, getString(R.string.enter_mail), Snackbar.LENGTH_SHORT)
                .setActionTextColor(
                    Color.WHITE
                ).show()
            emailflag = false
            etemaail!!.requestFocus()
            return
        }
        if ( emailflag == true) {

                progress =  ProgressDialog(this);
            progress!!.setTitle("Loading");
            progress!!.setMessage("Wait while loading...");
            progress!!.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress!!.show();
            submit(streml);


        }
    }
    fun submit( email: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {

            jsonObject.put("username", email)

            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.forgotpassword(Header,bodyRequest)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@ForgotPasswordActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                   onBackPressed()
                    progress!!.dismiss()

                } else {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                progress!!.dismiss()

            }
        })
    }


}