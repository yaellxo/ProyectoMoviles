package com.example.proyectomoviles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.proyectomoviles.client.LoginScreen
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val storedAlias = sharedPreferences.getString("activeUserAlias", null)
        val userType = sharedPreferences.getString("userType", null)
        val nombre = sharedPreferences.getString("nombre", null)
        val correo = sharedPreferences.getString("correo", null)
        val edad = sharedPreferences.getString("edad", null)
        val userId = sharedPreferences.getString("userId", null)
        val userPhotoUri = sharedPreferences.getString("userPhotoUri", null)

        Log.d("MainActivity", "storedAlias: $storedAlias")
        Log.d("MainActivity", "userType: $userType")
        Log.d("MainActivity", "nombre: $nombre")
        Log.d("MainActivity", "correo: $correo")
        Log.d("MainActivity", "edad: $edad")
        Log.d("MainActivity", "userId: $userId")
        Log.d("MainActivity", "userPhotoUri: $userPhotoUri")

        if (storedAlias == null) {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.main_activity)

            Log.d("MainActivity", "User logged in, setting up the main activity layout")

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                    ?: throw IllegalStateException(":)")

            val navController = navHostFragment.navController

            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
            val bottomNavHelper = BottomNavHelper()
            bottomNavHelper.setupBottomNavigationView(bottomNavigationView, navController)

            bottomNavigationView.selectedItemId = R.id.menu_hot

            Log.d("MainActivity", "Selected bottom navigation item: menu_hot")
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(
            R.menu.bottom_nav_menu,
            menu
        )
        menu?.let {
            for (i in 0 until it.size()) {
                val item = it.getItem(i)
                item.actionView = layoutInflater.inflate(R.layout.menu_item_layout, null)
            }
        }

        Log.d("MainActivity", "Options menu created")
        return true
    }

}
