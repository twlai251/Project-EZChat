package com.example.ezchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_app_start.*

class appStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_start)

        Handler().postDelayed({
            val intent = Intent(this@appStartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)

    }
}