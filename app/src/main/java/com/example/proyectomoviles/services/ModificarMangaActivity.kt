package com.example.proyectomoviles.services

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.repositories.MangaRepository

class ModificarMangaActivity : AppCompatActivity() {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_manga_activity)

        val repository = MangaRepository(this)
        val btnModificar = findViewById<Button>(R.id.fabMangaModificar)
        val btnImagen = findViewById<TextView>(R.id.btnImagenMangaModificar)
        val spinnerGenero = findViewById<Spinner>(R.id.spNivelAccesoMangaModificar)

        val generos = arrayOf("Shonen", "Shojo", "Seinen", "Josei", "Isekai", "Mecha", "Fantasy", "Horror", "Comedy", "Spokon", "Harem", "Slice of life")
        spinnerGenero.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, generos)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = repository.saveImageToInternalStorage(it)
                btnImagen.text = "Imagen seleccionada"
            }
        }

        btnImagen.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnModificar.setOnClickListener {
            val id = findViewById<EditText>(R.id.etIdMangaModificar).text.toString()
            val manga = repository.getManga(id)

            if (manga != null) {
                manga.nombre = findViewById<EditText>(R.id.etNombreMangaModificar).text.toString()
                manga.precio = findViewById<EditText>(R.id.etPrecioMangaModificar).text.toString().toFloat()
                manga.stock = findViewById<EditText>(R.id.etStockMangaModificar).text.toString().toInt()
                manga.descripcion = findViewById<EditText>(R.id.etDescripcionMangaModificar).text.toString()
                manga.volumen = findViewById<EditText>(R.id.etVolumenMangaModificar).text.toString().toInt()
                manga.autor = findViewById<EditText>(R.id.etAutorMangaModificar).text.toString()
                manga.genero = spinnerGenero.selectedItem.toString()
                manga.editorial = findViewById<EditText>(R.id.etEditorialMangaModificar).text.toString()
                manga.publicacion = findViewById<EditText>(R.id.etPublicacionMangaModificar).text.toString()
                manga.imagen = imageUri?.toString() ?: manga.imagen

                repository.saveManga(manga)
                finish()
            }
        }
    }
}
