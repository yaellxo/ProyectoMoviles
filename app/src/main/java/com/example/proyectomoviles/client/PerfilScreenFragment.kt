package com.example.proyectomoviles.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyectomoviles.R

class PerfilScreenFragment : Fragment(R.layout.perfil_activity) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCerrarSesion = view.findViewById<Button>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            onCerrarSesionClick()
        }
    }

    private fun onCerrarSesionClick() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.remove("userToken")
        editor.remove("userId")

        editor.apply()

        val intent = Intent(activity, LoginScreen::class.java)
        startActivity(intent)

        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}
