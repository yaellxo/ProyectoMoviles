package com.example.proyectomoviles.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.client.LoginScreen
//NO FUNCIONA TODAVIA
class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("AdminDashboard", "AdminDashboard creado")
        setContentView(R.layout.admin_activity)

        val btnLogoutAdmin: Button? = findViewById(R.id.btnLogoutAdmin)
        if (btnLogoutAdmin == null) {
            Log.e("AdminDashboard", "Botón btnLogoutAdmin no encontrado")
        } else {
            Log.d("AdminDashboard", "Botón btnLogoutAdmin encontrado")
        }
        if (btnLogoutAdmin != null) {
            btnLogoutAdmin.setOnClickListener {
                Log.d("AdminDashboard", "Botón de cerrar sesión presionado")
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
}
