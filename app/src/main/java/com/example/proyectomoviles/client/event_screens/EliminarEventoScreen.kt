package com.example.proyectomoviles.client.event_screens

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.models.EventosManager
import com.example.proyectomoviles.models.EventosManager.eventosList
import com.example.proyectomoviles.services.EventService
import com.example.proyectomoviles.utils.CustomToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EliminarEventoScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eliminar_evento_activity)

        val idField = findViewById<EditText>(R.id.etIdEventoEliminar)
        val btnElimiar: FloatingActionButton = findViewById(R.id.fabEventoEliminar)

        btnElimiar.setOnClickListener{
            val id = idField.text.toString()
            if(id.isNotEmpty() && !id.isNotNumber()){
                if(id.toInt() < eventosList.size && id.toInt() >= 0){
                    EventosManager.eliminarEvento(id.toInt())
                    CustomToast.show(this, R.id.fabEventoEliminar)
                    val intent = Intent(this, EventService::class.java)
                    startActivity(intent)
                }else{
                    CustomToast.show(this, 500)
                }
            } else {
                CustomToast.show(this, 500)
            }
        }


    }

    fun String.isNotNumber(): Boolean {
        return this.toIntOrNull() == null
    }
}