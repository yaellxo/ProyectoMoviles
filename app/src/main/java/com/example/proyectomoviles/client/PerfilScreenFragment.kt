package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.client.LoginScreen
//NO FUNCIONA TODAVIA
class PerfilScreenFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil_activity)

        val btnLogoutUser: Button = findViewById(R.id.btnLogoutUser)
        btnLogoutUser.setOnClickListener {
            // Limpiar SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // Redirigir al LoginScreen
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}
