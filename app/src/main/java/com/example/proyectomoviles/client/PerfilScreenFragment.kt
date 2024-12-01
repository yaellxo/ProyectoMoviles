package com.example.proyectomoviles.client

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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.proyectomoviles.R
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class PerfilScreenFragment : Fragment(R.layout.perfil_activity) {

    private lateinit var tvAlias: TextView
    private lateinit var tvEdad: TextView
    private lateinit var tvNombre: TextView
    private lateinit var ivUserPhoto: ImageView
    private lateinit var btnCerrarSesion: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("PerfilScreenFragment", "onViewCreated: Fragment creado correctamente.")

        tvAlias = view.findViewById(R.id.tvAliasUser)
        tvEdad = view.findViewById(R.id.tvEdadUser)
        tvNombre = view.findViewById(R.id.tvNombreUser)
        ivUserPhoto = view.findViewById(R.id.ivUserPhoto)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)

        val alias = arguments?.getString("alias")
        val userType = arguments?.getString("userType")

        Log.d("PerfilScreenFragment", "Alias recibido: $alias, Tipo de usuario recibido: $userType")

        loadUserData(alias)

        ivUserPhoto.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        btnCerrarSesion.setOnClickListener {
            onCerrarSesionClick()
        }
    }



    private fun loadUserData(alias: String?) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]")
        val users = JSONArray(usersJson)

        if (alias != null) {
            Log.d("PerfilScreenFragment", "Buscando usuario con alias: $alias")
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("alias") == alias) {
                    // Mostrar datos
                    tvAlias.text = user.getString("alias")
                    tvEdad.text = "Edad: ${user.getString("edad")}"
                    tvNombre.text = "Nombre: ${user.getString("nombre")}"

                    val photoUriString = user.optString("photoUri", "")
                    if (photoUriString.isNotEmpty()) {
                        loadProfilePhoto(Uri.parse(photoUriString))
                    } else {
                        ivUserPhoto.setImageResource(R.drawable.ic_perfil_negro) // Imagen predeterminada si no tiene foto
                    }
                    break
                }
            }
        } else {
            Log.d("PerfilScreenFragment", "Alias no encontrado en los argumentos.")
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

    private val getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            try {
                val savedUri = saveImageToInternalStorage(uri)
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val circularBitmap = getCircularBitmap(bitmap)

                activity?.runOnUiThread {
                    ivUserPhoto.setImageBitmap(circularBitmap)
                }
                saveProfilePhoto(savedUri)
                Toast.makeText(requireContext(), "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ImagePicker", "Error al cargar la imagen", e)
            }
        } else {
            Toast.makeText(requireContext(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri): Uri {
        val inputStream: InputStream? = requireActivity().contentResolver.openInputStream(imageUri)
        val file = File(requireContext().filesDir, "profile_image_user.jpg")
        val outputStream: OutputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return Uri.fromFile(file)
    }

    private fun saveProfilePhoto(imageUri: Uri) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val usersJson = sharedPreferences.getString("users", "[]") ?: "[]"
        val users = JSONArray(usersJson)

        val activeAlias = sharedPreferences.getString("activeUserAlias", null)
        if (activeAlias != null) {
            for (i in 0 until users.length()) {
                val user = users.getJSONObject(i)
                if (user.getString("alias") == activeAlias) {
                    user.put("photoUri", imageUri.toString()) // Asegúrate de que la URI se guarda correctamente
                    break
                }
            }
            sharedPreferences.edit().putString("users", users.toString()).apply()
        }
    }

    private fun loadProfilePhoto(uri: Uri?) {
        if (uri != null) {
            try {
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val circularBitmap = getCircularBitmap(bitmap)
                ivUserPhoto.setImageBitmap(circularBitmap)
            } catch (e: Exception) {
                Log.e("PerfilScreenFragment", "Error al cargar la imagen redonda", e)
                ivUserPhoto.setImageResource(R.drawable.ic_perfil_user)
            }
        } else {
            ivUserPhoto.setImageResource(R.drawable.ic_perfil_user)
        }
    }

    private fun onCerrarSesionClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userType")
        editor.apply()
        val intent = Intent(requireActivity(), LoginScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        requireActivity().finish()
    }
}