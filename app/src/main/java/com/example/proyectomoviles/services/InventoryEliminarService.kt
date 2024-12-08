package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.utils.CustomToast
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.File

class InventoryEliminarService : AppCompatActivity() {

    private lateinit var arbolBinarioManga: ArbolBinarioManga

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_manga_activity)

        cargarArbolDesdeArchivo()

        val etIdMangaEliminar: EditText = findViewById(R.id.etIdMangaEliminar)
        val fabMangaEliminar: FloatingActionButton = findViewById(R.id.fabMangaEliminar)

        fabMangaEliminar.setOnClickListener {
            val mangaId = etIdMangaEliminar.text.toString().trim()

            Log.d("InventoryEliminarService", "ID ingresado para eliminar: $mangaId")

            if (mangaId.isNotEmpty()) {
                val fueEliminado = arbolBinarioManga.eliminarManga(mangaId)

                if (fueEliminado) {
                    guardarArbolEnArchivo()

                    val resultIntent = Intent()
                    resultIntent.putExtra("manga_eliminado", mangaId)
                    setResult(RESULT_OK, resultIntent)
                    finish()

                    CustomToast.show(this,740)

                    finish()
                } else {
                    CustomToast.show(this,750)
                }

                val mangasEnOrden = arbolBinarioManga.obtenerMangasEnOrden()
                Log.d("InventoryEliminarService", "Árbol después de la eliminación (en orden): ${mangasEnOrden.joinToString()}")
            } else {
                CustomToast.show(this,500)
            }
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
                Log.d("InventoryEliminarService", "Árbol cargado exitosamente.")
            } else {
                arbolBinarioManga = ArbolBinarioManga()
                Log.d("InventoryEliminarService", "El archivo no existe, creando un árbol vacío.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("InventoryEliminarService", "Error al cargar el árbol: ${e.message}")
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

            Log.d("InventoryEliminarService", "Árbol guardado exitosamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}