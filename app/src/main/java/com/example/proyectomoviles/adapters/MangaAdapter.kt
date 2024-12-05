package com.example.proyectomoviles.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import java.io.File

class MangaAdapter(private val mMangas: MutableList<Manga>) : RecyclerView.Adapter<MangaAdapter.MangaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manga, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val manga = mMangas[position]

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.mangaImagenView.setImageBitmap(bitmap)
        }

        // Mostrar el ID con el prefijo "ID: "
        holder.mangaIdTextView.text = "ID: ${manga.mangaId}"
        holder.stockTextView.text = "Stock: ${manga.stock}"
        holder.nombreTextView.text = manga.titulo
        holder.precioTextView.text = "$${manga.precio}"
    }

    override fun getItemCount(): Int {
        return mMangas.size
    }

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImagenView: ImageView = itemView.findViewById(R.id.mangaImagenView)
        val mangaIdTextView: TextView = itemView.findViewById(R.id.mangaIdTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.stockTextView)
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
    }

    fun agregarManga(manga: Manga) {
        mMangas.add(manga)
        notifyItemInserted(mMangas.size - 1)
    }
}