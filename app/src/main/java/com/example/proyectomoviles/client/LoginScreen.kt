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
import com.example.proyectomoviles.models.AdminConstants

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

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (aliasLogin == AdminConstants.ADMIN_ALIAS && claveLogin == AdminConstants.ADMIN_PASSWORD) {
                editor.putString("userType", "admin")
                editor.apply()

                Toast.makeText(this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val storedAlias = sharedPreferences.getString("alias", null)
                val storedClave = sharedPreferences.getString("clave", null)

                if (storedAlias == aliasLogin && storedClave == claveLogin) {
                    editor.putString("userType", "user")
                    editor.apply()

                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Alias o clave incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }


        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserType(sharedPreferences: android.content.SharedPreferences, userType: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userType", userType)
        editor.apply()
    }
}
