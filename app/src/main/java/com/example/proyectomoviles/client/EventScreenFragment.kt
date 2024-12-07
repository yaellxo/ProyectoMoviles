package com.example.proyectomoviles.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.EventoAdapter
import com.example.proyectomoviles.adapters.ViewPagerAdapter
import com.example.proyectomoviles.models.EventosManager

class EventScreenFragment : Fragment(R.layout.event_activity) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del ViewPager2
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPagerImages)

        // Lista de imágenes
        val images = listOf(
            R.drawable.event_background,
            R.drawable.event_background_2
        )

        // Configuramos el adapter para el ViewPager2
        val viewPagerAdapter = ViewPagerAdapter(images)
        viewPager.adapter = viewPagerAdapter

        // Configuramos el efecto de separación y escalado
        viewPager.setPageTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f
            page.scaleY = scaleFactor
            page.translationX = -30 * position
        }

        // Mostramos un poco de la siguiente página
        viewPager.offscreenPageLimit = 3

        // Configuración del RecyclerView (no se toca)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMainEventos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val eventosList = EventosManager.obtenerEventos()
        val adapter = EventoAdapter(eventosList, requireContext())
        recyclerView.adapter = adapter
    }
}
