package com.example.proyectomoviles.services

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class EliminarAdminService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_admin_activity)

        val etIdAdminEliminar: EditText = findViewById(R.id.etIdAdminEliminar)
        val fabAdminEliminar: FloatingActionButton = findViewById(R.id.fabAdminEliminar)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val adminsJson = sharedPreferences.getString("admins_data", "[]")
        val admins = JSONArray(adminsJson)

        etIdAdminEliminar.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENTER) {
                val adminId = etIdAdminEliminar.text.toString().trim()

                if (adminId.isEmpty()) {
                    CustomToast.show(this,610)
                    return@setOnEditorActionListener true
                }

                val adminIndex = findAdminIndexById(adminId, admins)
                if (adminIndex == -1) {
                    CustomToast.show(this, 720)
                    return@setOnEditorActionListener true
                }

                admins.remove(adminIndex)

                sharedPreferences.edit().putString("admins_data", admins.toString()).apply()

                CustomToast.show(this,730)

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
                CustomToast.show(this,610)
                return@setOnClickListener
            }

            val adminIndex = findAdminIndexById(adminId, admins)
            if (adminIndex == -1) {
                CustomToast.show(this,720)
                return@setOnClickListener
            }

            admins.remove(adminIndex)

            sharedPreferences.edit().putString("admins_data", admins.toString()).apply()

            CustomToast.show(this,730)

            etIdAdminEliminar.text.clear()

            finish()
        }


    }

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
