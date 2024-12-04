package com.example.proyectomoviles.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.*
import com.google.gson.Gson
import com.example.proyectomoviles.models.Manga

class MangaRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MangaData", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveManga(manga: Manga) {
        val editor = sharedPreferences.edit()
        editor.putString(manga.id, gson.toJson(manga))
        editor.apply()
    }

    fun getManga(id: String): Manga? {
        val mangaJson = sharedPreferences.getString(id, null)
        return mangaJson?.let { gson.fromJson(it, Manga::class.java) }
    }

    fun deleteManga(id: String) {
        val editor = sharedPreferences.edit()
        editor.remove(id)
        editor.apply()
    }

    fun getAllMangas(): List<Manga> {
        val allEntries = sharedPreferences.all
        return allEntries.values.mapNotNull {
            gson.fromJson(it.toString(), Manga::class.java)
        }
    }

    fun saveImageToInternalStorage(imageUri: Uri): Uri {
        val bitmap = context.contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        }

        val file = File(context.filesDir, "${UUID.randomUUID()}.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        return Uri.fromFile(file)
    }
}