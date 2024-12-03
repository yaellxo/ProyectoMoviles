package com.example.proyectomoviles.client.event_screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgregarEventoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_evento_activity)

        val btnRegistrar: FloatingActionButton = findViewById(R.id.fabEventoAgregar)

       btnRegistrar.setOnClickListener{
           CustomToast.show(this, R.id.fabEventoAgregar)
        }
    }
}