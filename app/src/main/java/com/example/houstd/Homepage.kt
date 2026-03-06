package com.example.houstd

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.houstd.ui.Users.owner.OwnerApartmentDetails
import com.google.android.material.bottomnavigation.BottomNavigationView

class Homepage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)

        // Default fragment (Home)
        openFragment(OwnerApartmentDetails())

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    openFragment(OwnerApartmentDetails())
                    true
                }
                R.id.nav_requests -> {
//                    openFragment(RequestsFragment()) // create this fragment
                    true
                }
                R.id.nav_reviews -> {
//                    openFragment(ReviewsFragment()) // create this fragment
                    true
                }
                R.id.nav_more -> {
//                    openFragment(MoreFragment()) // create this fragment
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.ApartmentDetails, fragment)
            .commit()
    }
}