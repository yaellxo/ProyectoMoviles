package com.example.proyectomoviles.client

import android.text.SpannableString
import android.text.style.StyleSpan
import android.graphics.Typeface
import MangaAdapterCarrito
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
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

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", "defaultUserId") ?: "defaultUserId"

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

        val btnPaypal: Button = view.findViewById(R.id.btnPaypal)
        btnPaypal.setOnClickListener {
            procesarCompra()
        }

        carritoRecyclerView = view.findViewById(R.id.carritoRecyclerView)
        val etResumenCompraSumatoria: TextView = view.findViewById(R.id.etResumenCompraSumatoria)

        val usuario = obtenerUsuarioPorId(userId)

        if (usuario != null) {
            val carrito = usuario.carrito

            if (carrito.isNotEmpty()) {
                val mangasFiltrados = carrito.filter { manga ->
                    manga.mangaId.isNotEmpty()
                }.toMutableList()

                if (mangasFiltrados.isNotEmpty()) {
                    val nombresMangas = mangasFiltrados.joinToString("\n") { it.titulo }
                    val totalCompra = mangasFiltrados.sumOf { it.precio.toDouble() }.toFloat()
                    val resumen = "Mangas en tu carrito:\n$nombresMangas\n\nTotal: \$${totalCompra}"

                    val spannable = SpannableString(resumen)
                    val totalStart = resumen.indexOf("Total:")
                    val totalEnd = totalStart + "Total:".length
                    spannable.setSpan(StyleSpan(Typeface.BOLD), totalStart, totalEnd, 0)

                    etResumenCompraSumatoria.text = spannable

                    carritoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = MangaAdapterCarrito(
                        context = requireContext(),
                        mangas = mangasFiltrados,
                        userId = userId
                    )
                    carritoRecyclerView.adapter = adapter

                    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
                        override fun getMovementFlags(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                        ): Int {
                            val swipeFlags = ItemTouchHelper.RIGHT
                            return makeMovementFlags(0, swipeFlags)
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val position = viewHolder.adapterPosition
                            val mangaEliminado = mangasFiltrados[position]
                            mangasFiltrados.removeAt(position)
                            adapter.notifyItemRemoved(position)

                            val usuarioActualizado = obtenerUsuarioPorId(userId)
                            usuarioActualizado?.carrito = mangasFiltrados
                            guardarCarrito(usuarioActualizado!!)

                            actualizarResumenCompra(mangasFiltrados, etResumenCompraSumatoria)

                            val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                vibrator.vibrate(50)
                            }

                            Log.d("CarritoScreen", "Manga eliminado: ${mangaEliminado.titulo}")
                        }

                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false
                        }

                        override fun isItemViewSwipeEnabled(): Boolean {
                            return true
                        }
                    })

                    itemTouchHelper.attachToRecyclerView(carritoRecyclerView)

                    adapter.notifyDataSetChanged()
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

    private fun actualizarResumenCompra(mangasFiltrados: List<Manga>, etResumenCompraSumatoria: TextView) {
        val nombresMangas = mangasFiltrados.joinToString("\n") { it.titulo }
        val totalCompra = mangasFiltrados.sumOf { it.precio.toDouble() }.toFloat()

        val resumen = "Mangas en tu carrito:\n$nombresMangas\n\nTotal: \$${totalCompra}"

        val spannable = SpannableString(resumen)

        val totalStart = resumen.indexOf("Total:")
        val totalEnd = totalStart + "Total:".length

        spannable.setSpan(StyleSpan(Typeface.BOLD), totalStart, totalEnd, 0)

        etResumenCompraSumatoria.text = spannable
    }

    private fun procesarCompra() {
        val usuario = obtenerUsuarioPorId(userId)

        if (usuario != null) {
            val carrito = usuario.carrito

            if (carrito.isNotEmpty()) {
                val animatorList = mutableListOf<Animator>()

                val bundle = Bundle().apply {
                    putSerializable("carritoMangas", ArrayList(carrito))
                    putSerializable("usuario", usuario)
                    putString("alias", usuario?.alias)
                    Log.d("Carrito", "Carrito antes de enviar: ${carrito.size} items")
                }

                for (i in carrito.indices) {
                    val itemView = carritoRecyclerView.findViewHolderForAdapterPosition(i)?.itemView

                    itemView?.let {
                        val desplazamiento = it.width.toFloat() * 2
                        val animator = ObjectAnimator.ofFloat(it, "translationX", desplazamiento)
                        animator.duration = 500
                        animatorList.add(animator)
                    }
                }

                if (animatorList.isNotEmpty()) {
                    val animatorSet = AnimatorSet()
                    animatorSet.playTogether(animatorList)

                    animatorSet.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            carrito.clear()
                            carritoRecyclerView.adapter?.notifyDataSetChanged()

                            usuario.carrito = carrito
                            guardarCarrito(usuario)

                            actualizarResumenCompra(carrito, requireView().findViewById(R.id.etResumenCompraSumatoria))

                            val navController = findNavController()
                            navController.popBackStack(R.id.perfil_pedido, false)
                            navController.navigate(R.id.perfil_pedido, bundle)
                        }
                    })

                    animatorSet.start()
                }
            } else {
                Log.d("CarritoScreen", "El carrito está vacío después de la compra.")
                val etResumenCompraSumatoria: TextView = requireView().findViewById(R.id.etResumenCompraSumatoria)
                etResumenCompraSumatoria.text = "El carrito está vacío."
            }
        } else {
            Log.d("CarritoScreen", "No se encontró al usuario para limpiar el carrito.")
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
                mangaId = mangaJson.getString("mangaId"),
            )
            carrito.add(manga)
        }
        return carrito
    }

    private fun guardarCarrito(usuario: Usuario) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val usersJson = sharedPreferences.getString("users", "[]")
        val usersArray = JSONArray(usersJson)

        for (i in 0 until usersArray.length()) {
            val userJson = usersArray.getJSONObject(i)
            if (userJson.getString("userId") == usuario.userId) {
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

                usersArray.put(i, userJson)
                break
            }
        }

        editor.putString("users", usersArray.toString())
        editor.apply()

        Log.d("CarritoScreen", "Carrito actualizado en SharedPreferences.")
    }

}
