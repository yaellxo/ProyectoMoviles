package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.MainActivity
import com.example.proyectomoviles.R

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val etAliasLogin: EditText = findViewById(R.id.etAliasLogin)
        val etClaveLogin: EditText = findViewById(R.id.etClaveLogin)
        val btnInicioSesion: Button = findViewById(R.id.btnInicioSesion)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        btnInicioSesion.setOnClickListener {
            val aliasLogin = etAliasLogin.text.toString()
            val claveLogin = etClaveLogin.text.toString()

            if (aliasLogin.isEmpty() || claveLogin.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Manejar inicio de sesión del administrador
            if (aliasLogin == "admin" && claveLogin == "admin") {
                Toast.makeText(this, "Inicio de sesión como administrador exitoso", Toast.LENGTH_SHORT).show()

                // Redirigir a MainActivity como admin
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userType", "admin")
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            // Validar usuarios registrados
            val storedAliases = sharedPreferences.getStringSet("aliases", mutableSetOf())
            if (storedAliases?.contains(aliasLogin) == true) {
                val storedClave = sharedPreferences.getString("clave_$aliasLogin", null)
                if (storedClave == claveLogin) {
                    val userType = sharedPreferences.getString("userType_$aliasLogin", "user")

                    // Redirigir a MainActivity con el tipo de usuario
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userType", userType)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Clave incorrecta", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Alias no encontrado", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }
    }
}
