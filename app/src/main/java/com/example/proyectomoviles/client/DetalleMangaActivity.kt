package com.example.proyectomoviles.client

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.models.Usuario
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class DetalleMangaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_manga)

        // Obtener los datos del manga y usuario
        val manga = intent.getSerializableExtra("manga") as Manga
        val userId = intent.getStringExtra("userId") ?: ""  // Asegúrate de pasar el userId correctamente
        Log.d("DetalleMangaActivity", "User ID recibido: $userId")

        // Referencias a las vistas
        val tituloTextView: TextView = findViewById(R.id.etTitulo)
        val autorTextView: TextView = findViewById(R.id.etAutor)
        val calificacionRatingBar: RatingBar = findViewById(R.id.rbCalificacion)
        val imagenImageView: ImageView = findViewById(R.id.mangaImagenView)
        val precioTextView: TextView = findViewById(R.id.tvPrecio)
        val volumenTextView: TextView = findViewById(R.id.tvVolumen)
        val generoTextView: TextView = findViewById(R.id.tvGenero)
        val editorialTextView: TextView = findViewById(R.id.tvEditorial)
        val publicacionTextView: TextView = findViewById(R.id.tvPublicacion)
        val descripcionTextView: TextView = findViewById(R.id.tvDescripcion)
        val btnCarrito: Button = findViewById(R.id.btnCarrito)

        // Mostrar información del manga en los TextViews
        tituloTextView.text = manga.titulo
        autorTextView.text = manga.autor
        precioTextView.text = "$${manga.precio}"
        volumenTextView.text = "Volumen: ${manga.volumen}"
        generoTextView.text = "Género: ${manga.genero}"
        editorialTextView.text = "Editorial: ${manga.editorial}"
        publicacionTextView.text = "Publicación: ${manga.publicacion}"
        descripcionTextView.text = manga.descripcion

        // Cargar la imagen del manga
        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imagenImageView.setImageBitmap(bitmap)
        } else {
            imagenImageView.setImageResource(R.drawable.ic_deafaultmanga)
        }

        calificacionRatingBar.rating = 4.5f

        // Acción al hacer clic en el botón "Agregar al carrito"
        btnCarrito.setOnClickListener {
            Log.d("DetalleMangaActivity", "Botón 'Agregar al carrito' presionado")

            // Recuperar los usuarios desde SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val usersJson = sharedPreferences.getString("users", "[]")  // Obtener la lista de usuarios desde SharedPreferences
            val usersArray = JSONArray(usersJson)

            var usuario: Usuario? = null
            // Buscar al usuario con el userId recibido
            for (i in 0 until usersArray.length()) {
                val user = usersArray.getJSONObject(i)
                val currentUserId = user.getString("userId")
                if (currentUserId == userId) {
                    usuario = Usuario(
                        userId = user.getString("userId"),
                        alias = user.getString("alias"),
                        nombre = user.getString("nombre"),
                        correo = user.getString("correo"),
                        edad = user.getString("edad"),
                        clave = user.getString("clave"),
                        userPhotoUri = user.getString("userPhotoUri"),
                        carrito = cargarCarrito(user.getJSONArray("carrito")) // Cargar el carrito existente si lo hay
                    )
                    break
                }
            }

            if (usuario != null) {
                // Si se encuentra el usuario, agregar el manga al carrito
                Log.d("DetalleMangaActivity", "Agregando manga al carrito para el usuario ${usuario.nombre}")
                usuario.carrito.add(manga)  // Agregar el manga al carrito del usuario

                // Actualizar la lista de usuarios en SharedPreferences
                val updatedUsersArray = JSONArray()
                for (i in 0 until usersArray.length()) {
                    val user = usersArray.getJSONObject(i)
                    if (user.getString("userId") == usuario.userId) {
                        val updatedUser = JSONObject()
                        updatedUser.put("userId", usuario.userId)
                        updatedUser.put("alias", usuario.alias)
                        updatedUser.put("nombre", usuario.nombre)
                        updatedUser.put("correo", usuario.correo)
                        updatedUser.put("edad", usuario.edad)
                        updatedUser.put("clave", usuario.clave)
                        updatedUser.put("userPhotoUri", usuario.userPhotoUri)
                        updatedUser.put("carrito", JSONArray(usuario.carrito.map { manga ->
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
                            mangaJson
                        }))
                        updatedUsersArray.put(updatedUser)
                    } else {
                        updatedUsersArray.put(user)
                    }
                }

                sharedPreferences.edit().putString("users", updatedUsersArray.toString()).apply()

                // Crear el fragmento y pasar el userId como argumento
                val fragment = CarritoScreenFragment()
                val bundle = Bundle()
                bundle.putString("userId", usuario.userId)  // Usar el userId del usuario encontrado
                fragment.arguments = bundle

                // Mostrar un mensaje de éxito
                Toast.makeText(this, "Manga agregado al carrito", Toast.LENGTH_SHORT).show()
            } else {
                // Si no se encuentra el usuario
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para cargar el carrito del usuario
    fun cargarCarrito(carritoJson: JSONArray?): MutableList<Manga> {
        val carrito = mutableListOf<Manga>()
        if (carritoJson != null && carritoJson.length() > 0) {
            for (i in 0 until carritoJson.length()) {
                try {
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
                } catch (e: JSONException) {
                    Log.e("DetalleMangaActivity", "Error al procesar el carrito: ${e.message}")
                }
            }
        }
        return carrito
    }
}
