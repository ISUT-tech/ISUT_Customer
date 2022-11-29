package com.isut.customer

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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

class ReferFriendActivity : AppCompatActivity() {
    var et_email: TextView? = null
    var btnlogin: AppCompatButton? = null
    var jsonObject = JSONObject()
    var progress : ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_friend)
        et_email = findViewById(R.id.etemamil)
        btnlogin = findViewById(R.id.btnlogin)

        btnlogin?.setOnClickListener {
            if(et_email?.text.toString().isEmpty())
            {
                Toast.makeText(this@ReferFriendActivity,"Please enter email address",Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }
            progress =  ProgressDialog(this);
            progress?.setTitle("Loading");
            progress?.setMessage("Wait while loading...");
            progress?.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress?.show();
            refer(et_email?.text.toString())
        }
    }



    fun refer(emailId: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences.edit()
        val  id =sharedpreferences.getInt(FixValue.User_ID, 0)

        try {
            jsonObject.put("email", emailId)


            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.referFriend(Header,emailId)

        call?.enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@ReferFriendActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    progress!!.dismiss()
                } else {
                    Toast.makeText(this@ReferFriendActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                progress!!.dismiss()


            }
        })
    }

}