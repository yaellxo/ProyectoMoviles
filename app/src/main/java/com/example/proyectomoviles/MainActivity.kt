package com.example.proyectomoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.proyectomoviles.client.LoginScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val storedAlias = sharedPreferences.getString("alias", null)

        if (storedAlias == null) {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.main_activity)

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                ?: throw IllegalStateException(":)")

            val navController = navHostFragment.navController

            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
            val bottomNavHelper = BottomNavHelper()
            bottomNavHelper.setupBottomNavigationView(bottomNavigationView, navController)

            bottomNavigationView.selectedItemId = R.id.menu_hot
        }
    }
}
