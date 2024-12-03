package com.example.proyectomoviles.services

import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R

class RegistrarAdminService : AppCompatActivity() {
    private lateinit var etNombreAdminRegistro: EditText
    private lateinit var etAliasAdminRegistro: EditText
    private lateinit var etCorreoAdminRegistro: EditText
    private lateinit var spNivelAccesoAdminRegistro: Spinner
    private lateinit var fabAdminAgregar: FloatingActionButton
    private lateinit var fabRegresarAdminAgregar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_admin_activity)

        etNombreAdminRegistro = findViewById(R.id.etNombreAdminRegistro)
        etAliasAdminRegistro = findViewById(R.id.etAliasAdminRegistro)
        etCorreoAdminRegistro = findViewById(R.id.etCorreoAdminRegistro)
        spNivelAccesoAdminRegistro = findViewById(R.id.spNivelAccesoAdminRegistro)

        fabAdminAgregar = findViewById(R.id.fabAdminAgregar)
        fabRegresarAdminAgregar = findViewById(R.id.fabRegresarAdminAgregar)

        fabAdminAgregar.setOnClickListener {
            // Lógica para agregar un administrador
        }

        fabRegresarAdminAgregar.setOnClickListener {
            // Lógica para regresar a la pantalla anterior
        }
    }
}
