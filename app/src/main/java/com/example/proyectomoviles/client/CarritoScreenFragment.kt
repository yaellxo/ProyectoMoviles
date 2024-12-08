package com.example.proyectomoviles.client

import MangaAdapterCarrito
import android.content.Context
import android.os.Bundle
import android.util.Log  // Importamos Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.models.Usuario
import org.json.JSONArray

class CarritoScreenFragment : Fragment(R.layout.carrito_activity) {

    var userId: String = "defaultUserId"
    private lateinit var carritoRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar el userId desde SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")  // Obtener la lista de usuarios desde SharedPreferences

        if (!usersJson.isNullOrEmpty()) {
            val usersArray = JSONArray(usersJson)
            // Asignar el userId del primer usuario en el JSON (o el adecuado)
            userId = usersArray.getJSONObject(0).getString("userId")
        } else {
            userId = "default"
            Log.e("CarritoScreen", "No se encontró userId, se asignó un valor predeterminado.")
        }

        Log.d("CarritoScreen", "UserId recuperado: $userId")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.carrito_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carritoRecyclerView = view.findViewById(R.id.carritoRecyclerView)

        // Obtener usuario
        val usuario = obtenerUsuarioPorId(userId)

        if (usuario != null && usuario.carrito.isNotEmpty()) {
            // Log para verificar el carrito cargado
            Log.d("CarritoScreen", "Carrito cargado con ${usuario.carrito.size} elementos.")
            // Actualizar el RecyclerView con los datos del carrito
            carritoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            carritoRecyclerView.adapter = MangaAdapterCarrito(
                context = requireContext(),
                mangas = usuario.carrito,
                userId = userId
            )
        } else {
            Log.d("CarritoScreen", "No se encontraron mangas en el carrito o usuario no existe.")
        }
    }

    private fun obtenerUsuarioPorId(userId: String): Usuario? {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        Log.d("CarritoScreen", "Datos de usuarios en SharedPreferences: $usersJson")

        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val userJson = usersArray.getJSONObject(i)
            if (userJson.getString("userId") == userId) {
                val usuario = Usuario(
                    userId = userJson.getString("userId"),
                    alias = userJson.getString("alias"),
                    nombre = userJson.getString("nombre"),
                    correo = userJson.getString("correo"),
                    edad = userJson.getString("edad"),
                    clave = userJson.getString("clave"),
                    userPhotoUri = userJson.getString("userPhotoUri"),
                    carrito = cargarCarrito(userJson.getJSONArray("carrito"))
                )
                return usuario
            }
        }

        return null
    }

    private fun cargarCarrito(carritoJson: JSONArray): MutableList<Manga> {
        val carrito = mutableListOf<Manga>()
        for (i in 0 until carritoJson.length()) {
            val mangaJson = carritoJson.getJSONObject(i)
            val manga = Manga(
                titulo = mangaJson.getString("titulo"),
                precio = mangaJson.getDouble("precio").toFloat(),
                stock = mangaJson.getInt("stock"),
                descripcion = mangaJson.getString("descripcion"),
                volumen = mangaJson.getDouble("volumen"),
                autor = mangaJson.getString("autor"),
                genero = mangaJson.getString("genero"),
                editorial = mangaJson.getString("editorial"),
                publicacion = mangaJson.getString("publicacion"),
                imagenUrl = mangaJson.getString("imagenUrl"),
                mangaId = mangaJson.getString("mangaId")
            )
            carrito.add(manga)
        }
        return carrito
    }

    fun actualizarVista() {
        val usuario = obtenerUsuarioPorId(userId)
        usuario?.let {
            carritoRecyclerView.adapter?.notifyDataSetChanged() // Actualizar el RecyclerView si es necesario
        }
    }
}
