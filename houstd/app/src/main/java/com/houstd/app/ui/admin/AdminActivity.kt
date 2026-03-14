package com.houstd.app.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.houstd.app.R
import com.houstd.app.ui.shared.MoreFragment

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.inflateMenu(R.menu.menu_admin)
        loadFragment(AdminDashboardFragment())
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_dashboard -> AdminDashboardFragment()
                R.id.nav_listings -> AdminListingsFragment()
                R.id.nav_users -> AdminUsersFragment()
                R.id.nav_more -> MoreFragment()
                else -> return@setOnItemSelectedListener false
            }
            loadFragment(fragment)
            true
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
