package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.MainActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.AdminConstants
import org.json.JSONArray

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        Log.d("LoginScreen", "onCreate - Bundle: $savedInstanceState")

        val etAliasLogin: EditText = findViewById(R.id.etAliasLogin)
        val etClaveLogin: EditText = findViewById(R.id.etClaveLogin)
        val btnInicioSesion: Button = findViewById(R.id.btnInicioSesion)
        val btnRegistro: Button = findViewById(R.id.btnRegistro)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        btnInicioSesion.setOnClickListener {
            val alias = etAliasLogin.text.toString()
            val claveLogin = etClaveLogin.text.toString()

            Log.d("LoginScreen", "Alias ingresado: $alias")
            Log.d("LoginScreen", "Clave ingresada: $claveLogin")

            if (alias.isEmpty() || claveLogin.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (alias == AdminConstants.ADMIN_ALIAS && claveLogin == AdminConstants.ADMIN_PASSWORD) {
                val editor = sharedPreferences.edit()
                editor.putString("userType", "admin")
                editor.putString("activeUserAlias", alias)
                editor.apply()

                Toast.makeText(this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val usersJson = sharedPreferences.getString("users", "[]")
                val users = JSONArray(usersJson)

                var userFound = false

                for (i in 0 until users.length()) {
                    val user = users.getJSONObject(i)
                    Log.d("LoginScreen", "Verificando usuario ${user.getString("alias")}")

                    if (user.getString("alias") == alias && user.getString("clave") == claveLogin) {
                        val editor = sharedPreferences.edit()
                        editor.putString("userType", "user")
                        editor.putString("activeUserAlias", alias)
                        editor.putString("nombre", user.getString("nombre"))
                        editor.putString("correo", user.getString("correo"))
                        editor.putString("edad", user.getString("edad"))
                        editor.putString("userId", user.optString("userId", null))
                        editor.apply()

                        // Obtener la URL de la foto de perfil
                        val photoUriString = user.optString("photoUri", "")
                        editor.putString("userPhotoUri", photoUriString)
                        editor.apply()

                        Log.d("LoginScreen", "URL de la imagen de perfil: $photoUriString")

                        // Preparar el mensaje de bienvenida
                        val mensaje = if (photoUriString.isNotEmpty()) {
                            "URL de imagen de perfil: $photoUriString\nInicio de sesión exitoso\n" +
                                    "Nombre: ${user.getString("nombre")}\n" +
                                    "Alias: ${user.getString("alias")}\n" +
                                    "Correo: ${user.getString("correo")}\n" +
                                    "Edad: ${user.getString("edad")}"
                        } else {
                            "Inicio de sesión exitoso\n" +
                                    "Nombre: ${user.getString("nombre")}\n" +
                                    "Alias: ${user.getString("alias")}\n" +
                                    "Correo: ${user.getString("correo")}\n" +
                                    "Edad: ${user.getString("edad")}"
                        }

                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

                        // Llamar a MainActivity con la información del usuario
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                        userFound = true
                        break
                    }
                }

                if (!userFound) {
                    Toast.makeText(this, "Alias o clave incorrectos", Toast.LENGTH_SHORT).show()
                    Log.d("LoginScreen", "Usuario no encontrado con alias: $alias")
                }
            }
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }
    }
}
