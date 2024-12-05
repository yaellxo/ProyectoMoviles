package com.example.proyectomoviles.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
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

        val background = layout.background as GradientDrawable
        when (buttonId) {
            //Registrar usuario-----------------------------
            R.id.btnCrearCuenta -> {
                textView.text = "Usuario registrado exitosamente."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //Inicio de sesion exitoso-----------------------------
            R.id.btnCrearCuenta -> {
                textView.text = "Inicio de sesión exitoso."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //ELIMINAR-----------------------------
            R.id.fabEventoEliminar -> {
                textView.text = "El evento ha sido eliminado con éxito."
                imageView.setImageResource(R.drawable.ic_eventos_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            R.id.fabMangaEliminar -> {
                textView.text = "El manga ha sido eliminado con éxito."
                imageView.setImageResource(R.drawable.ic_inventario_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            R.id.fabAdminEliminar -> {
                textView.text = "El perfil ha sido eliminado con éxito."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }

            //AGREGAR-----------------------------
            R.id.fabEventoAgregar -> {
                textView.text = "El evento ha sido agregado con éxito."
                imageView.setImageResource(R.drawable.ic_eventos_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            R.id.fabMangaAgregar -> {
                textView.text = "El manga ha sido agregado con éxito."
                imageView.setImageResource(R.drawable.ic_inventario_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            R.id.fabAdminAgregar -> {
                textView.text = "El perfil ha sido agregado con éxito."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }

            //MODIFICAR-----------------------------
            R.id.fabEventoModificar -> {
                textView.text = "El evento ha sido modificado con éxito."
                imageView.setImageResource(R.drawable.ic_eventos_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }
            R.id.fabEventoModificar -> {
                textView.text = "El manga ha sido modificado con éxito."
                imageView.setImageResource(R.drawable.ic_inventario_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }
            R.id.fabAdminModificar -> {
                textView.text = "El perfil ha sido modificado con éxito."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }

            //REPORTE-----------------------------
            R.id.fabReporteAgregar -> {
                textView.text = "El reporte ha sido agrergado con éxito."
                imageView.setImageResource(R.drawable.ic_reporteventa_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_reporte))
            }

            else -> {
                textView.text = "Registra el id de este boton en utils/CustomToast"
                imageView.setImageResource(R.drawable.ic_launcher_background)
                background.setColor(ContextCompat.getColor(context, R.color.negro_color))
            }
        }

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = customView
        toast.show()

        //Ejemplo de uso en activities:
        //  val btnRegitro : Button = findViewById (R.id.btnRegitro)
        //  btnRegitro.setOnClickListener {
        //    CustomToast.show(this, R.id.btnRegitro)
        //  }
    }
}