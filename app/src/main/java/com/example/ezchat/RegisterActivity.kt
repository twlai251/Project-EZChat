package com.example.ezchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ezchat.call_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().uid ?: ""

    // PRIVATE FUNCTIONS
    private fun backToLogin(){
        tv_login_page.setOnClickListener {
            Log.d("MainActivity", "Login Activity")
            // Launch to LoginActivity
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun storingToFirebaseDatabase(uid: String, username: String, user_email: String, user_password: String, roll_num: String, student_id: String, department_number: String) {
        var roll_num_res = et_register_roll_num.text.toString().lowercase()

        roll_num_res = if (roll_num_res.contains("stud") || roll_num_res.contains("stu") || roll_num_res.startsWith("st")) {
            "Student"
        } else if (roll_num_res.contains("prof") || roll_num_res.contains("pro") || roll_num_res.startsWith("pr")) {
            "Professor"
        } else{
            "Other"
        }
        val uid = FirebaseAuth.getInstance().uid ?: ""

        val ref = FirebaseDatabase.getInstance().getReference("users/")
        // User class in User.kt
        val user = User(uid, username, user_email, user_password, roll_num_res, student_id, department_number)
        ref.push().setValue(user)

//      KEEP FOR FUTURE UPDATES!!!
//        when(roll_num_res){
//            "Student" -> {
//                val status_student = "Student"
//                val ref = FirebaseDatabase.getInstance().getReference("/Students/$username")
//                // User class in RegisterActivity
//                val user = User(username, user_email, user_password, status_student, student_id, department_number)
//                ref.push().setValue(user)
//            }
//            "Professor" -> {
//                val status_professor = "Professor"
//                val ref = FirebaseDatabase.getInstance().getReference("/Professors/$username")
//                val user = User(username, user_email, user_password, status_professor, student_id, department_number)
//                ref.push().setValue(user)
//            }
//            else ->{
//                val status_other = "Other"
//                val ref = FirebaseDatabase.getInstance().getReference("/Others/$username")
//                // User class in RegisterActivity
//                val user = User(username, user_email, user_password, status_other, student_id, department_number)
//                ref.push().setValue(user)
//            }
//        }

        //  DEBUG
        Log.d("RegisterActivity", " User saved on database.")

        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun performRegister() {

        btn_register.setOnClickListener {
            var username_res = et_register_username.text.toString()
            var email_res = et_register_email.text.toString()
            var password_res = et_register_password.text.toString()
            var roll_num_res = et_register_roll_num.text.toString()
            var student_id_res = et_register_student_id.text.toString()
            var department_res = et_register_department.text.toString()

            if(email_res.isEmpty() || password_res.isEmpty()){
                Toast.makeText(this, "Please enter in email/password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Debugging Log
            Log.d("MainActivity", "Username is $username_res")
            Log.d("MainActivity", "Roll Num is $roll_num_res")
            Log.d("MainActivity", "Student ID is $student_id_res")
            Log.d("MainActivity", "Department is $department_res")
            Log.d("MainActivity", "Email is $email_res")
            Log.d("MainActivity", "Password is $password_res")

            // Firebase Authentication for new users
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_res, password_res)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("NewUser", "Successfully created user with uid: ${it.result.user!!.uid}")
                    Toast.makeText(this, "Successfully registration!", Toast.LENGTH_LONG).show()
                    storingToFirebaseDatabase(uid, username_res, email_res, password_res, roll_num_res, student_id_res, department_res)
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

    }

    // ONCREATE FUN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // hide app bar
        supportActionBar?.hide()

        performRegister()
        backToLogin()

    }
    //END

}

