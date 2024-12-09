
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import java.io.File

class MangaAdapterCarrito(
    private val context: Context,
    private val mangas: MutableList<Manga>,
    private val userId: String
) : RecyclerView.Adapter<MangaAdapterCarrito.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTituloManga: TextView = view.findViewById(R.id.tvTituloManga)
        val tvAutor: TextView = view.findViewById(R.id.tvAutor)
        val tvPrecioManga: TextView = view.findViewById(R.id.tvPrecioManga)
        val ivMangaImagen: ImageView = view.findViewById(R.id.ivMangaImagen)
        val tvVolumen: TextView = view.findViewById(R.id.tvVolumen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_manga_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val manga = mangas[position]
        holder.tvTituloManga.text = manga.titulo
        holder.tvAutor.text = manga.autor
        holder.tvVolumen.text = "Volumen: ${manga.volumen}"

        val precioFormateado = String.format("%.2f", manga.precio)
        holder.tvPrecioManga.text = context.getString(R.string.precio_template, precioFormateado)

        val file = File(manga.imagenUrl)
        if (file.exists()) {
            holder.ivMangaImagen.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
        } else {
            holder.ivMangaImagen.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        Log.d("MangaAdapterCarrito", "Mostrando manga: ${manga.titulo}, Autor: ${manga.autor}, Precio: ${precioFormateado}")
        Log.d("MangaAdapterCarrito", "UserId del carrito: $userId")
    }

    override fun getItemCount(): Int = mangas.size
}
