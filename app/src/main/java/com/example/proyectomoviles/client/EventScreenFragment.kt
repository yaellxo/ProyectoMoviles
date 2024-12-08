package com.example.proyectomoviles.client

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.EventoAdapter
import com.example.proyectomoviles.adapters.ViewPagerAdapter
import com.example.proyectomoviles.models.Evento
import com.example.proyectomoviles.models.EventosManager

class EventScreenFragment : Fragment(R.layout.event_activity), EventoAdapter.OnEventoClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPagerImages)

        val images = listOf(
            R.drawable.event_background,
            R.drawable.event_background_2
        )

        val viewPagerAdapter = ViewPagerAdapter(images)
        viewPager.adapter = viewPagerAdapter

        viewPager.setPageTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f
            page.scaleY = scaleFactor
            page.translationX = -30 * position
        }

        viewPager.offscreenPageLimit = 3

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMainEventos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val eventosList = EventosManager.obtenerEventos()
        val adapter = EventoAdapter(eventosList, requireContext(), this)
        recyclerView.adapter = adapter
    }

    override fun onEventoClick(evento: Evento) {
        val bundle = Bundle().apply {
            putSerializable("evento", evento)
        }
        findNavController().navigate(R.id.action_eventScreenFragment_to_eventoDetalleFragment, bundle)
    }
}
