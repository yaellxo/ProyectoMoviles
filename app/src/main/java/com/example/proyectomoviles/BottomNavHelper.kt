package com.example.proyectomoviles

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.example.proyectomoviles.admin.AdminActivity
import com.example.proyectomoviles.admin.PerfilActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavHelper {

    fun setupBottomNavigationView(
        bottomNavigationView: BottomNavigationView,
        navController: NavController,
        userType: String // Recibe el tipo de usuario
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
                R.id.menu_perfil -> {
                    val intent = if (userType == "admin") {
                        Intent(bottomNavigationView.context, AdminActivity::class.java)
                    } else {
                        Intent(bottomNavigationView.context, PerfilActivity::class.java)
                    }
                    bottomNavigationView.context.startActivity(intent)
                }
                R.id.menu_hot -> navController.navigate(R.id.menu_hot)
                R.id.menu_eventos -> navController.navigate(R.id.menu_eventos)
                R.id.menu_busqueda -> navController.navigate(R.id.menu_busqueda)
                R.id.menu_carrito -> navController.navigate(R.id.menu_carrito)
            }
            true
        }
    }
}
