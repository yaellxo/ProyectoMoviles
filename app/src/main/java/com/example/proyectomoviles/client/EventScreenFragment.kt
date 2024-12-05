package com.example.proyectomoviles.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.EventoAdapter
import com.example.proyectomoviles.models.EventosManager

class EventScreenFragment : Fragment(R.layout.event_activity) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMainEventos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val eventosList = EventosManager.obtenerEventos()
        val adapter = EventoAdapter(eventosList)
        recyclerView.adapter = adapter
    }
}

