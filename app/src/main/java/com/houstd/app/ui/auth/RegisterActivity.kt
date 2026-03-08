package com.houstd.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.houstd.app.R
import com.houstd.app.data.model.Role
import com.houstd.app.data.repository.AppRepository
import com.houstd.app.ui.admin.AdminActivity
import com.houstd.app.ui.owner.OwnerActivity
import com.houstd.app.ui.student.StudentActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var repo: AppRepository
    private var selectedRole = Role.STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        repo = AppRepository(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvError = findViewById<TextView>(R.id.tvError)
        val tvLogin = findViewById<TextView>(R.id.tvGoToLogin)
        val btnStudent = findViewById<Button>(R.id.btnRoleStudent)
        val btnOwner = findViewById<Button>(R.id.btnRoleOwner)
//        val btnAdmin = findViewById<Button>(R.id.btnRoleAdmin)

        fun updateRoleButtons() {
            listOf(btnStudent to Role.STUDENT, btnOwner to Role.OWNER).forEach { (btn, role) ->
                btn.isSelected = selectedRole == role
            }
        }
        updateRoleButtons()

        btnStudent.setOnClickListener { selectedRole = Role.STUDENT; updateRoleButtons() }
        btnOwner.setOnClickListener { selectedRole = Role.OWNER; updateRoleButtons() }
//        btnAdmin.setOnClickListener { selectedRole = Role.ADMIN; updateRoleButtons() }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString()
            val confirm = etConfirm.text.toString()
            tvError.visibility = View.GONE

            when {
                name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty() ->
                    tvError.text = "Please fill all fields"
                pass.length < 6 -> tvError.text = "Password must be at least 6 characters"
                pass != confirm -> tvError.text = "Passwords do not match"
                else -> {
                    val result = repo.register(name, email, pass, selectedRole)
                    result.onSuccess { navigateToHome(it.role) }
                    result.onFailure { tvError.text = it.message }
                    if (result.isFailure) tvError.visibility = View.VISIBLE
                    return@setOnClickListener
                }
            }
            tvError.visibility = View.VISIBLE
        }

        tvLogin.setOnClickListener { finish() }
    }

    private fun navigateToHome(role: Role) {
        val intent = when (role) {
            Role.STUDENT -> Intent(this, StudentActivity::class.java)
            Role.OWNER -> Intent(this, OwnerActivity::class.java)
            Role.ADMIN -> Intent(this, AdminActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
