package com.example.ezchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener{
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()
            Log.d("Login", "Attempt login with email/password: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        }

        tv_register.setOnClickListener {
            finish()
        }

    }
}