package com.example.ezchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        performRegister()
        backToLogin()
    }

    private fun performRegister() {

        btn_register.setOnClickListener {
            var username_res = et_register_username.text.toString()
            var email_res = et_register_email.text.toString()
            var password_res = et_register_password.text.toString()

            if(email_res.isEmpty() || password_res.isEmpty()){
                Toast.makeText(this, "Please enter in email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Debugging Log
            Log.d("MainActivity", "Username is $username_res")
            Log.d("MainActivity", "Email is $email_res")
            Log.d("MainActivity", "Password is $password_res")

            // Firebase Authentication for new users
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_res, password_res)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("NewUser", "Successfully created user with uid: ${it.result.user!!.uid}")
                    Toast.makeText(this, "Successfully registration!", Toast.LENGTH_LONG).show()
                    storingToFirebaseDatabase(username_res)
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }


        }

    }

    private fun backToLogin(){
        tv_login_page.setOnClickListener {
            Log.d("MainActivity", "Login Activity")
            // Launch to LoginActivity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun storingToFirebaseDatabase(username: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(username, uid)
        ref.setValue(user).addOnSuccessListener { Log.d("RegisterActivity", " User saved on database.") }
    }

    //END

}

class User(val username:String, val uid:String)