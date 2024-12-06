package com.example.proyectomoviles.client.event_screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.EventosManager
import com.example.proyectomoviles.models.EventosManager.eventosList
import com.example.proyectomoviles.services.EventService
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ModificarEventoScreen : AppCompatActivity() {

    private lateinit var fabRegresarEvento: FloatingActionButton
    private lateinit var btnModificarImagen: ImageButton
    private var imagenUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_evento_activity)

        btnModificarImagen = findViewById(R.id.btnAgregarImagenModificarEvento)
        val btnModificar: FloatingActionButton = findViewById(R.id.fabEventoModificar)
        val idField = findViewById<EditText>(R.id.etIdEventoModificar)

        val nombreField = findViewById<EditText>(R.id.etNombreEventoModificar)
        val fechaField = findViewById<EditText>(R.id.etFechaEventoModificar)
        val ubicacionField = findViewById<EditText>(R.id.etUbicacionEventoModificar)
        val descripcionField = findViewById<EditText>(R.id.etDescripcionEventoModificar)

        fabRegresarEvento = findViewById(R.id.fabRegresarEventoModificar)

        btnModificar.setOnClickListener{
            val imagenUri = imagenUri?.toString()
            val id = idField.text.toString()
            val nombre = nombreField.text.toString()
            val fecha = fechaField.text.toString()
            val ubicacion = ubicacionField.text.toString()
            val descripcion = descripcionField.text.toString()

            if(id.isNotEmpty() && !id.isNotNumber()){
                if(id.toInt() < eventosList.size && id.toInt() >= 0){
                    if (nombre.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty() || imagenUri == null) {
                        CustomToast.show(this, 600)
                    } else {
                        EventosManager.modificarEvento(id.toInt(), nombre, fecha, ubicacion, descripcion, imagenUri)
                        CustomToast.show(this, R.id.fabEventoModificar)
                        val intent = Intent(this, EventService::class.java)
                        startActivity(intent)
                    }
                } else {
                    CustomToast.show(this, 500)
                }
            } else {
                CustomToast.show(this, 500)
            }
        }

        btnModificarImagen.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, AgregarEventoScreen.PICK_IMAGE_REQUEST)
        }

        fabRegresarEvento.setOnClickListener{
            finish()
        }
    }

    fun String.isNotNumber(): Boolean {
        return this.toIntOrNull() == null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AgregarEventoScreen.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                imagenUri = uri
                Glide.with(this)
                    .load(imagenUri)
                    .into(btnModificarImagen)
            }
        }
    }
}