package com.example.proyectomoviles.client.event_screens

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgregarEventoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_evento_activity)

        val etNombreEvento: EditText = findViewById(R.id.etNombreEventoRegistro)
        val etFechaEvento: EditText = findViewById(R.id.etFechaEventoRegistro)
        val etUbicacionEvento: EditText = findViewById(R.id.etUbicacionEventoRegistro)
        val etDescripcionEvento: EditText = findViewById(R.id.etDescripcionEventoRegistro)

        val btnRegistrar: FloatingActionButton = findViewById(R.id.fabEventoAgregar)

       btnRegistrar.setOnClickListener{



           CustomToast.show(this, R.id.fabEventoAgregar)
        }
    }
}