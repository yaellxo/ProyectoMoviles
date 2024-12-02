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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import android.util.Base64
import org.json.JSONException
import java.io.IOException
import java.io.InputStream

class PerfilScreenFragment : Fragment(R.layout.perfil_activity) {

    private lateinit var tvAlias: TextView
    private lateinit var tvEdad: TextView
    private lateinit var tvNombre: TextView
    private lateinit var ivUserPhoto: ImageView
    private lateinit var btnCerrarSesion: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAlias = view.findViewById(R.id.tvAliasUser)
        tvEdad = view.findViewById(R.id.tvEdadUser)
        tvNombre = view.findViewById(R.id.tvNombreUser)
        ivUserPhoto = view.findViewById(R.id.ivUserPhoto)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)

        val storedAlias = arguments?.getString("alias")
        val userPhotoUri = arguments?.getString("userPhotoUri")

        if (storedAlias != null) {
            Log.d("PerfilScreenFragment", "Alias activo recuperado: $storedAlias")
            loadUserData(storedAlias)  // Esto carga los datos y la imagen de perfil.
        } else {
            Log.e("PerfilScreenFragment", "Alias no encontrado en los argumentos")
        }

        ivUserPhoto.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        btnCerrarSesion.setOnClickListener {
            onCerrarSesionClick()
        }
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
                tvEdad.text = "Edad: ${user.getString("edad")}"
                tvNombre.text = "Nombre: ${user.getString("nombre")}"

                // Cargar la imagen de perfil desde la URI almacenada en SharedPreferences
                val photoUriString = user.optString("photoUri", null)
                if (!photoUriString.isNullOrEmpty()) {
                    val photoUri = Uri.parse(photoUriString)
                    val bitmap = BitmapFactory.decodeFile(photoUri.path)

                    // Aplicar la función getCircularBitmap para redondear la imagen
                    val circularBitmap = getCircularBitmap(bitmap)
                    ivUserPhoto.setImageBitmap(circularBitmap)
                } else {
                    ivUserPhoto.setImageResource(R.drawable.ic_perfil_negro)
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

            // Verificamos que el alias no sea nulo antes de continuar
            if (storedAlias != null && storedAlias.isNotEmpty()) {
                try {
                    // Guardar la imagen en el almacenamiento interno
                    val savedUri = saveImageToInternalStorage(userPhotoUri, storedAlias)

                    // Convertir la imagen seleccionada a Bitmap y actualizar el ImageView
                    val inputStream = requireActivity().contentResolver.openInputStream(userPhotoUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val circularBitmap = getCircularBitmap(bitmap)

                    // Actualizar el ImageView en el hilo principal
                    activity?.runOnUiThread {
                        ivUserPhoto.setImageBitmap(circularBitmap)
                    }

                    // Guardar la foto de perfil en el JSON
                    saveProfilePhoto(savedUri, storedAlias)
                    Toast.makeText(requireContext(), "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("ImagePicker", "Error al cargar la imagen", e)
                    Toast.makeText(requireContext(), "Hubo un error al cargar la imagen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("ImagePicker", "Alias activo no encontrado")
                Toast.makeText(requireContext(), "Alias activo no encontrado. No se pudo actualizar la imagen.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveImageToInternalStorage(userPhotoUri: Uri, storedAlias: String): Uri {
        val inputStream = requireActivity().contentResolver.openInputStream(userPhotoUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Crear el directorio para almacenar las imágenes
        val directory = File(requireContext().filesDir, "profile_pics")
        if (!directory.exists()) directory.mkdir()

        Log.d("PerfilScreenFragment", "Directorio para almacenar imágenes: ${directory.absolutePath}")

        // Crear el archivo para guardar la imagen con el alias del usuario
        val file = File(directory, "$storedAlias.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        // Log para ver la ubicación de la imagen guardada
        Log.d("PerfilScreenFragment", "Imagen guardada en: ${file.absolutePath}")

        return Uri.fromFile(file)
    }


    private fun saveProfilePhoto(userPhotoUri: Uri?, storedAlias: String) {
        if (userPhotoUri == null) {
            Log.e("PerfilScreenFragment", "La URI de la imagen es nula.")
            return
        }

        // Guardar la imagen en almacenamiento interno
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

                // Guardar la URI de la imagen en el objeto del usuario (o su nombre)
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


    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val aspectRatio = width.toFloat() / height.toFloat()

        val newWidth: Int
        val newHeight: Int
        if (width > height) {
            newWidth = maxWidth
            newHeight = (newWidth / aspectRatio).toInt()
        } else {
            newHeight = maxHeight
            newWidth = (newHeight * aspectRatio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun onCerrarSesionClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("alias").apply()

        val intent = Intent(requireContext(), LoginScreen::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}


