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

            val edad = edadString.toIntOrNull()
            if (edad == null || edad < 12 || edad > 80) {
                Toast.makeText(this, "Por favor, ingrese una edad válida entre 12 y 80 años", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar si el alias ya está registrado
            val storedAliases = sharedPreferences.getStringSet("aliases", mutableSetOf()) ?: mutableSetOf()
            if (storedAliases.contains(alias)) {
                Toast.makeText(this, "Este alias ya está registrado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar datos del usuario
            storedAliases.add(alias)
            editor.putStringSet("aliases", storedAliases)
            editor.putString("nombre_$alias", nombre)
            editor.putString("correo_$alias", correo)
            editor.putString("edad_$alias", edadString)
            editor.putString("clave_$alias", clave)
            editor.putString("userType_$alias", "user")
            editor.apply()

            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

            // Redirigir al LoginScreen después del registro
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
}
