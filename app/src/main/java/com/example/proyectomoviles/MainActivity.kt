package com.example.proyectomoviles

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Obtener el botón por su ID
        val btnRegistro: Button = findViewById(R.id.btnRegistro)

        // Establecer un listener para el botón
        btnRegistro.setOnClickListener {
            // Iniciar la actividad RegisterActivity
            val intent = Intent(this, com.example.proyectomoviles.client.RegisterScreen::class.java)
            startActivity(intent)
        }
    }
}
