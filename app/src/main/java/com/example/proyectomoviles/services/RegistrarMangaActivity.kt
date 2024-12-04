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
import java.util.*

class RegistrarMangaActivity : AppCompatActivity() {
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_manga_activity)

        val repository = MangaRepository(this)
        val btnRegistrar = findViewById<Button>(R.id.fabModificarManga)
        val btnImagen = findViewById<TextView>(R.id.btnImagenMangaRegistrar)
        val spinnerGenero = findViewById<Spinner>(R.id.etGeneroMangaRegistrar)

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

        btnRegistrar.setOnClickListener {
            val manga = Manga(
                id = UUID.randomUUID().toString(),
                nombre = findViewById<EditText>(R.id.etNombreMangaRegistrar).text.toString(),
                precio = findViewById<EditText>(R.id.etPrecioMangaRegistrar).text.toString().toFloat(),
                stock = findViewById<EditText>(R.id.etStockMangaRegistrar).text.toString().toInt(),
                descripcion = findViewById<EditText>(R.id.etDescripcionMangaRegistrar).text.toString(),
                volumen = findViewById<EditText>(R.id.etVolumenMangaRegistrar).text.toString().toInt(),
                autor = findViewById<EditText>(R.id.etAutorMangaRegistrar).text.toString(),
                genero = spinnerGenero.selectedItem.toString(),
                editorial = findViewById<EditText>(R.id.etEditorialMangaRegistrar).text.toString(),
                publicacion = findViewById<EditText>(R.id.etPublicacionMangaRegistrar).text.toString(),
                imagen = imageUri.toString()
            )

            repository.saveManga(manga)
            finish()
        }
    }
}
