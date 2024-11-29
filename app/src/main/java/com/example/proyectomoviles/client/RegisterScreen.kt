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

        //Se usa SharedPreferences para guardar los datos del usuario en el dispositivo
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        btnCrearCuenta.setOnClickListener {
            val nombre = etNombreRegistro.text.toString()
            val alias = etAliasRegistro.text.toString()
            val correo = etCorreoRegistro.text.toString()
            val edad = etEdadRegistro.text.toString()
            val clave = etClaveRegistro.text.toString()

            // Validaciones
            if (nombre.isEmpty() || alias.isEmpty() || correo.isEmpty() || edad.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificacion si el usuario ya existe
            val storedAlias = sharedPreferences.getString("alias", "")
            if (alias == storedAlias) {
                Toast.makeText(this, "Este alias ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generación de un id personalizado JIJIJA
            val userId = generateCustomUserId(alias)

            // Se guardan los datos
            editor.putString("userId", userId)
            editor.putString("nombre", nombre)
            editor.putString("alias", alias)
            editor.putString("correo", correo)
            editor.putString("edad", edad)
            editor.putString("clave", clave)
            editor.apply()

            Toast.makeText(this, "Registro exitoso. ID asignado: $userId", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }

        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }
    }

    // Función de un id personalizado
    private fun generateCustomUserId(alias: String): String {
        val currentDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return "USR-${alias.uppercase()}-$currentDate"
    }
}
