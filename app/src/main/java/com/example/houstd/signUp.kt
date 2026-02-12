package com.example.houstd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity

class signUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)  // ⚠️ غيّر الاسم لو ملف XML اسمه مختلف

        val userName = findViewById<TextInputEditText>(R.id.userName)
        val email = findViewById<TextInputEditText>(R.id.email)
        val password = findViewById<TextInputEditText>(R.id.password)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirmPassword)

        val confirmBtn = findViewById<Button>(R.id.createaccountbtn)

        confirmBtn.setOnClickListener {

            val nameTxt = userName.text.toString().trim()
            val emailTxt = email.text.toString().trim()
            val passTxt = password.text.toString().trim()
            val confirmTxt = confirmPassword.text.toString().trim()

            // Validation
            if (nameTxt.isEmpty()) {
                userName.error = "Enter your name"
                return@setOnClickListener
            }

            if (emailTxt.isEmpty()) {
                email.error = "Enter your email"
                return@setOnClickListener
            }

            if (passTxt.isEmpty()) {
                password.error = "Enter password"
                return@setOnClickListener
            }

            if (confirmTxt.isEmpty()) {
                confirmPassword.error = "Confirm password"
                return@setOnClickListener
            }

            if (passTxt.length < 6) {
                password.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (passTxt != confirmTxt) {
                confirmPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()

            // بعد التسجيل يرجع للـ Login
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}
