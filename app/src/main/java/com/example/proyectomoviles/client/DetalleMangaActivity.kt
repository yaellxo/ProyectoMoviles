package com.example.proyectomoviles.client

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import java.io.File

class DetalleMangaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_manga)

        val manga = intent.getSerializableExtra("manga") as Manga

        val tituloTextView: TextView = findViewById(R.id.etTitulo)
        val autorTextView: TextView = findViewById(R.id.etAutor)
        val calificacionRatingBar: RatingBar = findViewById(R.id.rbCalificacion)
        val imagenImageView: ImageView = findViewById(R.id.mangaImagenView)
        val precioTextView: TextView = findViewById(R.id.tvPrecio)
        val volumenTextView: TextView = findViewById(R.id.tvVolumen)
        val generoTextView: TextView = findViewById(R.id.tvGenero)
        val editorialTextView: TextView = findViewById(R.id.tvEditorial)
        val publicacionTextView: TextView = findViewById(R.id.tvPublicacion)
        val descripcionTextView: TextView = findViewById(R.id.tvDescripcion)

        tituloTextView.text = manga.titulo
        autorTextView.text = manga.autor
        precioTextView.text = "$${manga.precio}"
        volumenTextView.text = "Volumen: ${manga.volumen}"
        generoTextView.text = "Género: ${manga.genero}"
        editorialTextView.text = "Editorial: ${manga.editorial}"
        publicacionTextView.text = "Publicación: ${manga.publicacion}"
        descripcionTextView.text = manga.descripcion

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imagenImageView.setImageBitmap(bitmap)
        } else {
            imagenImageView.setImageResource(R.drawable.ic_deafaultmanga)
        }

        calificacionRatingBar.rating = 4.5f
    }
}
