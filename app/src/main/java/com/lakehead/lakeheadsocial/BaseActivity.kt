package com.lakehead.lakeheadsocial

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

abstract class BaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        highlightCurrentPage(bottomNavigationView)

        // Start the notification service automatically
        startNotificationService()
    }

    private fun startNotificationService() {
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                if (this !is MainActivity) {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                return true
            }
            R.id.navigation_profile -> {
                if (this !is ProfileActivity) {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                return true
            }
            R.id.navigation_friends -> {
                if (this !is FriendsActivity) {
                    startActivity(Intent(this, FriendsActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                return true
            }
            R.id.navigation_following -> {
                if (this !is FollowingActivity) {
                    startActivity(Intent(this, FollowingActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                return true
            }
            R.id.navigation_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return false
    }

    private fun highlightCurrentPage(bottomNavigationView: BottomNavigationView) {
        val menu = bottomNavigationView.menu
        when (this) {
            is MainActivity -> menu.findItem(R.id.navigation_home).isChecked = true
            is ProfileActivity -> menu.findItem(R.id.navigation_profile).isChecked = true
            is FriendsActivity -> menu.findItem(R.id.navigation_friends).isChecked = true
            is FollowingActivity -> menu.findItem(R.id.navigation_following).isChecked = true
        }
    }
}
