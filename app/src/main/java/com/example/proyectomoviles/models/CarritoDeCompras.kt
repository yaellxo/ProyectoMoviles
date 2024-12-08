package com.example.proyectomoviles.models

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class CarritoDeCompras(private val context: Context, private val userId: String) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    fun agregarAlCarrito(usuario: Usuario, manga: Manga) {
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val userJson = usersArray.getJSONObject(i)
            if (userJson.getString("userId") == usuario.userId) {
                val carritoArray = userJson.optJSONArray("carrito") ?: JSONArray()

                val mangaJson = JSONObject().apply {
                    put("titulo", manga.titulo)
                    put("autor", manga.autor)
                    put("precio", manga.precio)
                    put("imagenUrl", manga.imagenUrl)
                    put("mangaId", manga.mangaId)
                    put("volumen", manga.volumen)
                    put("genero", manga.genero)
                    put("editorial", manga.editorial)
                    put("publicacion", manga.publicacion)
                    put("descripcion", manga.descripcion)
                    put("stock", manga.stock)
                }

                carritoArray.put(mangaJson)
                userJson.put("carrito", carritoArray)

                guardarUsuarioEnSharedPreferences(userJson)
                break
            }
        }

        sharedPreferences.edit()
            .putString("users", usersArray.toString())
            .apply()
    }

    private fun guardarUsuarioEnSharedPreferences(userJson: JSONObject) {
        val editor = sharedPreferences.edit()
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val user = usersArray.getJSONObject(i)
            if (user.getString("userId") == userJson.getString("userId")) {
                usersArray.put(i, userJson)
                break
            }
        }

        editor.putString("users", usersArray.toString())
        editor.apply()
    }

    fun obtenerCarrito(): List<Manga> {
        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)
        val carrito = mutableListOf<Manga>()

        for (i in 0 until usersArray.length()) {
            val userJson = usersArray.getJSONObject(i)
            if (userJson.getString("userId") == userId) {
                val carritoArray = userJson.optJSONArray("carrito") ?: JSONArray()
                for (j in 0 until carritoArray.length()) {
                    val mangaJson = carritoArray.getJSONObject(j)
                    carrito.add(Manga(
                        titulo = mangaJson.getString("titulo"),
                        autor = mangaJson.getString("autor"),
                        precio = mangaJson.getDouble("precio").toFloat(),
                        imagenUrl = mangaJson.getString("imagenUrl"),
                        mangaId = mangaJson.getString("mangaId"),
                        volumen = mangaJson.getDouble("volumen"),
                        genero = mangaJson.getString("genero"),
                        editorial = mangaJson.getString("editorial"),
                        publicacion = mangaJson.getString("publicacion"),
                        descripcion = mangaJson.getString("descripcion"),
                        stock = mangaJson.getInt("stock")
                    ))
                }
                break
            }
        }

        return carrito
    }
}
