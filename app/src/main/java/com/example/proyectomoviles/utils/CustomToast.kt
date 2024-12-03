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
        val inflater = LayoutInflater.from(context)
        val customView: View = inflater.inflate(R.layout.toast, null)

        val textView = customView.findViewById<TextView>(R.id.textView)
        val imageView = customView.findViewById<ImageView>(R.id.imageView)
        val layout = customView.findViewById<LinearLayout>(R.id.customToastLayout)

        when (buttonId) {
            R.id.btnRegistro -> {
                textView.text = "Botón registro presionado"
                imageView.setImageResource(R.drawable.icon_eliminar)
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }

            //Demas info para cada boton .......


            else -> {
                textView.text = "Registra el id de este boton en utils/CustomToast"
                imageView.setImageResource(R.drawable.ic_launcher_background)
                layout.setBackgroundColor(ContextCompat.getColor(context, R.color.negro_color))
            }
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customView
        toast.show()

        //Ejemplo de uso en activities:
        //  val btnRegitro = findViewById<Button>(R.id.btnRegitro)
        //  btnRegitro.setOnClickListener {
        //    CustomToast.show(this, R.id.btnRegitro)
        //  }
    }
}