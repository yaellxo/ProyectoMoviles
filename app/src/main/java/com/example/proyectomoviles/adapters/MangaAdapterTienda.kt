package com.example.proyectomoviles.adapters

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import java.io.File

class MangaAdapterTienda(private var mMangas: MutableList<Manga>) :
    RecyclerView.Adapter<MangaAdapterTienda.MangaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_manga_tienda, parent, false)
        return MangaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val manga = mMangas[position]

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.mangaImagenView.setImageBitmap(bitmap)
        } else {
            holder.mangaImagenView.setImageResource(R.drawable.ic_deafaultmanga)
        }

        val tituloText = SpannableString(manga.titulo)
        tituloText.setSpan(StyleSpan(Typeface.BOLD), 0, manga.titulo.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.nombreTextView.text = tituloText

        holder.autorTextView.text = manga.autor
        holder.precioTextView.text = "$${manga.precio}"
    }

    override fun getItemCount(): Int = mMangas.size

    fun actualizarMangas(mangas: List<Manga>) {
        mMangas = mangas.toMutableList()
        notifyDataSetChanged()
    }


    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImagenView: ImageView = itemView.findViewById(R.id.mangaImagenView)
        val autorTextView: TextView = itemView.findViewById(R.id.autorTextView)
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
    }
}