package com.example.proyectomoviles.client

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
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
import com.example.proyectomoviles.utils.CustomToast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class DetalleMangaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_manga)

        val manga = intent.getSerializableExtra("manga") as Manga
        val userId = intent.getStringExtra("userId") ?: ""
        Log.d("DetalleMangaActivity", "User ID recibido: $userId")

        val calificacionRatingBar: RatingBar = findViewById(R.id.rbCalificacion)

        val currentRating = obtenerCalificacion(manga.mangaId, userId)
        calificacionRatingBar.rating = currentRating

        val promedioCalificacion = calcularPromedioCalificacion(manga.mangaId)
        calificacionRatingBar.rating = promedioCalificacion

        calificacionRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                guardarCalificacionEnPreferencias(userId, manga.mangaId, rating)

                Toast.makeText(this, "Calificación guardada: $rating", Toast.LENGTH_SHORT).show()
            }
        }

        val tituloTextView: TextView = findViewById(R.id.etTitulo)
        val autorTextView: TextView = findViewById(R.id.etAutor)
        val imagenImageView: ImageView = findViewById(R.id.mangaImagenView)
        val precioTextView: TextView = findViewById(R.id.tvPrecio)
        val volumenTextView: TextView = findViewById(R.id.tvVolumen)
        val generoTextView: TextView = findViewById(R.id.tvGenero)
        val editorialTextView: TextView = findViewById(R.id.tvEditorial)
        val publicacionTextView: TextView = findViewById(R.id.tvPublicacion)
        val descripcionTextView: TextView = findViewById(R.id.tvDescripcion)
        val btnCarrito: Button = findViewById(R.id.btnCarrito)

        tituloTextView.text = manga.titulo
        autorTextView.text = manga.autor
        precioTextView.text = "$${manga.precio}"
        val volumenText = "Volumen: ${manga.volumen}"
        val volumenSpannable = SpannableString(volumenText)
        volumenSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, volumenText.indexOf(":") + 1, 0)
        volumenTextView.text = volumenSpannable

        val generoText = "Género: ${manga.genero}"
        val generoSpannable = SpannableString(generoText)
        generoSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, generoText.indexOf(":") + 1, 0)
        generoTextView.text = generoSpannable

        val editorialText = "Editorial: ${manga.editorial}"
        val editorialSpannable = SpannableString(editorialText)
        editorialSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, editorialText.indexOf(":") + 1, 0)
        editorialTextView.text = editorialSpannable

        val publicacionText = "Publicación: ${manga.publicacion}"
        val publicacionSpannable = SpannableString(publicacionText)
        publicacionSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, publicacionText.indexOf(":") + 1, 0)
        publicacionTextView.text = publicacionSpannable

        val descripcionText = "Descripción:\n${manga.descripcion}"
        val descripcionSpannable = SpannableString(descripcionText)
        descripcionSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, descripcionText.indexOf(":") + 1, 0)
        descripcionTextView.text = descripcionSpannable

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imagenImageView.setImageBitmap(bitmap)
        } else {
            imagenImageView.setImageResource(R.drawable.ic_deafaultmanga)
        }

        btnCarrito.setOnClickListener {
            Log.d("DetalleMangaActivity", "Botón 'Agregar al carrito' presionado")

            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val usersJson = sharedPreferences.getString("users", "[]")
            val usersArray = JSONArray(usersJson)

            var usuario: Usuario? = null
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
                        carrito = cargarCarrito(user.getJSONArray("carrito"))
                    )
                    break
                }
            }

            if (usuario != null) {
                Log.d("DetalleMangaActivity", "Agregando manga al carrito para el usuario ${usuario.nombre}")
                usuario.carrito.add(manga)

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

                val fragment = CarritoScreenFragment()
                val bundle = Bundle()
                bundle.putString("userId", usuario.userId)
                fragment.arguments = bundle

                CustomToast.show(this, R.id.btnCarrito)
            } else {
                Toast.makeText(this, "Función no disponible.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerCalificacion(mangaId: String, userId: String): Float {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val calificacionesJson = sharedPreferences.getString("calificaciones", "{}")
        val calificaciones = JSONObject(calificacionesJson)
        return if (calificaciones.has(mangaId) && calificaciones.getJSONObject(mangaId).has(userId)) {
            calificaciones.getJSONObject(mangaId).getDouble(userId).toFloat()
        } else {
            0f
        }
    }

    fun guardarCalificacionEnPreferencias(userId: String, mangaId: String, calificacion: Float) {
        val sharedPreferences = getSharedPreferences("MangaPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val clave = "$userId-$mangaId"

        editor.putFloat(clave, calificacion)
        editor.apply()
    }

    fun calcularPromedioCalificacion(mangaId: String): Float {
        val sharedPreferences = getSharedPreferences("MangaPrefs", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all

        var totalCalificaciones = 0f
        var numUsuarios = 0

        for (entry in allEntries) {
            val clave = entry.key
            if (clave.endsWith("-$mangaId")) {
                val calificacion = sharedPreferences.getFloat(clave, 0.0f)
                totalCalificaciones += calificacion
                numUsuarios++
            }
        }

        return if (numUsuarios > 0) totalCalificaciones / numUsuarios else 0.0f
    }

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
                        mangaId = mangaJson.getString("mangaId"),
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
