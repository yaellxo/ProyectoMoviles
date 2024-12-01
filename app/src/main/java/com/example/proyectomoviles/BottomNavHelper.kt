package com.example.proyectomoviles

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavHelper {

    fun setupBottomNavigationView(
        bottomNavigationView: BottomNavigationView,
        navController: NavController // Añadido el parámetro NavController
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
                    val userType = sharedPreferences.getString("userType", "user")

                    if (userType == "admin") {
                        // Navegar al perfil del administrador
                        navController.navigate(R.id.menu_admin)
                    } else {
                        // Navegar al perfil del usuario regular
                        navController.navigate(R.id.menu_perfil)
                    }
                }
            }
            true
        }
    }
}
