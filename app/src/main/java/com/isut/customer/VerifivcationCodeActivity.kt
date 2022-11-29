package com.isut.customer

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.widget.TextView
import org.json.JSONObject
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.FirebaseException
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.view.View
import org.json.JSONException
import okhttp3.RequestBody
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.login.LoginModel
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class VerifivcationCodeActivity : AppCompatActivity() {
    private var mVerificationId: String? = null
    private var password: String? = null
    var btnlogin: AppCompatButton? = null

    //The edittext to input the code
    private var editTextCode: TextInputEditText? = null

    //firebase auth object
    private var mAuth: FirebaseAuth? = null
    var mobile: String? = null
    var name: String? = null
    var email: String? = null
    var reference: DatabaseReference? = null
    var tv_rent: TextView? = null
    var token: String? = null
    var selected_country_code: String? = null
    var progress: ProgressDialog? = null
    var jsonObject = JSONObject()
    var selected_country_code2 = 0
    private var mResendToken: ForceResendingToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifivcation_code)
        mAuth = FirebaseAuth.getInstance()
        editTextCode = findViewById(R.id.etname)
        tv_rent = findViewById(R.id.tv_rent)
        btnlogin = findViewById(R.id.btnlogin)
      ///  reference = FirebaseDatabase.getInstance().getReference("USER")
        FirebaseApp.initializeApp(this)
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


        //getting mobile number from the previous activity
        //and sending the verification code to the number
        val intent = intent
        if (intent != null) {
            if (intent.getStringExtra("key").equals("fromsignup", ignoreCase = true)) {
                mobile = intent.getStringExtra("mobile")
                name = intent.getStringExtra("name")
                password = intent.getStringExtra("password")
                email = intent.getStringExtra("name")
                selected_country_code = intent.getStringExtra("code")
                Log.d("selected_country_code12", selected_country_code!!)
            }
            if (intent.getStringExtra("key").equals("fromlogin", ignoreCase = true)) {
                mobile = intent.getStringExtra("mobile")
                password = intent.getStringExtra("password")

                selected_country_code2 = intent.getIntExtra("code", 0)
                selected_country_code = intent.getStringExtra("code2")
                Log.d("selected_country_code12", selected_country_code2.toString())
                Log.d("selected_country_codessss", selected_country_code.toString())
            }
        }
        sendVerificationCode(mobile, selected_country_code)
        btnlogin!!.setOnClickListener(View.OnClickListener {
            val code = editTextCode!!.getText().toString().trim { it <= ' ' }
            if (code.isEmpty() || code.length < 6) {
                editTextCode!!.setError("Enter valid code")
                editTextCode!!.requestFocus()
                return@OnClickListener
            }

            //verifying the code entered manually
            verifyVerificationCode(code)
        })
        tv_rent!!.setOnClickListener(View.OnClickListener {
            resendVerificationCode(
                editTextCode!!.getText().toString(), mResendToken
            )
        })
    }

    private fun resendVerificationCode(phoneNumber: String, token: ForceResendingToken?) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .setForceResendingToken(token) // ForceResendingToken from callbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun sendVerificationCode(mobile: String?, code: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            code + mobile,  // Phone number to verify
            120,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            this,  // Activity (for callback binding)
            mCallbacks
        )
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    editTextCode!!.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifivcationCodeActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                mResendToken = forceResendingToken

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }

    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@VerifivcationCodeActivity) { task ->
                if (task.isSuccessful) {
                    //verification successful we will start the profile activity
                    progress = ProgressDialog(this@VerifivcationCodeActivity)
                    progress!!.setTitle("Loading")
                    progress!!.setMessage("Wait while loading...")
                    progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
                    progress!!.show()
                    submit()
                    /* SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES,  MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedpreferences.edit();
                                UserModel userModel = new UserModel();
                                userModel.setMobile(mobile);
                                reference.child(mobile).push().setValue(userModel);
                                edit.putString(IN_lOGIN, "IN_lOGIN");
                                edit.apply();*/
                    /*  Intent intent = new Intent(VerifivcationCodeActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    
                                startActivity(intent);*/
                } else {

                    //verification unsuccessful.. display an error message
                    var message = "Somthing is wrong, we will fix it soon..."
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered..."
                    }
                    val snackbar =
                        Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG)
                    snackbar.setAction("Dismiss") { }
                    snackbar.show()
                }
            }
    }

    fun submit() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        try {
            jsonObject.put("username", mobile)
            jsonObject.put("password", password)
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
               if(response.code() == 200) {
                   if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                       val myObj = response.body()


                       /* Intent i = new Intent(SignupActivity.this,HomeActivity.class);
                    startActivity(i);*/
                       //getotp();
                       //userdatwe(response.body().getUserId(),phone);
                       // int ids = response.body().get();
                       val i = Intent(this@VerifivcationCodeActivity, MainActivity::class.java)
                       edit.putInt(FixValue.User_ID, myObj!!.data.userDetails.id)
                       edit.putString(FixValue.MOBILE, myObj.data.userDetails.mobileNumber)
                       edit.putString(FixValue.TOKEN, response.body()!!.data.token)
                       edit.putString(
                           FixValue.FIRSTNAME,
                           response.body()!!.data.userDetails.fullName
                       )
                       edit.putString(FixValue.EMAIL, response.body()!!.data.userDetails.email)
                       i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                       edit.putString(FixValue.IN_lOGIN, "IN_lOGIN")
                       edit.apply()
                       startActivity(i)
                       finish()
                       Toast.makeText(
                           this@VerifivcationCodeActivity,
                           "" + response.body()!!.message,
                           Toast.LENGTH_SHORT
                       ).show()
                       progress!!.dismiss()
                   } else {
                       Toast.makeText(
                           this@VerifivcationCodeActivity,
                           "" + response.body()!!.message,
                           Toast.LENGTH_SHORT
                       ).show()
                       progress!!.dismiss()
                   }
               }else{
                   Toast.makeText(this@VerifivcationCodeActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                   progress!!.dismiss()
               }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {}
        })
    }
}