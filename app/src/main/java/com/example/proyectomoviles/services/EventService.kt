package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Evento
import com.example.proyectomoviles.client.event_screens.AgregarEventoScreen
import com.example.proyectomoviles.client.event_screens.EliminarEventoScreen
import com.example.proyectomoviles.client.event_screens.ModificarEventoScreen


class EventService : AppCompatActivity() {

    private val eventos = ArrayList<Evento>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_event_activity)

        val btnRegistrar: ImageButton = findViewById(R.id.btnAgergarEvento)
        val btnEliminar: ImageButton = findViewById(R.id.btnEliminarEvento)
        val btnModificar: ImageButton = findViewById(R.id.btnModificarEvento)

        btnRegistrar.setOnClickListener{
            val intent = Intent(this, AgregarEventoScreen::class.java)
            startActivity(intent)
        }

        btnEliminar.setOnClickListener{
            val intent = Intent(this, EliminarEventoScreen::class.java)
            startActivity(intent)
        }

        btnModificar.setOnClickListener{
            val intent = Intent(this, ModificarEventoScreen::class.java)
            startActivity(intent)
        }
    }
}
