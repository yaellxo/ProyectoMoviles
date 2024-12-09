package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val etNombreRegistro: EditText = findViewById(R.id.etNombreRegistro)
        val etAliasRegistro: EditText = findViewById(R.id.etAliasRegistro)
        val etCorreoRegistro: EditText = findViewById(R.id.etCorreoRegistro)
        val etEdadRegistro: EditText = findViewById(R.id.etEdadRegistro)
        val etClaveRegistro: EditText = findViewById(R.id.etClaveRegistro)
        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val btnRegresar: Button = findViewById(R.id.btnRegresar)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val registeredEmails = sharedPreferences.getStringSet("emails", mutableSetOf()) ?: mutableSetOf()
        Log.d("RegisterScreen", "Registered Emails: $registeredEmails")

        val usersJson = sharedPreferences.getString("users", "[]")
        Log.d("RegisterScreen", "Users JSON: $usersJson")

        val users = JSONArray(usersJson)

        btnCrearCuenta.setOnClickListener {
            val nombre = etNombreRegistro.text.toString().takeIf { it.isNotEmpty() } ?: ""
            val alias = etAliasRegistro.text.toString().takeIf { it.isNotEmpty() } ?: ""
            val correo = etCorreoRegistro.text.toString().takeIf { it.isNotEmpty() } ?: ""
            val edadString = etEdadRegistro.text.toString().takeIf { it.isNotEmpty() } ?: ""
            val clave = etClaveRegistro.text.toString().takeIf { it.isNotEmpty() } ?: ""

            if (nombre.isEmpty() || alias.isEmpty() || correo.isEmpty() || edadString.isEmpty() || clave.isEmpty()) {
                CustomToast.show(this, 600)
                return@setOnClickListener
            }

            if (edadString.toIntOrNull() == null || edadString.toInt() !in 1..150) {
                CustomToast.show(this, 100)
                return@setOnClickListener
            }

            // Verificar si el correo o alias ya están registrados
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("correo") == correo) {
                    CustomToast.show(this,300)
                    return@setOnClickListener
                }
                if (user.getString("alias") == alias) {
                    CustomToast.show(this,310)
                    return@setOnClickListener
                }
            }

            val userId = generateCustomUserId(alias)

            // Crear el nuevo usuario como JSONObject
            val newUser = JSONObject().apply {
                put("userId", userId) // El userId es seguro, ya que se genera de manera confiable
                put("nombre", nombre)
                put("alias", alias)
                put("correo", correo)
                put("edad", edadString)
                put("clave", clave)
                put("userPhotoUri", "") // Suponiendo que se mantendrá vacío
                put("carrito", JSONArray()) // Carrito vacío al registrarse
            }

            // Añadir el nuevo usuario a la lista de usuarios
            users.put(newUser)

            // Guardar los usuarios en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("users", users.toString())
            editor.apply()

            CustomToast.show(this, R.id.btnCrearCuenta)

            val intent = Intent(this, LoginScreen::class.java)
            intent.putExtra("usuario", newUser.toString()) // Enviar el usuario a la pantalla de login
            startActivity(intent)
            finish()
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateCustomUserId(alias: String): String {
        val currentDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return "USR-${alias.uppercase()}-$currentDate"
    }
}
