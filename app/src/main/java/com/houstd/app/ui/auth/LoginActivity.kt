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

class LoginActivity : AppCompatActivity() {

    private lateinit var repo: AppRepository
    private var selectedRole = Role.STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repo = AppRepository(this)
        // Auto-login if session exists
        repo.getCurrentUser()?.let { navigateToHome(it.role); return }

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvError = findViewById<TextView>(R.id.tvError)
        val tvRegister = findViewById<TextView>(R.id.tvGoToRegister)
        val btnStudent = findViewById<Button>(R.id.btnRoleStudent)
        val btnOwner = findViewById<Button>(R.id.btnRoleOwner)
        val btnAdmin = findViewById<Button>(R.id.btnRoleAdmin)

        fun updateRoleButtons() {
            listOf(btnStudent to Role.STUDENT, btnOwner to Role.OWNER, btnAdmin to Role.ADMIN).forEach { (btn, role) ->
                btn.isSelected = selectedRole == role
            }
        }
        updateRoleButtons()

        btnStudent.setOnClickListener { selectedRole = Role.STUDENT; updateRoleButtons() }
        btnOwner.setOnClickListener { selectedRole = Role.OWNER; updateRoleButtons() }
        btnAdmin.setOnClickListener { selectedRole = Role.ADMIN; updateRoleButtons() }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            tvError.visibility = View.GONE

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = "Please fill all fields"
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            btnLogin.text = "Signing in..."

            val result = repo.login(email, password, selectedRole)
            result.onSuccess { navigateToHome(it.role) }
            result.onFailure {
                tvError.text = it.message
                tvError.visibility = View.VISIBLE
                btnLogin.isEnabled = true
                btnLogin.text = "Sign In"
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
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
