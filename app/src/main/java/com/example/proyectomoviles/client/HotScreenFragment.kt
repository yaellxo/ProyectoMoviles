package com.example.proyectomoviles.client

import MangaAdapterTienda
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
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

    private var userId: String? = null
    private var nombre: String? = null
    private var correo: String? = null
    private var edad: String? = null
    private var userType: String? = null
    private var photoBase64: String? = null
    private var area: String? = null
    private var nivelAcceso: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.hot_activity, container, false)

        // Recuperar los datos pasados por el Bundle
        val bundle = arguments
        if (bundle != null) {
            userId = bundle.getString("userId")
            nombre = bundle.getString("nombre")
            correo = bundle.getString("correo")
            edad = bundle.getString("edad")
            userType = bundle.getString("userType")
            photoBase64 = bundle.getString("photoBase64")
            area = bundle.getString("area")
            nivelAcceso = bundle.getString("nivelAcceso")
        }

        // Log para verificar los datos recibidos
        Log.d("HotScreenFragment", "User ID: $userId, Nombre: $nombre, Correo: $correo, Edad: $edad, Tipo de Usuario: $userType")

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
                Log.d("HotScreenFragment", "userId: $userId")
                // Al crear el adaptador, pasamos el userId
                mangaAdapter = MangaAdapterTienda(listaMangas, { manga, userId ->
                    val intent = Intent(requireContext(), DetalleMangaActivity::class.java)
                    intent.putExtra("manga", manga)
                    intent.putExtra("userId", userId)  // Pasamos el userId
                    startActivity(intent)
                }, userId ?: "", nombre, correo, edad, userType, photoBase64, area, nivelAcceso)

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
