package com.example.proyectomoviles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavHelper {

    fun setupBottomNavigationView(
        bottomNavigationView: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigationView.menu.forEach { menuItem ->
            menuItem.title = menuItem.title
        }

        bottomNavigationView.menu.forEach { menuItem ->
            val menuItemView = bottomNavigationView.findViewById<View>(menuItem.itemId)

            menuItemView?.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event?.action == MotionEvent.ACTION_DOWN) {
                        val rotate = RotateAnimation(
                            0f, 15f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f
                        )
                        rotate.duration = 200
                        v?.startAnimation(rotate)
                    }

                    if (event?.action == MotionEvent.ACTION_UP || event?.action == MotionEvent.ACTION_CANCEL) {
                        val rotateBack = RotateAnimation(
                            15f, 0f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f
                        )
                        rotateBack.duration = 200
                        v?.startAnimation(rotateBack)
                    }

                    return false
                }
            })
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_hot -> navController.navigate(R.id.menu_hot)
                R.id.menu_eventos -> navController.navigate(R.id.menu_eventos)
                R.id.menu_busqueda -> navController.navigate(R.id.menu_busqueda)
                R.id.menu_carrito -> navController.navigate(R.id.menu_carrito)
                R.id.menu_perfil -> {
                    val sharedPreferences = bottomNavigationView.context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

                    val alias = sharedPreferences.getString("alias", "No disponible")
                    val nombre = sharedPreferences.getString("nombre", "No disponible")
                    val correo = sharedPreferences.getString("correo", "No disponible")
                    val edad = sharedPreferences.getString("edad", "No disponible")
                    val userType = sharedPreferences.getString("userType", "No disponible")
                    val photoUrl = sharedPreferences.getString("photoUrl", "")

                    Log.d("BottomNavHelper", "Alias: $alias, Nombre: $nombre, Correo: $correo, Edad: $edad, Tipo de usuario: $userType, Foto: $photoUrl")

                    val mensaje = "Bienvenido $nombre ($alias)\nCorreo: $correo\nEdad: $edad\nTipo de usuario: $userType"
                    Toast.makeText(bottomNavigationView.context, mensaje, Toast.LENGTH_LONG).show()

                    val bundle = Bundle().apply {
                        putString("alias", alias)
                        putString("nombre", nombre)
                        putString("correo", correo)
                        putString("edad", edad)
                        putString("userType", userType)
                        putString("photoUrl", photoUrl)
                    }

                    if (userType == "admin") {
                        Log.d("BottomNavHelper", "Navigating to admin menu")
                        navController.navigate(R.id.menu_admin)
                    } else {
                        Log.d("BottomNavHelper", "Navigating to user profile")
                        navController.navigate(R.id.menu_perfil, bundle)
                    }
                }
            }
            true
        }
    }
}