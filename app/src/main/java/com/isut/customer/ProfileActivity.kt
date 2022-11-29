package com.isut.customer

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
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


class ProfileActivity : AppCompatActivity() {
    var txtname: TextView? = null
    var txtemail: TextView? = null
    var tv_delete_account: TextView? = null
    var txtmobile: TextView? = null
    var tv_edit: TextView? = null
    var jsonObject = JSONObject()
    var progress: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        txtname = findViewById(R.id.textView58)
        txtmobile = findViewById(R.id.textView59)
        tv_delete_account = findViewById(R.id.tv_delete_account)
        tv_edit = findViewById(R.id.tv_edit)
        txtemail = findViewById(R.id.emailtxt);
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val id = sharedpreferences.getInt(FixValue.User_ID, 0)
        val name = sharedpreferences.getString(FixValue.FIRSTNAME, "")
        val mobile = sharedpreferences.getString(FixValue.MOBILE, "")
        val email = sharedpreferences.getString(FixValue.EMAIL, "")
        txtname!!.setText(name)
        txtmobile!!.setText(mobile)
        txtemail!!.setText(email)
        tv_edit?.setOnClickListener {
            EditDialog(name.toString(),email.toString(),mobile.toString())
        }
        tv_delete_account?.setOnClickListener {
            logdialog()
        }
    }

    fun  EditDialog(name: String, email: String, mobile: String)
    {
        val view: View = layoutInflater.inflate(R.layout.dailog_edit_profile, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Update Pofile")
        alertDialog.setCancelable(false)
        val tvname = view.findViewById(R.id.etname) as EditText
        val btnsave = view.findViewById(R.id.btnsave) as AppCompatButton
        val btncancel = view.findViewById(R.id.btncancel) as AppCompatButton
        val etphone = view.findViewById(R.id.etphone) as EditText
        val tvemail = view.findViewById(R.id.etemamil) as EditText
        tvname.setText(name)
        etphone.setText(mobile)
        tvemail.setText(email)

        btnsave.setOnClickListener {
            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()
            submit(tvname.text.toString(),etphone.text.toString(), tvemail.text.toString())
        }
  btncancel.setOnClickListener {
      alertDialog.cancel();
        }



        alertDialog.setView(view);
        alertDialog.show();
    }

    fun submit(name: String, phone: String, email: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {
            jsonObject.put("fullName", name)
            jsonObject.put("id", id.toString())
            jsonObject.put("email", email)
            jsonObject.put("mobileNumber", phone)
            jsonObject.put("role", "2")
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
        val call = apiService?.update(Header,bodyRequest)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@ProfileActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    edit.putInt(FixValue.User_ID, response.body()?.data!!.id)
                    edit.putString(FixValue.MOBILE, response.body()?.data!!.mobileNumber)
                    edit.putString(FixValue.FIRSTNAME, response.body()!!.data!!.fullName)
                    edit.putString(FixValue.EMAIL, response.body()!!.data!!.email)
                    txtname?.setText(response.body()!!.data!!.fullName)
                    txtemail?.setText(response.body()!!.data!!.email)
                    txtmobile?.setText(response.body()!!.data!!.mobileNumber)
                    edit.apply()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {

            }
        })
    }
    fun deleteAccount() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)
        val url = FixValue.baseurl.plus("customer/$id/delete")

        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.deleteAccount(Header,url)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@ProfileActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                    val preferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.clear()
                    editor.apply()
                    againSetData()

                    val intent = Intent(this@ProfileActivity, LogInActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                    progress!!.dismiss()
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "" + response.body()!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {

            }
        })
    }

    fun logdialog() {
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Are you sure you want to Delete Account?")
        alert.setPositiveButton("YES") { dialog, which ->
            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()
            deleteAccount()
        }
        alert.setNegativeButton("NO") { dialog, which -> // close dialog
            dialog.cancel()
        }
        alert.show()
    }

    private fun againSetData() {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        edit.putString(FixValue.IN_lOGIN, "")
        //edit.putString(IsAppOpenFirst,OUT_LOGIN);
        edit.commit()
    }

}