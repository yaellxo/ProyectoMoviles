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
import com.example.proyectomoviles.utils.CustomToast
import org.json.JSONArray

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
            val alias = etAliasLogin.text.toString()
            val claveLogin = etClaveLogin.text.toString()

            if (alias.isEmpty() || claveLogin.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (alias == AdminConstants.ADMIN_ALIAS && claveLogin == AdminConstants.ADMIN_PASSWORD) {
                val editor = sharedPreferences.edit()
                editor.putString("userType", "superAdmin")
                editor.putString("activeUserAlias", alias)
                editor.putString("nombre", AdminConstants.ADMIN_NOMBRE)
                editor.putString("correo", AdminConstants.ADMIN_EMAIL)
                editor.putString("edad", AdminConstants.ADMIN_AREA)
                editor.putString("userId", AdminConstants.ADMIN_ID)
                editor.apply()

                CustomToast.show(this, R.id.btnInicioSesion)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            try {
                val adminsJson = sharedPreferences.getString("admins_data", "[]")
                val adminsArray = JSONArray(adminsJson)
                var adminFound = false

                for (i in 0 until adminsArray.length()) {
                    val admin = adminsArray.getJSONObject(i)
                    if (admin.getString("alias") == alias && admin.getString("clave") == claveLogin) {
                        adminFound = true

                        val editor = sharedPreferences.edit()
                        editor.putString("userType", "admin")
                        editor.putString("activeUserAlias", alias)
                        editor.putString("nombre", admin.getString("nombre"))
                        editor.putString("correo", admin.getString("correo"))
                        editor.putString("area", admin.getString("area"))
                        editor.putString("nivelAcceso", admin.getString("nivelAcceso"))
                        editor.putString("userId", admin.getString("adminId"))
                        editor.apply()

                        CustomToast.show(this, R.id.btnInicioSesion)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@setOnClickListener
                    }
                }

                if (!adminFound) {
                }
            } catch (e: Exception) {
                Log.e("LoginScreen", "Error al recuperar los administradores: ${e.message}")
                Toast.makeText(this, "Error al verificar administrador", Toast.LENGTH_SHORT).show()
            }

            try {
                val usersJson = sharedPreferences.getString("users", "[]")
                val usersArray = JSONArray(usersJson)
                var userFound = false

                for (i in 0 until usersArray.length()) {
                    val user = usersArray.getJSONObject(i)
                    if (user.getString("alias") == alias && user.getString("clave") == claveLogin) {
                        userFound = true

                        val editor = sharedPreferences.edit()
                        editor.putString("userType", "user")
                        editor.putString("activeUserAlias", alias)
                        editor.putString("nombre", user.getString("nombre"))
                        editor.putString("correo", user.getString("correo"))
                        editor.putString("edad", user.getString("edad"))
                        editor.putString("userId", user.getString("userId"))
                        editor.apply()

                        val photoUriString = user.optString("photoUri", "")
                        editor.putString("userPhotoUri", photoUriString)
                        editor.apply()

                        Log.d("LoginScreen", "URL de la imagen de perfil: $photoUriString")

                        CustomToast.show(this, R.id.btnInicioSesion)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@setOnClickListener
                    }
                }

                if (!userFound) {
                }
            } catch (e: Exception) {
                Log.e("LoginScreen", "Error al recuperar los usuarios: ${e.message}")
                Toast.makeText(this, "Error al verificar usuario", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistro.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
        }
    }
}
