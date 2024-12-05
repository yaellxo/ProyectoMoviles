package com.example.proyectomoviles.models

object EventosManager {
    val eventosList = mutableListOf<Evento>()

    fun agregarEvento(evento: Evento) {
        eventosList.add(evento)
    }

    fun eliminarEvento(eventoIndex: Int) {
        if (eventoIndex in 0 until eventosList.size) {
            eventosList.removeAt(eventoIndex)
        } else {
            println("Índice fuera de rango")
        }
    }

    fun modificarEvento(
        eventoIndex: Int,
        nuevoNombre: String,
        nuevaFecha: String,
        nuevaUbicacion: String,
        nuevaDescripcion: String
    ) {
        if (eventoIndex in 0 until eventosList.size) {
            val evento = eventosList[eventoIndex]
            eventosList[eventoIndex] = evento.copy(
                nombre = nuevoNombre,
                fecha = nuevaFecha,
                ubicacion = nuevaUbicacion,
                descripcion = nuevaDescripcion
            )
        } else {
            println("Índice fuera de rango")
        }
    }

    fun obtenerEventos(): List<Evento> {
        return eventosList
    }
}