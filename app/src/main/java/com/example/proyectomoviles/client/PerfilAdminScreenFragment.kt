package com.example.proyectomoviles.client

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
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.example.proyectomoviles.R
import android.content.Context
import android.content.Intent
import com.example.proyectomoviles.models.AdminConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class PerfilAdminScreenFragment : Fragment(R.layout.perfil_activity_admin) {

    private lateinit var tvAlias: TextView
    private lateinit var tvIdAdmin: TextView
    private lateinit var tvNombreAdmin: TextView
    private lateinit var tvCorreoAdmin: TextView
    private lateinit var tvAreaAdmin: TextView
    private lateinit var tvRangoAdmin: TextView
    private lateinit var ivAdminPhoto: ImageView
    private lateinit var btnCerrarSesion: Button
    private lateinit var fabMain: FloatingActionButton
    private lateinit var additionalButtons: List<FloatingActionButton>

    // Íconos para el FAB principal
    private val fabOpenIcon = R.drawable.ic_plus_admin
    private val fabCloseIcon = R.drawable.ic_cerrar_admin

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAlias = view.findViewById(R.id.tvAliasUser)
        tvIdAdmin = view.findViewById(R.id.tvEdadUser)
        tvNombreAdmin = view.findViewById(R.id.tvNombreUser)
        tvCorreoAdmin = view.findViewById(R.id.tvCorreoAdmin)
        tvAreaAdmin = view.findViewById(R.id.tvAreaAdmin)
        tvRangoAdmin = view.findViewById(R.id.tvRangoAdmin)
        ivAdminPhoto = view.findViewById(R.id.ivAdminPhoto)
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion)
        fabMain = view.findViewById(R.id.fab)

        // Inicialización de los botones adicionales
        additionalButtons = listOf(
            view.findViewById(R.id.fab1),
            view.findViewById(R.id.fab2),
            view.findViewById(R.id.fab3),
            view.findViewById(R.id.fab4),
            view.findViewById(R.id.fab5),
            view.findViewById(R.id.fab6)
        )

        fabMain.setImageResource(fabOpenIcon)

        hideAdditionalButtons()

        fabMain.setOnClickListener {
            toggleAdditionalButtons()
        }

        additionalButtons[0].setOnClickListener {
            hideAdditionalButtons()
        }

        loadAdminData()
        loadProfilePhoto()

        ivAdminPhoto.setOnClickListener {
            getImageLauncher.launch("image/*")
        }

        btnCerrarSesion.setOnClickListener {
            onCerrarSesionClick()
        }
    }

    private fun hideAdditionalButtons() {
        additionalButtons.forEach {
            it.animate()
                .translationY(0f)
                .alpha(0f)
                .setDuration(200)
                .withEndAction { it.visibility = View.GONE }
                .start()
        }
        fabMain.setImageResource(fabOpenIcon)
    }

    private fun showAdditionalButtons() {
        additionalButtons.forEachIndexed { index, button ->
            button.visibility = View.VISIBLE
            button.alpha = 0f
            button.animate()
                .translationY(-200f * (index + 1))
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        fabMain.setImageResource(fabCloseIcon)
    }

    private fun toggleAdditionalButtons() {
        val areButtonsVisible = additionalButtons.first().visibility == View.VISIBLE
        if (areButtonsVisible) {
            hideAdditionalButtons()
        } else {
            showAdditionalButtons()
        }
    }

    private fun loadAdminData() {
        tvAlias.text = AdminConstants.ADMIN_ALIAS
        tvIdAdmin.text = "ID: ${AdminConstants.ADMIN_ID}"
        tvNombreAdmin.text = "Nombre: ${AdminConstants.ADMIN_NOMBRE}"
        tvCorreoAdmin.text = "Correo: ${AdminConstants.ADMIN_EMAIL}"
        tvAreaAdmin.text = "Área: ${AdminConstants.ADMIN_AREA}"
        tvRangoAdmin.text = "Rango: ${AdminConstants.ADMIN_ACCESS_LEVEL}"
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
                    ivAdminPhoto.setImageBitmap(circularBitmap)
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
        val file = File(requireContext().filesDir, "profile_image.jpg")
        val outputStream: OutputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return Uri.fromFile(file)
    }

    private fun saveProfilePhoto(imageUri: Uri) {
        val sharedPreferences = requireActivity().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("adminPhotoUri", imageUri.toString())
        editor.apply()
    }

    private fun loadProfilePhoto() {
        val sharedPreferences = requireActivity().getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)
        val photoUriString = sharedPreferences.getString("adminPhotoUri", null)

        if (!photoUriString.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(photoUriString)
                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val circularBitmap = getCircularBitmap(bitmap)
                ivAdminPhoto.setImageBitmap(circularBitmap)
            } catch (e: Exception) {
                ivAdminPhoto.setImageResource(R.drawable.ic_perfil_admin)
            }
        } else {
            ivAdminPhoto.setImageResource(R.drawable.ic_perfil_admin)
        }
    }

    private fun onCerrarSesionClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userType")
        editor.apply()
        val intent = Intent(activity, LoginScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}
