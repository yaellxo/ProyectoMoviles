package com.example.proyectomoviles.services

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.OutputStream
import java.io.ObjectOutputStream

class InventoryModificarService : AppCompatActivity() {

    private lateinit var arbolBinarioManga: ArbolBinarioManga
    private var selectedImageUri: Uri? = null
    private var currentManga: Manga? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_manga_activity)

        cargarArbolDesdeArchivo()

        val etIdMangaModificar: EditText = findViewById(R.id.etIdMangaModificar)
        val etNombreMangaModificar: EditText = findViewById(R.id.etNombreMangaModificar)
        val etPrecioMangaModificar: EditText = findViewById(R.id.etPrecioMangaModificar)
        val etStockMangaModificar: EditText = findViewById(R.id.etStockMangaModificar)
        val etDescripcionMangaModificar: EditText = findViewById(R.id.etDescripcionMangaModificar)
        val etVolumenMangaModificar: EditText = findViewById(R.id.etVolumenMangaModificar)
        val etAutorMangaModificar: EditText = findViewById(R.id.etAutorMangaModificar)
        val spGeneroMangaModificar: Spinner = findViewById(R.id.spGeneroMangaModificar)
        val etEditorialMangaModificar: EditText = findViewById(R.id.etEditorialMangaModificar)
        val etPublicacionMangaModificar: EditText = findViewById(R.id.etPublicacionMangaModificar)
        val btnImagenMangaModificar: TextView = findViewById(R.id.btnImagenMangaModificar)
        val fabMangaModificar: FloatingActionButton = findViewById(R.id.fabMangaModificar)
        val fabRegresarMangaModificar: FloatingActionButton = findViewById(R.id.fabRegresarMangaModificar)

        fabMangaModificar.visibility = View.GONE

        etNombreMangaModificar.isEnabled = false
        etPrecioMangaModificar.isEnabled = false
        etStockMangaModificar.isEnabled = false
        etDescripcionMangaModificar.isEnabled = false
        etVolumenMangaModificar.isEnabled = false
        etAutorMangaModificar.isEnabled = false
        spGeneroMangaModificar.isEnabled = false
        etEditorialMangaModificar.isEnabled = false
        etPublicacionMangaModificar.isEnabled = false
        btnImagenMangaModificar.isEnabled = false

        etIdMangaModificar.setOnEditorActionListener { _, _, _ ->
            val mangaId = etIdMangaModificar.text.toString()
            val manga = arbolBinarioManga.buscarPorId(mangaId)

            manga?.let {
                currentManga = it
                etNombreMangaModificar.setText(it.titulo)
                etPrecioMangaModificar.setText(it.precio.toString())
                etStockMangaModificar.setText(it.stock.toString())
                etDescripcionMangaModificar.setText(it.descripcion)
                etVolumenMangaModificar.setText(it.volumen.toString())
                etAutorMangaModificar.setText(it.autor)
                etEditorialMangaModificar.setText(it.editorial)
                etPublicacionMangaModificar.setText(it.publicacion)

                val genero = it.genero
                val generoArray = resources.getStringArray(R.array.generos)
                val generoPosition = generoArray.indexOfFirst { it.equals(genero, ignoreCase = true) }

                if (generoPosition != -1) {
                    spGeneroMangaModificar.setSelection(generoPosition)
                } else {
                    Log.e("ModificarManga", "Género no encontrado en el Spinner: $genero")
                }

                if (it.imagenUrl.isNotEmpty()) {
                    btnImagenMangaModificar.text = "Imagen seleccionada: ${it.imagenUrl}"
                }

                etNombreMangaModificar.isEnabled = true
                etPrecioMangaModificar.isEnabled = true
                etStockMangaModificar.isEnabled = true
                etDescripcionMangaModificar.isEnabled = true
                etVolumenMangaModificar.isEnabled = true
                etAutorMangaModificar.isEnabled = true
                spGeneroMangaModificar.isEnabled = true
                etEditorialMangaModificar.isEnabled = true
                etPublicacionMangaModificar.isEnabled = true
                btnImagenMangaModificar.isEnabled = true

                fabMangaModificar.visibility = View.VISIBLE

            } ?: run {
                CustomToast.show(this,760)

                fabMangaModificar.visibility = View.GONE
            }
            true
        }

        fabMangaModificar.setOnClickListener {
            val mangaId = currentManga?.mangaId ?: ""

            if (mangaId.isNotEmpty() && arbolBinarioManga.buscarPorId(mangaId) != null) {
                val nuevoManga = Manga(
                    mangaId = mangaId,
                    titulo = etNombreMangaModificar.text.toString(),
                    precio = try {
                        etPrecioMangaModificar.text.toString().toFloat()
                    } catch (e: NumberFormatException) {
                        0f
                    },
                    stock = try {
                        etStockMangaModificar.text.toString().toInt()
                    } catch (e: NumberFormatException) {
                        0
                    },
                    descripcion = etDescripcionMangaModificar.text.toString(),
                    volumen = try {
                        etVolumenMangaModificar.text.toString().toDouble()
                    } catch (e: NumberFormatException) {
                        0.0
                    },
                    autor = etAutorMangaModificar.text.toString(),
                    genero = spGeneroMangaModificar.selectedItem?.toString() ?: "",
                    editorial = etEditorialMangaModificar.text.toString(),
                    publicacion = etPublicacionMangaModificar.text.toString(),
                    imagenUrl = selectedImageUri?.let {
                        saveImageToInternalStorage(it)
                    } ?: currentManga?.imagenUrl ?: ""
                )

                if (arbolBinarioManga.modificarManga(mangaId, nuevoManga)) {
                    CustomToast.show(this,910)
                    guardarArbolEnArchivo()
                } else {
                    CustomToast.show(this,920)
                }
            } else {
                CustomToast.show(this,760)
            }
        }

        btnImagenMangaModificar.setOnClickListener {
            openGallery()
        }

        fabRegresarMangaModificar.setOnClickListener {
            onBackPressed()
        }
    }

    private fun cargarArbolDesdeArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            if (archivo.exists()) {
                val fileInputStream = FileInputStream(archivo)
                val objectInputStream = ObjectInputStream(fileInputStream)
                arbolBinarioManga = objectInputStream.readObject() as ArbolBinarioManga
                objectInputStream.close()
                fileInputStream.close()
                Log.d("InventoryModificarService", "Árbol cargado exitosamente.")
            } else {
                arbolBinarioManga = ArbolBinarioManga()
                Log.d("InventoryModificarService", "El archivo no existe, creando un árbol vacío.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("InventoryModificarService", "Error al cargar el árbol: ${e.message}")
            arbolBinarioManga = ArbolBinarioManga()
        }
    }

    private fun guardarArbolEnArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            val fileOutputStream = FileOutputStream(archivo)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)

            objectOutputStream.writeObject(arbolBinarioManga)

            objectOutputStream.close()
            fileOutputStream.close()

            Log.d("InventoryModificarService", "Árbol guardado exitosamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                val btnImagenMangaModificar: TextView = findViewById(R.id.btnImagenMangaModificar)
                btnImagenMangaModificar.text = "Imagen seleccionada: $imagePath"
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

        Log.d("InventoryModificarService", "Imagen guardada en: ${file.absolutePath}")

        return file.absolutePath
    }

    companion object {
        const val REQUEST_IMAGE_PICK = 1001
    }
}
