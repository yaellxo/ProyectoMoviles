package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.EventoAdapter
import com.example.proyectomoviles.client.event_screens.AgregarEventoScreen
import com.example.proyectomoviles.client.event_screens.EliminarEventoScreen
import com.example.proyectomoviles.client.event_screens.ModificarEventoScreen
import com.example.proyectomoviles.models.EventosManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EventService : AppCompatActivity() {

    private lateinit var fabRegresarEvento: FloatingActionButton
    private lateinit var additionalButtons: List<FloatingActionButton>
    private lateinit var fabMain: FloatingActionButton

    private val fabOpenIcon = R.drawable.ic_plus_admin
    private val fabCloseIcon = R.drawable.ic_cerrar_menu_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_event_activity)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEventos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val eventosList = EventosManager.obtenerEventos()
        val adapter = EventoAdapter(eventosList)
        recyclerView.adapter = adapter

        fabRegresarEvento = findViewById(R.id.fabRegresarEvento)
        fabMain = findViewById(R.id.fabEvento)

        additionalButtons = listOf(
            findViewById(R.id.agregarEvento),
            findViewById(R.id.modificarEvento),
            findViewById(R.id.eliminarEvento)
        )

        fabMain.setOnClickListener {
            toggleAdditionalButtons()
        }

        fabMain.setImageResource(fabOpenIcon)
        hideAdditionalButtons()

        additionalButtons[0].setImageResource(R.drawable.agregar_evento)
        additionalButtons[1].setImageResource(R.drawable.modificar_evento)
        additionalButtons[2].setImageResource(R.drawable.eliminar_evento)

        additionalButtons[0].setOnClickListener {
            val intent = Intent(this, AgregarEventoScreen::class.java)
            startActivity(intent)
        }

        additionalButtons[1].setOnClickListener {
            val intent = Intent(this, ModificarEventoScreen::class.java)
            startActivity(intent)
        }

        additionalButtons[2].setOnClickListener {
            val intent = Intent(this, EliminarEventoScreen::class.java)
            startActivity(intent)
        }

        fabRegresarEvento.setOnClickListener{
            finish()
        }
    }

    private fun hideAdditionalButtons() {
        additionalButtons.forEach { button ->
            button.animate()
                .translationY(0f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction { button.visibility = View.GONE }
                .start()
        }
        fabMain.setImageResource(fabOpenIcon)
    }

    private fun showAdditionalButtons() {
        additionalButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.alpha = 0f
            button.animate()
                .translationY(-200f * (index + 1))
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        fabMain.setImageResource(fabCloseIcon)
    }

    private fun toggleAdditionalButtons() {
        val areButtonsVisible = additionalButtons.first().visibility == View.VISIBLE
        if (areButtonsVisible) {
            hideAdditionalButtons()
        } else {
            showAdditionalButtons()
        }
    }
}
