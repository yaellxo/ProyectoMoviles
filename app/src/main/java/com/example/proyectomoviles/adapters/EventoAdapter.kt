package com.example.proyectomoviles.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Evento

class EventoAdapter(
    private var eventos: List<Evento>,
    private val context: Context,
    private val listener: OnEventoClickListener? = null
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    interface OnEventoClickListener {
        fun onEventoClick(evento: Evento)
    }

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivEventoImagen: ImageView = itemView.findViewById(R.id.ivEventoImagen)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEvento)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaEvento)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacionEvento)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionEvento)
    }

    fun resetEvents(eventosOriginales : List<Evento>){
        eventos = eventosOriginales
    }

    fun updateEventos(index: Int) {
        val eventoSeleccionado = eventos[index]
        eventos = listOf(eventoSeleccionado)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]

        Glide.with(context)
            .load(evento.imagenUri)
            .into(holder.ivEventoImagen)

        holder.tvNombre.text = evento.nombre
        holder.tvFecha.text = evento.fecha
        holder.tvUbicacion.text = evento.ubicacion
        holder.tvDescripcion.text = evento.descripcion

        holder.itemView.setOnClickListener {
            listener?.onEventoClick(evento)
        }
    }

    override fun getItemCount(): Int = eventos.size
}
