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

        val sharedPreferences: SharedPreferences =
            getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
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
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val edad = edadString.toIntOrNull()
            if (edad == null || edad < 12 || edad > 80) {
                Toast.makeText(
                    this,
                    "Por favor, ingrese una edad válida entre 12 y 80 años",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (registeredEmails.contains(correo)) {
                Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val storedAlias = sharedPreferences.getString("alias", "")
            if (alias == storedAlias) {
                Toast.makeText(this, "Este alias ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = generateCustomUserId(alias)

            editor.putString("userId", userId)
            editor.putString("nombre", nombre)
            editor.putString("alias", alias)
            editor.putString("correo", correo)
            editor.putString("edad", edadString)
            editor.putString("clave", clave)
            registeredEmails.add(correo)
            editor.putStringSet("emails", registeredEmails)
            editor.apply()

            Toast.makeText(this, "Registro exitoso. ID asignado: $userId", Toast.LENGTH_SHORT)
                .show()

            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
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
