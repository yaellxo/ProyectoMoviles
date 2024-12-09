package com.example.proyectomoviles.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.proyectomoviles.R

object CustomToast {

    fun show(context: Context, buttonId: Int, gravity: Int = Gravity.BOTTOM, xOffset: Int = 0, yOffset: Int = 200) {
        val inflater = LayoutInflater.from(context)
        val customView: View = inflater.inflate(R.layout.toast, null)

        val textView = customView.findViewById<TextView>(R.id.textView)
        val imageView = customView.findViewById<ImageView>(R.id.imageView)
        val layout = customView.findViewById<LinearLayout>(R.id.customToastLayout)

        val background = layout.background as GradientDrawable
        when (buttonId) {
            //Registrar usuario-----------------------------
            R.id.btnInicioSesion -> {
                textView.text = "¡Ya era hora! El universo te esperaba."
                imageView.setImageResource(R.drawable.ic_iniciodesesion)
                background.setColor(ContextCompat.getColor(context, R.color.color_font_principal))
            }
            //Agregar Manga al Carrito-----------------------------
            R.id.btnCarrito -> {
                textView.text = "¡Lo has agregado! La aventura está a un clic de comenzar."
                imageView.setImageResource(R.drawable.ic_agregarmangacarrito)
                background.setColor(ContextCompat.getColor(context, R.color.boton_compra))
            }
            //Inicio de sesion exitoso-----------------------------
            R.id.btnCrearCuenta -> {
                textView.text = "Bienvenido a la Sociedad de Almas."
                imageView.setImageResource(R.drawable.ic_registrarse)
                background.setColor(ContextCompat.getColor(context, R.color.color_font_principal))
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
            R.id.fabAgregarManga -> {
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

            R.id.fabReporteAgregar -> {
                textView.text = "El reporte ha sido agrergado con éxito."
                imageView.setImageResource(R.drawable.ic_reporteventa_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_reporte))
            }

            //-------------------------------------------------------------------------------

            //Validaciones

            //Edad Inválida
            100 -> {
                textView.text = "Por favor, ingrese una edad válida"
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Alias o clave incorrectos
            200 -> {
                textView.text = "Alias o clave incorrectos."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Correo ya registrado
            300 -> {
                textView.text = "Este correo ya está registrado."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Alias ya registrado
            310 -> {
                textView.text = "Este alias ya está registrado."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Correo no valido
            320 -> {
                textView.text = "Correo electrónico no válido."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Datos cargados, puede realizar modificaciones
            400 -> {
                textView.text = "Datos cargados. Puede realizar modificaciones."
                imageView.setImageResource(R.drawable.ic_modificar_blanco)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }
            //Has alcanzado el limite de caracteres
            410 -> {
                textView.text = "Has alcanzado el límite de caracteres."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //ID INVÁLIDO
            500 -> {
                textView.text = "Ingresa un ID válido."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //completa todos los campos
            600 -> {
                textView.text = "Por favor, completa todos los campos."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Ingresar el ID del admin
            610 -> {
                textView.text = "Por favor, ingrese el ID del administrador."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Completar todos los campos incluyendo la imagen
            620 -> {
                textView.text = "Por favor, complete todos los campos, incluyendo la imagen."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Inicio de sesion como super administrador
            700 -> {
                textView.text = "Inicio de sesión exitoso como super administrador."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //Error al verificar administrador
            710 -> {
                textView.text = "Error al verificar administrador."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Administrador no encontrado
            720 -> {
                textView.text = "Administrador no encontrado."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Administrador eliminado con exito
            730 -> {
                textView.text = "Administrador eliminado con éxito."
                imageView.setImageResource(R.drawable.ic_perfil)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Manga eliminado con exito
            740 -> {
                textView.text = "Manga eliminado exitosamente."
                imageView.setImageResource(R.drawable.ic_inventario_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Manga no encontrado con ese ID
            750 -> {
                textView.text = "No se encontró el manga con ese ID."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Manga no encontrado
            760 -> {
                textView.text = "Manga no encontrado."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //No se encontraron mangas
            770 -> {
                textView.text = "No se encontraron mangas."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Administrador modificado con exito
            780 -> {
                textView.text = "Administrador modificado con éxito."
                imageView.setImageResource(R.drawable.ic_modificar_blanco)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }
            //Imagen actualizada
            800 -> {
                textView.text = "¡La nueva versión de ti está lista para luchar!"
                imageView.setImageResource(R.drawable.ic_cambioimagen)
                background.setColor(ContextCompat.getColor(context, R.color.negro_color))
            }
            //Error al cargar la imagen
            810 -> {
                textView.text = "Hubo un error al cargar la imagen."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Alias activo no encontrado
            820 -> {
                textView.text = "Alias activo no encontrado. No se pudo actualizar la imagen."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //No se selecciono ninguna imagen
            830 -> {
                textView.text = "No se seleccionó ninguna imagen."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Manga agregado con exito
            900 -> {
                textView.text = "Manga agregado con éxito!"
                imageView.setImageResource(R.drawable.ic_inventario_admin)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //Manga modificado con exito
            910 -> {
                textView.text = "Manga modificado con éxito!"
                imageView.setImageResource(R.drawable.ic_modificar_blanco)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_modificar))
            }
            //Error al modificar manga
            920 -> {
                textView.text = "Error al modificar manga."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Sesion cerrada
            1000 -> {
                textView.text = "Sesión cerrada."
                imageView.setImageResource(R.drawable.ic_todogood)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_agregar))
            }
            //Por favor ingrese un reporte
            1100 -> {
                textView.text = "Por favor, ingrese un reporte."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Reporte guardado exitosamente como PDF
            1200 -> {
                textView.text = "Reporte guardado exitosamente como PDF."
                imageView.setImageResource(R.drawable.ic_todogood)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_reporte))
            }
            //Error al abrir el archivo
            1300 -> {
                textView.text = "Error al abrir el archivo."
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
            }
            //Error: Tipo de usuario no reconocido
            1400 -> {
                textView.text = "Error: Tipo de usuario no reconocido :("
                imageView.setImageResource(R.drawable.advertencia)
                background.setColor(ContextCompat.getColor(context, R.color.opcion_eliminar))
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

        toast.setGravity(gravity, xOffset, yOffset)

        toast.show()
    }
}