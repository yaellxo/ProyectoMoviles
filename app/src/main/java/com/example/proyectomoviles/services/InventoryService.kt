package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.MangaAdapter
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class InventoryService : AppCompatActivity() {

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

        val et_search: EditText = findViewById(R.id.et_search)
        recyclerViewMangas = findViewById(R.id.recyclerViewMangas)
        fabMain = findViewById(R.id.fab)
        additionalButtons = listOf(
            findViewById(R.id.agregarManga),
            findViewById(R.id.modificarManga),
            findViewById(R.id.eliminarManga)
        )

        cargarArbolDesdeArchivo()

        var mangas = arbolBinarioManga.obtenerMangasEnOrden()
        Log.d("InventoryService", "Número de mangas cargados: ${mangas.size}")
        mangas.forEachIndexed { index, manga ->
            Log.d("InventoryService", "Manga $index: ${manga.titulo}")
        }

        mangaAdapter = MangaAdapter(mangas.toMutableList())

        recyclerViewMangas.adapter = mangaAdapter
        recyclerViewMangas.layoutManager = LinearLayoutManager(this)

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

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val query = charSequence.toString().trim()

                if (query.isNotEmpty()) {
                    val mangasFiltrados = arbolBinarioManga.obtenerMangasEnOrden().filter { manga ->
                        manga.titulo.contains(query, ignoreCase = true) ||
                                manga.precio.toString().contains(query) ||
                                manga.volumen.toString().contains(query) ||
                                manga.autor.contains(query, ignoreCase = true) ||
                                manga.genero.contains(query, ignoreCase = true) ||
                                manga.editorial.contains(query, ignoreCase = true) ||
                                manga.mangaId.contains(query, ignoreCase = true)
                    }

                    mangaAdapter.actualizarMangas(mangasFiltrados)
                    mangaAdapter.notifyDataSetChanged()

                    if (mangasFiltrados.isEmpty()) {
                    }
                } else {
                    val mangas = arbolBinarioManga.obtenerMangasEnOrden()
                    mangaAdapter.actualizarMangas(mangas)
                    mangaAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
            }
        })
    }

    override fun onResume() {
        super.onResume()
        cargarArbolDesdeArchivo()

        val mangasActualizados = arbolBinarioManga.obtenerMangasEnOrden()

        mangaAdapter.actualizarMangas(mangasActualizados)
        mangaAdapter.notifyDataSetChanged()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ELIMINAR_MANGA && resultCode == RESULT_OK) {
            val mangaIdEliminado = data?.getStringExtra("manga_eliminado")
            mangaIdEliminado?.let {
                arbolBinarioManga.eliminarManga(mangaIdEliminado)

                val mangasRestantes = arbolBinarioManga.obtenerMangasEnOrden()

                mangaAdapter.actualizarMangas(mangasRestantes)

                mangaAdapter.notifyDataSetChanged()

                CustomToast.show(this,740)
            }
        }

        if (requestCode == REQUEST_ADD_MANGA && resultCode == RESULT_OK) {
            val nuevoManga = data?.getSerializableExtra("nuevo_manga") as Manga
            arbolBinarioManga.agregarManga(nuevoManga)
            mangaAdapter.agregarManga(nuevoManga)

            guardarArbolEnArchivo()
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
            } else {
                arbolBinarioManga = ArbolBinarioManga()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
            Log.d("InventoryService", "Árbol guardado exitosamente.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val REQUEST_ADD_MANGA = 1002
        const val REQUEST_ELIMINAR_MANGA = 1003
    }
}
