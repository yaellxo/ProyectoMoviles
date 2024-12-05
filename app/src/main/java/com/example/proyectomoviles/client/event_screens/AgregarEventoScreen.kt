package com.example.proyectomoviles.client.event_screens

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Evento
import com.example.proyectomoviles.models.EventosManager
import com.example.proyectomoviles.services.EventService
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgregarEventoScreen : AppCompatActivity() {

    private lateinit var fabRegresarEvento: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_evento_activity)

        val nombreField = findViewById<EditText>(R.id.etNombreEventoRegistro)
        val fechaField = findViewById<EditText>(R.id.etFechaEventoRegistro)
        val ubicacionField = findViewById<EditText>(R.id.etUbicacionEventoRegistro)
        val descripcionField = findViewById<EditText>(R.id.etDescripcionEventoRegistro)

        fabRegresarEvento = findViewById(R.id.fabRegresarEventoAgregar)
        val btnRegistrar: FloatingActionButton = findViewById(R.id.fabEventoAgregar)

        btnRegistrar.setOnClickListener{

            val nombre = nombreField.text.toString()
            val fecha = fechaField.text.toString()
            val ubicacion = ubicacionField.text.toString()
            val descripcion = descripcionField.text.toString()

            if (nombre.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty()) {
                CustomToast.show(this, 600)
            } else{
                val nuevoEvento = Evento(nombre, fecha, ubicacion, descripcion)
                EventosManager.agregarEvento(nuevoEvento)
                CustomToast.show(this, R.id.fabEventoAgregar)
                val intent = Intent(this, EventService::class.java)
                startActivity(intent)
            }
        }

        fabRegresarEvento.setOnClickListener{
            finish()
        }
    }
}