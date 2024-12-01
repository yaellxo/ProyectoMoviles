package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
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

        val registeredEmails =
            sharedPreferences.getStringSet("emails", mutableSetOf()) ?: mutableSetOf()

        btnCrearCuenta.setOnClickListener {
            val nombre = etNombreRegistro.text.toString()
            val alias = etAliasRegistro.text.toString()
            val correo = etCorreoRegistro.text.toString()
            val edadString = etEdadRegistro.text.toString()
            val clave = etClaveRegistro.text.toString()

            if (nombre.isEmpty() || alias.isEmpty() || correo.isEmpty() || edadString.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edadString.toIntOrNull() == null || edadString.toInt() !in 1..150) {
                Toast.makeText(this, "Por favor, ingrese una edad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usersJson = sharedPreferences.getString("users", "[]")
            val users = JSONArray(usersJson)

            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("correo") == correo) {
                    Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (user.getString("alias") == alias) {
                    Toast.makeText(this, "Este alias ya está registrado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val userId = generateCustomUserId(alias)

            val newUser = JSONObject().apply {
                put("userId", userId)
                put("nombre", nombre)
                put("alias", alias)
                put("correo", correo)
                put("edad", edadString)
                put("clave", clave)
                put("photoUri", "")
            }

            users.put(newUser)
            editor.putString("users", users.toString())
            editor.apply()

            val mensaje = "Usuario registrado exitosamente:\n" +
                    "Nombre: $nombre\n" +
                    "Alias: $alias\n" +
                    "Correo: $correo\n" +
                    "Edad: $edadString\n" +
                    "Clave: $clave"
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()

            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun generateCustomUserId(alias: String): String? {
        val currentDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return "USR-${alias.uppercase()}-$currentDate"
    }
}
