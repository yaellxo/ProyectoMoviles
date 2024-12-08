import android.animation.ObjectAnimator
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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

        Log.d("MangaAdapter", "Cargando manga en posición $position: ${manga.titulo}")

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.mangaImagenView.setImageBitmap(bitmap)
            Log.d("MangaAdapter", "Imagen cargada desde la ruta: ${manga.imagenUrl}")
        } else {
            holder.mangaImagenView.setImageResource(R.drawable.ic_deafaultmanga)
            Log.d("MangaAdapter", "Imagen no encontrada, utilizando imagen por defecto.")
        }

        val idText = SpannableString("ID: ${manga.mangaId}")
        idText.setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.mangaIdTextView.text = idText

        val stockText = SpannableString("Stock: ${manga.stock}")
        stockText.setSpan(StyleSpan(Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.stockTextView.text = stockText

        holder.nombreTextView.text = manga.titulo
        holder.precioTextView.text = "$${manga.precio}"

        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startVibrationAnimation(holder.itemView)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        Log.d("MangaAdapter", "Número de mangas en el adapter: ${mMangas.size}")
        return mMangas.size
    }

    class MangaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mangaImagenView: ImageView = itemView.findViewById(R.id.mangaImagenView)
        val mangaIdTextView: TextView = itemView.findViewById(R.id.mangaIdTextView)
        val stockTextView: TextView = itemView.findViewById(R.id.stockTextView)
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        val precioTextView: TextView = itemView.findViewById(R.id.precioTextView)
    }

    private fun startVibrationAnimation(view: View) {
        val shake = ObjectAnimator.ofFloat(view, "translationX", -10f, 10f)
        shake.duration = 200
        shake.repeatCount = 5
        shake.repeatMode = ObjectAnimator.REVERSE
        shake.start()
    }

    fun agregarManga(manga: Manga) {
        mMangas.add(manga)
        notifyItemInserted(mMangas.size - 1)
    }

    fun actualizarMangas(mangasActualizadas: List<Manga>) {
        mMangas.clear()
        mMangas.addAll(mangasActualizadas)
    }
}
