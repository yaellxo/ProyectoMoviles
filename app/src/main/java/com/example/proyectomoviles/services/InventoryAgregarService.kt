package com.example.proyectomoviles.services

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.ObjectOutputStream

class InventoryAgregarService : AppCompatActivity() {

    private val arbolManga = ArbolBinarioManga()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_manga_activity)

        val etNombreMangaRegistrar: EditText = findViewById(R.id.etNombreMangaRegistrar)
        val etPrecioMangaRegistrar: EditText = findViewById(R.id.etPrecioMangaRegistrar)
        val etStockMangaRegistrar: EditText = findViewById(R.id.etStockMangaRegistrar)
        val etDescripcionMangaRegistrar: EditText = findViewById(R.id.etDescripcionMangaRegistrar)
        val etVolumenMangaRegistrar: EditText = findViewById(R.id.etVolumenMangaRegistrar)
        val etAutorMangaRegistrar: EditText = findViewById(R.id.etAutorMangaRegistrar)
        val etGeneroMangaRegistrar: Spinner = findViewById(R.id.etGeneroMangaRegistrar)
        val etEditorialMangaRegistrar: EditText = findViewById(R.id.etEditorialMangaRegistrar)
        val etPublicacionMangaRegistrar: EditText = findViewById(R.id.etPublicacionMangaRegistrar)
        val btnImagenMangaRegistrar: TextView = findViewById(R.id.btnImagenMangaRegistrar)
        val fabAgregarManga: FloatingActionButton = findViewById(R.id.fabAgregarManga)
        val fabRegresarMangaAgregar: FloatingActionButton = findViewById(R.id.fabRegresarMangaAgregar)

        fabAgregarManga.setOnClickListener {
            val titulo = etNombreMangaRegistrar.text.toString()
            val precio = etPrecioMangaRegistrar.text.toString().toFloatOrNull() ?: 0f
            val stock = etStockMangaRegistrar.text.toString().toIntOrNull() ?: 0
            val descripcion = etDescripcionMangaRegistrar.text.toString()
            val volumen = etVolumenMangaRegistrar.text.toString().toDoubleOrNull() ?: 0.0
            val autor = etAutorMangaRegistrar.text.toString()
            val genero = etGeneroMangaRegistrar.selectedItem.toString()
            val editorial = etEditorialMangaRegistrar.text.toString()
            val publicacion = etPublicacionMangaRegistrar.text.toString()

            val imagenUrl = selectedImageUri?.let { saveImageToInternalStorage(it) } ?: ""

            val mangaId = generateMangaId()

            val manga = Manga(
                titulo = titulo,
                precio = precio,
                stock = stock,
                descripcion = descripcion,
                volumen = volumen,
                autor = autor,
                genero = genero,
                editorial = editorial,
                publicacion = publicacion,
                imagenUrl = imagenUrl,
                mangaId = mangaId
            )

            arbolManga.agregarManga(manga)
            guardarArbolEnArchivo()

            Log.d("InventoryAgregarService", "Manga agregado: ${manga.titulo}, ID: ${manga.mangaId}")

            Toast.makeText(this, "Manga agregado con éxito!", Toast.LENGTH_SHORT).show()

            val resultIntent = Intent()
            resultIntent.putExtra("nuevo_manga", manga)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        btnImagenMangaRegistrar.setOnClickListener {
            openGallery()
        }

        fabRegresarMangaAgregar.setOnClickListener {
            onBackPressed()
        }
    }


    private fun guardarArbolEnArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            val fileOutputStream = FileOutputStream(archivo)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)

            objectOutputStream.writeObject(arbolManga)

            objectOutputStream.close()
            fileOutputStream.close()

            Log.d("InventoryAgregarService", "Árbol guardado exitosamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateMangaId(): String {
        return "MANGA-${System.currentTimeMillis()}"
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let {
                val imagePath = it.toString()
                val btnImagenMangaRegistrar: TextView = findViewById(R.id.btnImagenMangaRegistrar)
                btnImagenMangaRegistrar.text = imagePath
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val fileName = "manga_image_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        val outputStream: OutputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)
        outputStream.close()

        Log.d("InventoryAgregarService", "Imagen guardada en: ${file.absolutePath}")

        return file.absolutePath
    }

    companion object {
        const val REQUEST_IMAGE_PICK = 1001
    }
}
