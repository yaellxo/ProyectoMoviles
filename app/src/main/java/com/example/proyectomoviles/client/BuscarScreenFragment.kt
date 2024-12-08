package com.example.proyectomoviles.client

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.MangaAdapterTienda
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

class BuscarScreenFragment : Fragment(R.layout.buscar_activity) {

    private lateinit var mangaAdapter: MangaAdapterTienda
    private lateinit var arbolBinarioManga: ArbolBinarioManga
    private lateinit var mangas: MutableList<Manga>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMangas)
        val et_search = view.findViewById<EditText>(R.id.et_search)

        mangas = mutableListOf()

        mangaAdapter = MangaAdapterTienda(mangas) { manga ->
            val intent = Intent(requireContext(), DetalleMangaActivity::class.java)
            intent.putExtra("manga", manga)
            startActivity(intent)
        }
        recyclerView.adapter = mangaAdapter


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        recyclerView.visibility = View.GONE

        cargarArbolDesdeArchivo()
        val mangas = arbolBinarioManga.obtenerMangasEnOrden()

        mangaAdapter.actualizarMangas(mangas.toMutableList())

        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val query = charSequence.toString().trim()

                if (query.isNotEmpty()) {
                    ocultarBotonesGenero(view)

                    val mangasFiltrados = arbolBinarioManga.obtenerMangasEnOrden().filter { manga ->
                        manga.titulo.contains(query, ignoreCase = true) ||
                                manga.precio.toString().contains(query) ||
                                manga.volumen.toString().contains(query) ||
                                manga.autor.contains(query, ignoreCase = true) ||
                                manga.genero.contains(query, ignoreCase = true) ||
                                manga.editorial.contains(query, ignoreCase = true) ||
                                manga.mangaId.contains(query, ignoreCase = true)
                    }

                    if (mangasFiltrados.isEmpty()) {
                        Toast.makeText(requireContext(), "No se encontraron mangas", Toast.LENGTH_SHORT).show()
                    } else {
                        recyclerView.visibility = View.VISIBLE
                    }

                    mangaAdapter.actualizarMangas(mangasFiltrados)
                    mangaAdapter.notifyDataSetChanged()
                } else {
                    mostrarBotonesGenero(view)

                    val mangasRestantes = arbolBinarioManga.obtenerMangasEnOrden()
                    mangaAdapter.actualizarMangas(mangasRestantes)
                    mangaAdapter.notifyDataSetChanged()

                    recyclerView.visibility = View.GONE
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        view.findViewById<ImageButton>(R.id.ibshonen).setOnClickListener {
            aplicarFiltroPorGenero("Shonen")
        }
        view.findViewById<ImageButton>(R.id.ibshoujo).setOnClickListener {
            aplicarFiltroPorGenero("Shojo")
        }
        view.findViewById<ImageButton>(R.id.ibseinen).setOnClickListener {
            aplicarFiltroPorGenero("Seinen")
        }
        view.findViewById<ImageButton>(R.id.ibjosei).setOnClickListener {
            aplicarFiltroPorGenero("Josei")
        }
        view.findViewById<ImageButton>(R.id.ibmecha).setOnClickListener {
            aplicarFiltroPorGenero("Mecha")
        }
        view.findViewById<ImageButton>(R.id.ibhorror).setOnClickListener {
            aplicarFiltroPorGenero("Horror")
        }
        view.findViewById<ImageButton>(R.id.ibspokon).setOnClickListener {
            aplicarFiltroPorGenero("Spokon")
        }
        view.findViewById<ImageButton>(R.id.ibslice).setOnClickListener {
            aplicarFiltroPorGenero("Slice of Life")
        }
        view.findViewById<ImageButton>(R.id.ibisekai).setOnClickListener {
            aplicarFiltroPorGenero("Isekai")
        }
        view.findViewById<ImageButton>(R.id.ibfantasy).setOnClickListener {
            aplicarFiltroPorGenero("Fantasy")
        }
        view.findViewById<ImageButton>(R.id.ibcomedy).setOnClickListener {
            aplicarFiltroPorGenero("Comedy")
        }
        view.findViewById<ImageButton>(R.id.ibharem).setOnClickListener {
            aplicarFiltroPorGenero("Harem")
        }

    }

    private fun cargarArbolDesdeArchivo() {
        try {
            val archivo = File(requireContext().filesDir, "arbol_manga.ser")
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

    private fun ocultarBotonesGenero(view: View) {
        view.findViewById<ImageButton>(R.id.ibshonen).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibshoujo).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibseinen).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibjosei).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibmecha).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibhorror).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibspokon).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibslice).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibisekai).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibfantasy).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibcomedy).visibility = View.GONE
        view.findViewById<ImageButton>(R.id.ibharem).visibility = View.GONE
    }

    private fun mostrarBotonesGenero(view: View) {
        view.findViewById<ImageButton>(R.id.ibshonen).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibshoujo).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibseinen).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibjosei).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibmecha).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibhorror).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibspokon).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibslice).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibisekai).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibfantasy).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibcomedy).visibility = View.VISIBLE
        view.findViewById<ImageButton>(R.id.ibharem).visibility = View.VISIBLE
    }

    private fun aplicarFiltroPorGenero(genero: String) {
        val mangasFiltrados = arbolBinarioManga.obtenerMangasEnOrden().filter { manga ->
            manga.genero.contains(genero, ignoreCase = true)
        }

        mangaAdapter.actualizarMangas(mangasFiltrados)
        mangaAdapter.notifyDataSetChanged()

        if (mangasFiltrados.isEmpty()) {
            Toast.makeText(requireContext(), "No se encontraron mangas de género $genero", Toast.LENGTH_SHORT).show()
        } else {
            val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerViewMangas)
            recyclerView.visibility = View.VISIBLE

            ocultarBotonesGenero(requireView())
        }
    }
}
