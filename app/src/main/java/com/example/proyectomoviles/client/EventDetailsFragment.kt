package com.example.proyectomoviles.client

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Evento
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class EventDetailsFragment : Fragment(R.layout.event_detail_activity) {

    private lateinit var evento: Evento

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.event_detail_activity, container, false)
        evento = arguments?.getSerializable("evento") as Evento

        val ivEventoImagen: ImageView = view.findViewById(R.id.ivEventoDetalleImagen)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreDetalleEvento)
        val tvFecha: TextView = view.findViewById(R.id.tvFechaDetalleEvento)
        val tvUbicacion: TextView = view.findViewById(R.id.tvUbicacionDetalleEvento)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcionDetalleEvento)


        Glide.with(requireContext())
            .load(evento.imagenUri)
            .into(ivEventoImagen)

        tvNombre.text = evento.nombre
        tvFecha.text = evento.fecha
        tvUbicacion.text = evento.ubicacion
        tvDescripcion.text = evento.descripcion

        return view
    }

}
