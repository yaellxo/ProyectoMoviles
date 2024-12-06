package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.MangaAdapter
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class InventoryService : AppCompatActivity() {

    private lateinit var fabRegresarInventario: FloatingActionButton
    private lateinit var additionalButtons: List<FloatingActionButton>
    private lateinit var fabMain: FloatingActionButton
    private lateinit var arbolBinarioManga: ArbolBinarioManga
    private lateinit var recyclerViewMangas: RecyclerView
    private lateinit var mangaAdapter: MangaAdapter

    private val fabOpenIcon = R.drawable.ic_plus_admin
    private val fabCloseIcon = R.drawable.ic_cerrar_menu_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_manga)

        // Inicialización de vistas antes de usarlas
        recyclerViewMangas = findViewById(R.id.recyclerViewMangas)
        fabRegresarInventario = findViewById(R.id.fabRegresarInventario)
        fabMain = findViewById(R.id.fab)
        additionalButtons = listOf(
            findViewById(R.id.agregarManga),
            findViewById(R.id.modificarManga),
            findViewById(R.id.eliminarManga)
        )

        // Cargar el árbol antes de mostrar los datos
        cargarArbolDesdeArchivo()

        // Obtener los mangas del árbol y asegurarse de que el adaptador esté inicializado
        val mangas = arbolBinarioManga.obtenerMangasEnOrden()
        Log.d("InventoryService", "Número de mangas cargados: ${mangas.size}")
        mangas.forEachIndexed { index, manga ->
            Log.d("InventoryService", "Manga $index: ${manga.titulo}")
        }

        // Inicializar el adaptador con los mangas
        mangaAdapter = MangaAdapter(mangas.toMutableList())

        // Establecer el adaptador y el layout manager
        recyclerViewMangas.adapter = mangaAdapter
        recyclerViewMangas.layoutManager = LinearLayoutManager(this)

        // Configuración de FAB y botones adicionales
        fabMain.setOnClickListener {
            toggleAdditionalButtons()
        }
        fabMain.setImageResource(fabOpenIcon)
        hideAdditionalButtons()

        additionalButtons[0].setOnClickListener {
            val intent = Intent(this, InventoryAgregarService::class.java)
            startActivityForResult(intent, REQUEST_ADD_MANGA)
        }
        additionalButtons[1].setOnClickListener {
            val intent = Intent(this, InventoryModificarService::class.java)
            startActivity(intent)
        }
        additionalButtons[2].setOnClickListener {
            val intent = Intent(this, InventoryEliminarService::class.java)
            startActivity(intent)
        }
    }

    // Método para ocultar los botones adicionales
    private fun hideAdditionalButtons() {
        additionalButtons.forEach { button ->
            button.animate()
                .translationY(0f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction { button.visibility = View.GONE }
                .start()
        }
        fabMain.setImageResource(fabOpenIcon)
    }

    // Método para mostrar los botones adicionales
    private fun showAdditionalButtons() {
        additionalButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.animate()
                .translationY((-100 * (index + 1)).toFloat())
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        fabMain.setImageResource(fabCloseIcon)
    }

    // Método para alternar entre mostrar y ocultar los botones adicionales
    private fun toggleAdditionalButtons() {
        if (additionalButtons[0].visibility == View.GONE) {
            showAdditionalButtons()
        } else {
            hideAdditionalButtons()
        }
    }

    // Método para mostrar los datos del árbol en el RecyclerView
    private fun mostrarDatosDelArbol() {
        val mangas = arbolBinarioManga.obtenerMangasEnOrden()
        mangaAdapter.actualizarMangas(mangas)
    }

    // Manejo de resultados al agregar un nuevo manga
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_MANGA && resultCode == RESULT_OK) {
            val nuevoManga = data?.getSerializableExtra("nuevo_manga") as Manga
            arbolBinarioManga.agregarManga(nuevoManga)
            mangaAdapter.agregarManga(nuevoManga)
            guardarArbolEnArchivo()
        }
    }

    // Método para cargar el árbol de mangas desde un archivo
    private fun cargarArbolDesdeArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            if (archivo.exists()) {
                val fileInputStream = FileInputStream(archivo)
                val objectInputStream = ObjectInputStream(fileInputStream)
                arbolBinarioManga = objectInputStream.readObject() as ArbolBinarioManga
                objectInputStream.close()
                fileInputStream.close()
            } else {
                arbolBinarioManga = ArbolBinarioManga()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Método para guardar el árbol de mangas en un archivo
    private fun guardarArbolEnArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            val fileOutputStream = FileOutputStream(archivo)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(arbolBinarioManga)
            objectOutputStream.close()
            fileOutputStream.close()
            Log.d("InventoryService", "Árbol guardado exitosamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val REQUEST_ADD_MANGA = 1002
    }
}

