package com.example.proyectomoviles.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.adapters.MangaAdapterTienda
import com.example.proyectomoviles.models.ArbolBinarioManga
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.models.NodoManga
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

class HotScreenFragment : Fragment(R.layout.hot_activity) {

    private lateinit var recyclerViewMangas: RecyclerView
    private lateinit var mangaAdapter: MangaAdapterTienda
    private lateinit var arbolBinarioManga: ArbolBinarioManga

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.hot_activity, container, false)

        recyclerViewMangas = view.findViewById(R.id.recyclerViewMangasTienda)

        recyclerViewMangas.layoutManager = GridLayoutManager(requireContext(), 3)

        cargarArbolDesdeArchivo()

        return view
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

                val listaMangas = mutableListOf<Manga>()
                recorrerArbolEnOrden(arbolBinarioManga.raiz, listaMangas)

                mangaAdapter = MangaAdapterTienda(listaMangas)
                recyclerViewMangas.adapter = mangaAdapter
            } else {
                arbolBinarioManga = ArbolBinarioManga()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            arbolBinarioManga = ArbolBinarioManga()
        }
    }

    private fun recorrerArbolEnOrden(nodo: NodoManga?, listaMangas: MutableList<Manga>) {
        if (nodo != null) {
            recorrerArbolEnOrden(nodo.izquierda, listaMangas)
            listaMangas.add(nodo.manga)
            recorrerArbolEnOrden(nodo.derecha, listaMangas)
        }
    }
}
