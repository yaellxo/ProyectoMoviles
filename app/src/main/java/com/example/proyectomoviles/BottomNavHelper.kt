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

            menuItemView?.setOnTouchListener { v, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val rotate = RotateAnimation(
                            0f, 15f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f
                        )
                        rotate.duration = 200
                        v?.startAnimation(rotate)
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val rotateBack = RotateAnimation(
                            15f, 0f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                            RotateAnimation.RELATIVE_TO_SELF, 0.5f
                        )
                        rotateBack.duration = 200
                        v?.startAnimation(rotateBack)
                    }
                }
                false
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            bottomNavigationView.menu.forEach { menuItem ->
                val itemView = bottomNavigationView.findViewById<View>(menuItem.itemId)
                itemView?.isSelected = menuItem.itemId == item.itemId
            }

            when (item.itemId) {
                R.id.menu_hot -> navController.navigate(R.id.menu_hot)
                R.id.menu_eventos -> navController.navigate(R.id.menu_eventos)
                R.id.menu_busqueda -> navController.navigate(R.id.menu_busqueda)
                R.id.menu_carrito -> navController.navigate(R.id.menu_carrito)
                R.id.menu_perfil -> {
                    val sharedPreferences =
                        bottomNavigationView.context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

                    val storedAlias = sharedPreferences.getString("activeUserAlias", "No disponible")
                    val nombre = sharedPreferences.getString("nombre", "No disponible")
                    val correo = sharedPreferences.getString("correo", "No disponible")
                    val edad = sharedPreferences.getString("edad", "No disponible")
                    val userType = sharedPreferences.getString("userType", "No disponible")
                    val photoBase64 = sharedPreferences.getString("photoBase64", null)
                    val area = sharedPreferences.getString("area", null)
                    val nivelAcceso = sharedPreferences.getString("nivelAcceso", null)

                    Log.d("BottomNavHelper", "Alias: $storedAlias, Nombre: $nombre, Correo: $correo, Edad: $edad, Tipo de usuario: $userType")

                    val bundle = Bundle().apply {
                        putString("alias", storedAlias)
                        putString("nombre", nombre)
                        putString("correo", correo)
                        putString("edad", edad)
                        putString("userType", userType)
                        putString("photoBase64", photoBase64)
                        putString("area", area)
                        putString("nivelAcceso", nivelAcceso)
                    }

                    when (userType) {
                        "superAdmin" -> {
                            Log.d("BottomNavHelper", "Navigating to superadmin menu")
                            navController.navigate(R.id.menu_super_admin, bundle)
                        }
                        "admin" -> {
                            Log.d("BottomNavHelper", "Navigating to admin menu")
                            navController.navigate(R.id.menu_admin, bundle)
                        }
                        "user" -> {
                            Log.d("BottomNavHelper", "Navigating to user profile")
                            navController.navigate(R.id.menu_perfil, bundle)
                        }
                        else -> {
                            Toast.makeText(
                                bottomNavigationView.context,
                                "Error: Tipo de usuario no reconocido",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            true
        }
    }
}