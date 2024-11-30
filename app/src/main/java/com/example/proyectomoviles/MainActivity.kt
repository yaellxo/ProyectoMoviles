package com.example.proyectomoviles

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si se recibió el tipo de usuario desde LoginScreen
        userType = intent.getStringExtra("userType") ?: ""

        if (userType.isEmpty()) {
            // Si no hay usuario, redirigir al LoginScreen
            val intent = Intent(this, com.example.proyectomoviles.client.LoginScreen::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Configurar la vista principal si el usuario inició sesión
        setContentView(R.layout.main_activity)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found!")

        val navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val bottomNavHelper = BottomNavHelper()
        bottomNavHelper.setupBottomNavigationView(bottomNavigationView, navController, userType)

        bottomNavigationView.selectedItemId = R.id.menu_hot
    }
}
