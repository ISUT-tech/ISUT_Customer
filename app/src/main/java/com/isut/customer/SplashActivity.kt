package com.isut.customer

import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private var sharedpreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedpreferences = getSharedPreferences(FixValue.MyPREFERENCES, MODE_PRIVATE)
        val edit = sharedpreferences!!.edit()
        handlerWork()
    }

    private fun handlerWork() {
        if (sharedpreferences != null) {

            //boolean openFirst=sharedpreferences.getBoolean(IsAppOpenFirst,true);

            //Log.d("openFirst","openFirst-=>"+openFirst);
            Handler().postDelayed({
                val b = sharedpreferences!!.getString(FixValue.IN_lOGIN, "")
                if (b.equals("", ignoreCase = true)) {
                    val intent1 = Intent(this@SplashActivity, LogInActivity::class.java)
                    startActivity(intent1)
                    finish()
                } else {
                    val intent2 = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent2)
                    finish()
                }
            }, 3000)
        }
    }
}