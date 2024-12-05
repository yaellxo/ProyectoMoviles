package com.example.proyectomoviles.services

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class EliminarAdminService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_admin_activity)

        val etIdAdminEliminar: EditText = findViewById(R.id.etIdAdminEliminar)
        val fabAdminEliminar: FloatingActionButton = findViewById(R.id.fabAdminEliminar)
        val fabRegresarAdminEliminar: FloatingActionButton = findViewById(R.id.fabRegresarAdminEliminar)

        // Obtenemos las SharedPreferences donde están guardados los datos de los administradores
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val adminsJson = sharedPreferences.getString("admins_data", "[]")
        val admins = JSONArray(adminsJson)

        // Escuchar la tecla Enter para eliminar el administrador
        etIdAdminEliminar.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENTER) {
                val adminId = etIdAdminEliminar.text.toString().trim()

                if (adminId.isEmpty()) {
                    Toast.makeText(this, "Por favor, ingrese el ID del administrador.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }

                val adminIndex = findAdminIndexById(adminId, admins)
                if (adminIndex == -1) {
                    Toast.makeText(this, "Administrador no encontrado.", Toast.LENGTH_SHORT).show()
                    return@setOnEditorActionListener true
                }

                // Eliminar el administrador
                admins.remove(adminIndex)

                // Guardar los datos actualizados
                sharedPreferences.edit().putString("admins_data", admins.toString()).apply()

                Toast.makeText(this, "Administrador eliminado con éxito.", Toast.LENGTH_SHORT).show()

                // Limpiar el campo de texto
                etIdAdminEliminar.text.clear()

                finish()
                true
            } else {
                false
            }
        }

        fabAdminEliminar.setOnClickListener {
            val adminId = etIdAdminEliminar.text.toString().trim()

            if (adminId.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese el ID del administrador.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val adminIndex = findAdminIndexById(adminId, admins)
            if (adminIndex == -1) {
                Toast.makeText(this, "Administrador no encontrado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Eliminar el administrador
            admins.remove(adminIndex)

            // Guardar los datos actualizados
            sharedPreferences.edit().putString("admins_data", admins.toString()).apply()

            Toast.makeText(this, "Administrador eliminado con éxito.", Toast.LENGTH_SHORT).show()

            // Limpiar el campo de texto
            etIdAdminEliminar.text.clear()

            finish()
        }

        fabRegresarAdminEliminar.setOnClickListener {
            finish()
        }
    }

    // Función para buscar el índice del administrador por ID
    private fun findAdminIndexById(adminId: String, admins: JSONArray): Int {
        for (i in 0 until admins.length()) {
            val admin = admins.getJSONObject(i)
            if (admin.getString("adminId") == adminId) {
                return i
            }
        }
        return -1
    }
}
