package com.example.proyectomoviles.services

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.client.LoginScreen
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RegistrarAdminService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_admin_activity)

        val etNombreAdminRegistro: EditText = findViewById(R.id.etNombreAdminRegistro)
        val etAliasAdminRegistro: EditText = findViewById(R.id.etAliasAdminRegistro)
        val etCorreoAdminRegistro: EditText = findViewById(R.id.etCorreoAdminRegistro)
        val spNivelAccesoAdminRegistro: Spinner = findViewById(R.id.spNivelAccesoAdminRegistro)
        val etAreaAdminRegistro: EditText = findViewById(R.id.etAreaAdminRegistro)
        val etClaveAdminRegistro: EditText = findViewById(R.id.etClaveAdminRegistro)
        val fabAdminAgregar: FloatingActionButton = findViewById(R.id.fabAdminAgregar)
        val fabRegresarAdminAgregar: FloatingActionButton = findViewById(R.id.fabRegresarAdminAgregar)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        fabAdminAgregar.setOnClickListener {
            val nombre = etNombreAdminRegistro.text.toString().trim()
            val alias = etAliasAdminRegistro.text.toString().trim()
            val correo = etCorreoAdminRegistro.text.toString().trim()
            val area = etAreaAdminRegistro.text.toString().trim()
            val clave = etClaveAdminRegistro.text.toString().trim()
            val nivelAcceso = spNivelAccesoAdminRegistro.selectedItem.toString()

            if (nombre.isEmpty() || alias.isEmpty() || correo.isEmpty() || area.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Correo electrónico no válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val adminsJson = sharedPreferences.getString("admins_data", "[]")
            val admins = JSONArray(adminsJson)

            for (i in 0 until admins.length()) {
                val admin = admins.getJSONObject(i)
                if (admin.getString("alias") == alias) {
                    Toast.makeText(this, "Este alias ya está registrado.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val adminId = generateCustomUserId(alias)

            val nuevoAdmin = JSONObject().apply {
                put("adminId", adminId)
                put("alias", alias)
                put("nombre", nombre)
                put("correo", correo)
                put("area", area)
                put("clave", clave)
                put("nivelAcceso", nivelAcceso)
            }

            admins.put(nuevoAdmin)

            sharedPreferences.edit().putString("admins_data", admins.toString()).apply()

            Log.d("RegistrarAdminService", "Administradores guardados: ${admins.toString()}")

            val intent = Intent(this, LoginScreen::class.java)
            intent.putExtra("nuevoAdmin", nuevoAdmin.toString())
            startActivity(intent)
            finish()
        }

        fabRegresarAdminAgregar.setOnClickListener {
            finish()
        }
    }

    private fun generateCustomUserId(alias: String): String {
        val currentDate = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return "ADM-${alias.uppercase()}-$currentDate"
    }
}
