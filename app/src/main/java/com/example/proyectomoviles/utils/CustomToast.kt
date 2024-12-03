package com.example.proyectomoviles.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.proyectomoviles.R

object CustomToast {

    fun show(context: Context, buttonId: Int) {
        // Inflar la vista personalizada
        val inflater = LayoutInflater.from(context)
        val customView: View = inflater.inflate(R.layout.toast, null)

        // Referencias a los elementos
        val textView = customView.findViewById<TextView>(R.id.textView)
        val imageView = customView.findViewById<ImageView>(R.id.imageView)
        val layout = customView.findViewById<LinearLayout>(R.id.customToastLayout)

        // Cambiar apariencia según botón presionado
        when (buttonId) {
            R.id.btnEliminar -> {
                textView.text = "Botón elimiar presionado"
                imageView.setImageResource(R.drawable.ic_busqueda)
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            R.id.btnRegistro -> {
                textView.text = "Botón registro presionado"
                imageView.setImageResource(R.drawable.ic_busqueda)
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            else -> {
                textView.text = "Botón no configurado aún"
                imageView.setImageResource(R.drawable.ic_launcher_background)
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //Demas info para cada boton .......
            


            //Ejemplo de uso en activities:
            //  val btnEliminar = findViewById<Button>(R.id.btnEliminar)
            //  btnEliminar.setOnClickListener {
            //    CustomToast.show(this, R.id.btnEliminar)
            //  }
        }

        // Crear y mostrar el Toast
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customView
        toast.show()
    }
}