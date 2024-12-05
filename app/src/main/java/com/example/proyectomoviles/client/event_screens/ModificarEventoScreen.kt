package com.example.proyectomoviles.client.event_screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ModificarEventoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_evento_activity)

        val btnModificar: FloatingActionButton = findViewById(R.id.fabEventoModificar)

        btnModificar.setOnClickListener{
            CustomToast.show(this, R.id.fabEventoModificar)
        }
    }
}