package com.example.proyectomoviles.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Evento

class EventoAdapter(private val eventos: List<Evento>) :
    RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEvento)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacionEvento)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionEvento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        holder.tvNombre.text = evento.nombre
        holder.tvFecha.text = evento.fecha
        holder.tvUbicacion.text = evento.ubicacion
        holder.tvDescripcion.text = evento.descripcion
    }

    override fun getItemCount(): Int = eventos.size
}
