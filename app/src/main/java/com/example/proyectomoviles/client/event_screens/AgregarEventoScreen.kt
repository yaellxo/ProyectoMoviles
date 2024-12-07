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
import com.example.proyectomoviles.models.Evento
import com.example.proyectomoviles.models.EventosManager
import com.example.proyectomoviles.services.EventService
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgregarEventoScreen : AppCompatActivity() {

    private lateinit var fabRegresarEvento: FloatingActionButton
    private lateinit var btnAgregarImagen: ImageButton
    private var imagenUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_evento_activity)

        val nombreField = findViewById<EditText>(R.id.etNombreEventoRegistro)
        val fechaField = findViewById<EditText>(R.id.etFechaEventoRegistro)
        val ubicacionField = findViewById<EditText>(R.id.etUbicacionEventoRegistro)
        val descripcionField = findViewById<EditText>(R.id.etDescripcionEventoRegistro)

        fabRegresarEvento = findViewById(R.id.fabRegresarEventoAgregar)
        val btnRegistrar: FloatingActionButton = findViewById(R.id.fabEventoAgregar)
        btnAgregarImagen = findViewById(R.id.btnAgregarImagenRegistrarEvento)

        btnRegistrar.setOnClickListener{
            val imagenUriString = imagenUri.toString()
            val nombre = nombreField.text.toString()
            val fecha = fechaField.text.toString()
            val ubicacion = ubicacionField.text.toString()
            val descripcion = descripcionField.text.toString()

            if (nombre.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || descripcion.isEmpty() || imagenUri == null) {
                CustomToast.show(this, 600)
            } else{
                val nuevoEvento = Evento(nombre, fecha, ubicacion, descripcion, imagenUriString)
                EventosManager.agregarEvento(nuevoEvento)
                CustomToast.show(this, R.id.fabEventoAgregar)
                val intent = Intent(this, EventService::class.java)
                startActivity(intent)
            }
        }

        btnAgregarImagen.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        fabRegresarEvento.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                imagenUri = uri
                Glide.with(this)
                    .load(imagenUri)
                    .into(btnAgregarImagen)
            }
        }
    }
}