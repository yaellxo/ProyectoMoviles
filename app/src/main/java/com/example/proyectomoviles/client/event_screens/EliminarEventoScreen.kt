package com.example.proyectomoviles.client.event_screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EliminarEventoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_evento_activity)

        val btnElimiar: FloatingActionButton = findViewById(R.id.fabEventoEliminar)

        btnElimiar.setOnClickListener{
            CustomToast.show(this, R.id.fabEventoEliminar)
        }
    }
}