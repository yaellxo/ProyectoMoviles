package com.example.proyectomoviles.services

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class ModificarAdminService : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_admin_activity)

        val etIdAdminModificar: EditText = findViewById(R.id.etIdAdminModificar)
        val etNombreAdminModificar: EditText = findViewById(R.id.etNombreAdminModificar)
        val etAliasAdminModificar: EditText = findViewById(R.id.etAliasAdminModificar)
        val etCorreoAdminModificar: EditText = findViewById(R.id.etCorreoAdminModificar)
        val spNivelAccesoAdminModificar: Spinner = findViewById(R.id.spNivelAccesoAdminModificar)
        val etAreaAdminModificar: EditText = findViewById(R.id.etAreaAdminModificar)
        val etClaveAdminModificar: EditText = findViewById(R.id.etClaveAdminModificar)
        val fabAdminModificar: FloatingActionButton = findViewById(R.id.fabAdminModificar)

        disableFields(etNombreAdminModificar, etAliasAdminModificar, etCorreoAdminModificar, spNivelAccesoAdminModificar, etAreaAdminModificar, etClaveAdminModificar)

        val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val adminsJson = sharedPreferences.getString("admins_data", "[]")
        val admins = JSONArray(adminsJson)

        etIdAdminModificar.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENTER) {
                val adminId = etIdAdminModificar.text.toString().trim()
                if (adminId.isEmpty()) {
                    CustomToast.show(this,610)
                    return@setOnEditorActionListener true
                }

                val adminIndex = findAdminIndexById(adminId, admins)
                if (adminIndex == -1) {
                    CustomToast.show(this,720)
                    return@setOnEditorActionListener true
                }

                val admin = admins.getJSONObject(adminIndex)
                etNombreAdminModificar.setText(admin.getString("nombre"))
                etAliasAdminModificar.setText(admin.getString("alias"))
                etCorreoAdminModificar.setText(admin.getString("correo"))
                etAreaAdminModificar.setText(admin.getString("area"))
                etClaveAdminModificar.setText(admin.getString("clave"))

                val nivelAcceso = admin.getString("nivelAcceso")
                val spinnerAdapter = spNivelAccesoAdminModificar.adapter
                for (i in 0 until spinnerAdapter.count) {
                    if (spinnerAdapter.getItem(i).toString() == nivelAcceso) {
                        spNivelAccesoAdminModificar.setSelection(i)
                        break
                    }
                }

                enableFields(etNombreAdminModificar, etAliasAdminModificar, etCorreoAdminModificar, spNivelAccesoAdminModificar, etAreaAdminModificar, etClaveAdminModificar)

                CustomToast.show(this,400)
                true
            } else {
                false
            }
        }

        etIdAdminModificar.setOnClickListener {
            val adminId = etIdAdminModificar.text.toString().trim()
            if (adminId.isNotEmpty()) {
                val adminIndex = findAdminIndexById(adminId, admins)
                if (adminIndex != -1) {
                    val admin = admins.getJSONObject(adminIndex)
                    etNombreAdminModificar.setText(admin.getString("nombre"))
                    etAliasAdminModificar.setText(admin.getString("alias"))
                    etCorreoAdminModificar.setText(admin.getString("correo"))
                    etAreaAdminModificar.setText(admin.getString("area"))
                    etClaveAdminModificar.setText(admin.getString("clave"))

                    val nivelAcceso = admin.getString("nivelAcceso")
                    val spinnerAdapter = spNivelAccesoAdminModificar.adapter
                    for (i in 0 until spinnerAdapter.count) {
                        if (spinnerAdapter.getItem(i).toString() == nivelAcceso) {
                            spNivelAccesoAdminModificar.setSelection(i)
                            break
                        }
                    }

                    enableFields(etNombreAdminModificar, etAliasAdminModificar, etCorreoAdminModificar, spNivelAccesoAdminModificar, etAreaAdminModificar, etClaveAdminModificar)

                    CustomToast.show(this,400)
                } else {
                    CustomToast.show(this,720)
                }
            }
        }

        fabAdminModificar.setOnClickListener {
            val adminId = etIdAdminModificar.text.toString().trim()

            if (adminId.isEmpty()) {
                CustomToast.show(this,610)
                return@setOnClickListener
            }

            val adminIndex = findAdminIndexById(adminId, admins)
            if (adminIndex == -1) {
                CustomToast.show(this,720)
                return@setOnClickListener
            }

            val admin = admins.getJSONObject(adminIndex)
            admin.put("nombre", etNombreAdminModificar.text.toString().trim())
            admin.put("alias", etAliasAdminModificar.text.toString().trim())
            admin.put("correo", etCorreoAdminModificar.text.toString().trim())
            admin.put("area", etAreaAdminModificar.text.toString().trim())
            admin.put("clave", etClaveAdminModificar.text.toString().trim())
            admin.put("nivelAcceso", spNivelAccesoAdminModificar.selectedItem.toString())

            sharedPreferences.edit().putString("admins_data", admins.toString()).apply()
            CustomToast.show(this,780)

            Log.d("ModificarAdminService", "Administrador modificado: $admin")

            clearFields()

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

    private fun disableFields(vararg views: View) {
        for (view in views) {
            view.isEnabled = false
        }
    }

    private fun enableFields(vararg views: View) {
        for (view in views) {
            view.isEnabled = true
        }
    }

    private fun clearFields() {
        val etIdAdminModificar: EditText = findViewById(R.id.etIdAdminModificar)
        val etNombreAdminModificar: EditText = findViewById(R.id.etNombreAdminModificar)
        val etAliasAdminModificar: EditText = findViewById(R.id.etAliasAdminModificar)
        val etCorreoAdminModificar: EditText = findViewById(R.id.etCorreoAdminModificar)
        val spNivelAccesoAdminModificar: Spinner = findViewById(R.id.spNivelAccesoAdminModificar)
        val etAreaAdminModificar: EditText = findViewById(R.id.etAreaAdminModificar)
        val etClaveAdminModificar: EditText = findViewById(R.id.etClaveAdminModificar)

        etIdAdminModificar.text.clear()
        etNombreAdminModificar.text.clear()
        etAliasAdminModificar.text.clear()
        etCorreoAdminModificar.text.clear()
        spNivelAccesoAdminModificar.setSelection(0)
        etAreaAdminModificar.text.clear()
        etClaveAdminModificar.text.clear()
    }
}