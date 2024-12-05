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
        cargarArbolDesdeArchivo()

        arbolBinarioManga = ArbolBinarioManga()

        val manga = intent.getSerializableExtra("manga") as? Manga

        if (manga != null) {
            arbolBinarioManga.agregarManga(manga)
            Log.d("InventoryService", "Manga recibido: ${manga.titulo}")
        } else {
            Log.d("InventoryService", "No se recibió manga")
        }

        val mangas = arbolBinarioManga.obtenerMangasEnOrden()

        recyclerViewMangas = findViewById(R.id.recyclerViewMangas)
        recyclerViewMangas.layoutManager = LinearLayoutManager(this)

        Log.d("InventoryService", "Número de mangas cargados: ${mangas.size}")
        mangas.forEachIndexed { index, manga ->
            Log.d("InventoryService", "Manga $index: ${manga.titulo}")
        }

        mangaAdapter = MangaAdapter(mangas.toMutableList())
        recyclerViewMangas.adapter = mangaAdapter

        fabRegresarInventario = findViewById(R.id.fabRegresarInventario)
        fabMain = findViewById(R.id.fab)
        additionalButtons = listOf(
            findViewById(R.id.agregarManga),
            findViewById(R.id.modificarManga),
            findViewById(R.id.eliminarManga)
        )

        fabMain.setOnClickListener {
            toggleAdditionalButtons()
        }
        fabMain.setImageResource(fabOpenIcon)
        hideAdditionalButtons()

        additionalButtons[0].setOnClickListener {
            val intent = Intent(this, InventoryAgregarService::class.java)
            startActivity(intent)
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

    private fun showAdditionalButtons() {
        additionalButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.alpha = 0f
            button.animate()
                .translationY(-200f * (index + 1))
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        fabMain.setImageResource(fabCloseIcon)
    }

    private fun toggleAdditionalButtons() {
        val areButtonsVisible = additionalButtons.first().visibility == View.VISIBLE
        if (areButtonsVisible) {
            hideAdditionalButtons()
        } else {
            showAdditionalButtons()
        }
    }

    private fun cargarArbolDesdeArchivo() {
        try {
            val archivo = File(filesDir, "arbol_manga.ser")
            if (archivo.exists()) {
                val fileInputStream = FileInputStream(archivo)
                val objectInputStream = ObjectInputStream(fileInputStream)

                // Deserializamos el objeto y lo asignamos a arbolManga
                val arbolCargado = objectInputStream.readObject() as ArbolBinarioManga
                objectInputStream.close()
                fileInputStream.close()

                arbolBinarioManga.addAll(arbolCargado)
                Log.d("MainActivity", "Árbol cargado exitosamente.")
            } else {
                Log.d("MainActivity", "Archivo de árbol no encontrado.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("MainActivity", "Error al cargar el árbol.")
        }
    }
}

