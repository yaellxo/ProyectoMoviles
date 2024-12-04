package com.example.proyectomoviles.services

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.repositories.MangaRepository

class EliminarMangaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_manga_activity)

        val repository = MangaRepository(this)
        val btnEliminar = findViewById<Button>(R.id.fabMangaEliminar)

        btnEliminar.setOnClickListener {
            val id = findViewById<EditText>(R.id.etIdMangaEliminar).text.toString()
            repository.deleteManga(id)
            finish()
        }
    }
}
