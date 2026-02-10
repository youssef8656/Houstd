package com.example.houstd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val ownerRadio = findViewById<RadioButton>(R.id.ownerradio)
        val studentRadio = findViewById<RadioButton>(R.id.studentradio)

        val forgotPasswordBtn = findViewById<Button>(R.id.forgotpasswordbtn)
        val loginBtn = findViewById<Button>(R.id.loginbtn)
        val createAccountBtn = findViewById<Button>(R.id.createaccountbtn)

        // Forgot Password
        forgotPasswordBtn.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }

        // Create Account
        createAccountBtn.setOnClickListener {
            Toast.makeText(this, "Create Account clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, signUp::class.java)
            startActivity(intent)
        }

        // Login
        loginBtn.setOnClickListener {

            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isEmpty()) {
                username.error = "Enter username"
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                password.error = "Enter password"
                return@setOnClickListener
            }

            val role = when {
                ownerRadio.isChecked -> "Owner"
                studentRadio.isChecked -> "Student"
                else -> ""
            }

            if (role.isEmpty()) {
                Toast.makeText(this, "Please select Owner or Student", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Login Success as $role", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, Homepage::class.java)
            intent.putExtra("role", role)
            intent.putExtra("username", user)
            startActivity(intent)
        }
    }
}