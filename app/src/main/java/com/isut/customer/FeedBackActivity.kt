package com.isut.customer

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.isut.customer.apiclient.APIClient
import com.isut.customer.apiclient.APIInterface
import com.isut.customer.model.login.LoginModel
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedBackActivity : AppCompatActivity() {
    var rating :RatingBar? = null
    var etcode :EditText? = null
    var btnsave :AppCompatButton? = null
    var btncancel :AppCompatButton? = null
    var progress: ProgressDialog? = null
    var jsonObject = JSONObject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        rating = findViewById<RatingBar>(R.id.rating)
        btnsave = findViewById<AppCompatButton>(R.id.btnsave)
        btncancel = findViewById<AppCompatButton>(R.id.btncancel)
    etcode = findViewById<EditText>(R.id.etcode)
        btnsave?.setOnClickListener {
            val feedback = etcode!!.text.toString()
            if (feedback.isEmpty()) {
                Toast.makeText(this@FeedBackActivity, "Please enter feedback" , Toast.LENGTH_SHORT).show()

                etcode!!.requestFocus()
                return@setOnClickListener
            }
            progress = ProgressDialog(this)
            progress!!.setTitle("Loading")
            progress!!.setMessage("Wait while loading...")
            progress!!.setCancelable(false) // disable dismiss by tapping outside of the dialog
            progress!!.show()
            submit(feedback)
        }
        btncancel?.setOnClickListener {
            onBackPressed()
        }
    }
    fun submit(feedback: String) {
        val sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
       val  id = sharedpreferences!!.getInt(FixValue.User_ID, 0)

        try {
            jsonObject.put("feedback", feedback)
            jsonObject.put("userId", id)

            //  jsonObject.put("appId", token)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val bodyRequest =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        val  Header = "Bearer " + sharedpreferences.getString(FixValue.TOKEN, "")

        // SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //  final SharedPreferences.Editor editor = sharedPreferences.edit();
        val apiService = APIClient.client?.create(APIInterface::class.java)
        val call = apiService?.rateus(Header, bodyRequest)
        call?.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                if (response.body()!!.status.equals("ok", ignoreCase = true)) {
                    Toast.makeText(this@FeedBackActivity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progress!!.dismiss()
                    etcode!!.setText("")
                } else {
                    Toast.makeText(this@FeedBackActivity, "" + response.body()!!.message, Toast.LENGTH_SHORT).show()
                    progress!!.dismiss()
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {}
        })
    }

}