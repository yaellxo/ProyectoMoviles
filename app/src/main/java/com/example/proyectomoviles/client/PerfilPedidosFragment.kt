package com.example.proyectomoviles.client

import PedidosAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.Manga
import com.example.proyectomoviles.models.Usuario
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import org.json.JSONException

class PerfilPedidosFragment : Fragment(R.layout.perfil_activity_pedidos) {

    private lateinit var tvAlias: TextView
    private lateinit var tvNombre: TextView
    private lateinit var btnCerrarSesion: ImageView
    private lateinit var pedidosRecyclerView: RecyclerView
    private lateinit var pedidosAdapter: PedidosAdapter
    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAlias = view.findViewById(R.id.tvAliasUser)
        tvNombre = view.findViewById(R.id.tvNombreUser)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        pedidosRecyclerView = view.findViewById(R.id.pedidosRecyclerView)

        val usuario = arguments?.getSerializable("usuario") as? Usuario
        val carrito = arguments?.getSerializable("carritoMangas") as? ArrayList<Manga>

        usuario?.let {
            Log.d("PerfilFragment", "Usuario: ${it.nombre}, Carrito: ${carrito?.size} items")
        }

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.logout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.custom_dialog_bg)
        )
        dialog.setCancelable(false)

        val btnDialogCancel= dialog.findViewById<FloatingActionButton>(R.id.cancelarDialog)
        val btnDialogLogout = dialog.findViewById<FloatingActionButton>(R.id.cerrarSesionDialog)

        btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDialogLogout.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("userType")
            editor.apply()
            val intent = Intent(activity, LoginScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val storedAlias = arguments?.getString("alias")
        if (storedAlias != null) {
            Log.d("PerfilScreenFragment", "Alias activo recuperado: $storedAlias")
            loadUserData(storedAlias)
        } else {
            Log.e("PerfilScreenFragment", "Alias no encontrado en los argumentos")
        }

        val carritoMangas = arguments?.getSerializable("carritoMangas") as? ArrayList<Manga>
        if (carritoMangas != null && carritoMangas.isNotEmpty()) {
            Log.d("PerfilFragment", "Mangas en el carrito: ${carritoMangas.size} items")
            mostrarMangasEnRecyclerView(carritoMangas)
        } else {
            Log.d("PerfilFragment", "Carrito vacío o no se recuperaron mangas.")
        }


        val alias = arguments?.getString("alias")
        if (alias != null) {
            Log.d("PerfilScreenFragment", "Alias: $alias")
        } else {
            Log.e("PerfilScreenFragment", "Alias no encontrado en los argumentos")
        }

        btnCerrarSesion.setOnClickListener {
            onCerrarSesionClick()
        }
    }

    private fun mostrarMangasEnRecyclerView(mangas: ArrayList<Manga>) {
        pedidosAdapter = PedidosAdapter(
            mMangas = mangas,
            onMangaClick = { manga, userId ->
                Log.d("PerfilScreenFragment", "Manga seleccionado: ${manga.titulo}")
            },
            userId = "userId"
        )
        pedidosRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        pedidosRecyclerView.adapter = pedidosAdapter
    }

    private fun loadUserData(storedAlias: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]") ?: "[]"
        val users = try {
            JSONArray(usersJson)
        } catch (e: JSONException) {
            Log.e("PerfilScreenFragment", "Error al convertir el JSON a JSONArray", e)
            return
        }

        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            val alias = user.getString("alias")

            if (alias.equals(storedAlias, ignoreCase = true)) {
                Log.d("PerfilScreenFragment", "Cargando datos del usuario: $storedAlias")
                tvAlias.text = user.getString("alias")

                val photoUriString = user.optString("photoUri", null)
                if (!photoUriString.isNullOrEmpty()) {
                    val photoUri = Uri.parse(photoUriString)
                    val bitmap = BitmapFactory.decodeFile(photoUri.path)

                    val circularBitmap = getCircularBitmap(bitmap)
                } else {
                }
                break
            }
        }
    }


    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = Math.min(bitmap.width, bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = RectF(0f, 0f, size.toFloat(), size.toFloat())

        paint.isAntiAlias = true
        canvas.drawOval(rect, paint)
        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(squaredBitmap, 0f, 0f, paint)

        return output
    }

    private val getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { userPhotoUri: Uri? ->
        if (userPhotoUri != null) {
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val storedAlias = sharedPreferences.getString("activeUserAlias", null)

            if (storedAlias != null && storedAlias.isNotEmpty()) {
                try {
                    val savedUri = saveImageToInternalStorage(userPhotoUri, storedAlias)

                    val inputStream = requireActivity().contentResolver.openInputStream(userPhotoUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val circularBitmap = getCircularBitmap(bitmap)

                    saveProfilePhoto(savedUri, storedAlias)
                    CustomToast.show(requireContext(),800)
                } catch (e: Exception) {
                    Log.e("ImagePicker", "Error al cargar la imagen", e)
                    CustomToast.show(requireContext(),810)
                }
            } else {
                Log.e("ImagePicker", "Alias activo no encontrado")
                CustomToast.show(requireContext(),820)
            }
        } else {
            CustomToast.show(requireContext(),830)
        }
    }


    private fun saveImageToInternalStorage(userPhotoUri: Uri, storedAlias: String): Uri {
        val inputStream = requireActivity().contentResolver.openInputStream(userPhotoUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val directory = File(requireContext().filesDir, "profile_pics")
        if (!directory.exists()) directory.mkdir()

        Log.d("PerfilScreenFragment", "Directorio para almacenar imágenes: ${directory.absolutePath}")

        val file = File(directory, "$storedAlias.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        Log.d("PerfilScreenFragment", "Imagen guardada en: ${file.absolutePath}")

        return Uri.fromFile(file)
    }

    private fun saveProfilePhoto(userPhotoUri: Uri?, storedAlias: String) {
        if (userPhotoUri == null) {
            Log.e("PerfilScreenFragment", "La URI de la imagen es nula.")
            return
        }

        val imageUri = saveImageToInternalStorage(userPhotoUri, storedAlias)

        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]") ?: "[]"

        Log.d("PerfilScreenFragment", "Usuarios en SharedPreferences antes de procesar: $usersJson")

        val users = try {
            JSONArray(usersJson)
        } catch (e: JSONException) {
            Log.e("PerfilScreenFragment", "Error al convertir el JSON a JSONArray", e)
            return
        }

        var userFound = false
        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            val alias = user.getString("alias")
            Log.d("PerfilScreenFragment", "Procesando usuario con alias: $alias")

            if (alias.equals(storedAlias, ignoreCase = true)) {
                Log.d("PerfilScreenFragment", "Se encontró el usuario con el alias proporcionado: $storedAlias")
                userFound = true

                user.put("photoUri", imageUri.toString())
                Log.d("PerfilScreenFragment", "Foto de perfil guardada para el usuario con alias: $storedAlias")
                break
            }
        }

        if (!userFound) {
            Log.e("PerfilScreenFragment", "No se encontró un usuario con el alias: $storedAlias")
        } else {
            sharedPreferences.edit().putString("users", users.toString()).apply()
            Log.d("PerfilScreenFragment", "Usuarios actualizados en SharedPreferences: ${users.toString()}")
        }
    }

    private fun onCerrarSesionClick() {
        dialog.show()
    }
}