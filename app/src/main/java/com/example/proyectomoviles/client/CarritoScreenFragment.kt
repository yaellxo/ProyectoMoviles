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
import org.json.JSONObject

class CarritoScreenFragment : Fragment(R.layout.carrito_activity) {

    var userId: String = "defaultUserId"
    private lateinit var carritoRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar el userId desde SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")  // Obtener la lista de usuarios desde SharedPreferences

        val usersArray = JSONArray(usersJson)  // Crear el JSONArray, aunque esté vacío
        userId = if (usersArray.length() > 0) {
            usersArray.getJSONObject(0).getString("userId")  // Usar el primer elemento si existe
        } else {
            "defaultUserId"  // Valor predeterminado cuando no hay elementos
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

        if (usuario != null) {
            val carrito = usuario.carrito

            // Verificar si el carrito del usuario tiene mangas
            if (carrito.isNotEmpty()) {
                // Filtrar mangas por mangaId o cualquier condición específica
                val mangasFiltrados = carrito.filter { manga ->
                    manga.mangaId.isNotEmpty()
                }.toMutableList()

                if (mangasFiltrados.isNotEmpty()) {
                    // Log para verificar el carrito filtrado
                    Log.d("CarritoScreen", "Carrito cargado con ${mangasFiltrados.size} mangas.")
                    // Actualizar el RecyclerView con los mangas filtrados
                    carritoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    carritoRecyclerView.adapter = MangaAdapterCarrito(
                        context = requireContext(),
                        mangas = mangasFiltrados,
                        userId = userId
                    )
                    // Notificar que los datos del RecyclerView han cambiado
                    carritoRecyclerView.adapter?.notifyDataSetChanged()
                } else {
                    Log.d("CarritoScreen", "No se encontraron mangas válidos en el carrito.")
                }
            } else {
                Log.d("CarritoScreen", "El carrito está vacío.")
            }
        } else {
            Log.d("CarritoScreen", "Usuario no encontrado.")
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

    private fun guardarCarrito(usuario: Usuario) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convertir la lista de usuarios a JSON
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        // Buscar el usuario y actualizar su carrito
        for (i in 0 until usersArray.length()) {
            val userJson = usersArray.getJSONObject(i)
            if (userJson.getString("userId") == usuario.userId) {
                // Actualizar el carrito en el JSON del usuario
                val carritoJson = JSONArray()
                usuario.carrito.forEach { manga ->
                    val mangaJson = JSONObject()
                    mangaJson.put("titulo", manga.titulo)
                    mangaJson.put("precio", manga.precio)
                    mangaJson.put("stock", manga.stock)
                    mangaJson.put("descripcion", manga.descripcion)
                    mangaJson.put("volumen", manga.volumen)
                    mangaJson.put("autor", manga.autor)
                    mangaJson.put("genero", manga.genero)
                    mangaJson.put("editorial", manga.editorial)
                    mangaJson.put("publicacion", manga.publicacion)
                    mangaJson.put("imagenUrl", manga.imagenUrl)
                    mangaJson.put("mangaId", manga.mangaId)
                    carritoJson.put(mangaJson)
                }
                userJson.put("carrito", carritoJson)

                // Guardar el JSON actualizado en SharedPreferences
                usersArray.put(i, userJson)
                break
            }
        }

        editor.putString("users", usersArray.toString())
        editor.apply()

        Log.d("CarritoScreen", "Carrito actualizado en SharedPreferences.")
    }

    fun actualizarVista() {
        val usuario = obtenerUsuarioPorId(userId)
        usuario?.let {
            // Actualizar el RecyclerView con los datos actualizados
            carritoRecyclerView.adapter?.notifyDataSetChanged() // Actualizar el RecyclerView si es necesario
        }
    }

}
