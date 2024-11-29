package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val etAliasLogin: EditText = findViewById(R.id.etAliasLogin)
        val etClaveLogin: EditText = findViewById(R.id.etClaveLogin)
        val btnInicioSesion: Button = findViewById(R.id.btnInicioSesion)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)

        // Inicializar SharedPreferences (Es el que maneja las contraseñas)
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        btnInicioSesion.setOnClickListener {
            val aliasLogin = etAliasLogin.text.toString()
            val claveLogin = etClaveLogin.text.toString()

            // Validaciones de campos vacíos
            if (aliasLogin.isEmpty() || claveLogin.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtienen los datos de sharedPreferences
            val storedAlias = sharedPreferences.getString("alias", null)
            val storedClave = sharedPreferences.getString("clave", null)

            // Validación de credenciales
            if (storedAlias == aliasLogin && storedClave == claveLogin) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                // Redirigir al menu
                val intent = Intent(this, CatalogScreen::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Alias o clave incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }
    }
}
