package com.isut.customer

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AboutActivity : AppCompatActivity() {
    var texts : TextView ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        texts = findViewById(R.id.texts);
        texts?.setMovementMethod(ScrollingMovementMethod())
        texts?.text = "Welcome to ISUT.\n" +
                "\n" +
                "Looking for commute to the college your at the right place."
    }
}