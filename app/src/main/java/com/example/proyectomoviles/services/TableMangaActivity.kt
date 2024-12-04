package com.example.proyectomoviles.services

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.example.proyectomoviles.repositories.MangaRepository
import com.example.proyectomoviles.services.RegistrarMangaActivity

class TableMangaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table_manga)

        val repository = MangaRepository(this)
        val tableLayout = findViewById<TableLayout>(R.id.table_layout)

        val mangas = repository.getAllMangas()
        mangas.forEach { manga ->
            val row = TableRow(this)
            row.addView(TextView(this).apply { text = manga.id })
            row.addView(TextView(this).apply { text = manga.stock.toString() })
            row.addView(TextView(this).apply { text = manga.nombre })
            row.addView(TextView(this).apply { text = manga.precio.toString() })
            tableLayout.addView(row)
        }

        val btnAdd = findViewById<ImageButton>(R.id.btn_add)

        btnAdd.setOnClickListener {
            val intent = Intent(this, RegistrarMangaActivity::class.java)
            startActivity(intent)
        }


    }
}
