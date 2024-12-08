package com.example.proyectomoviles.services

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomoviles.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfStamper
import com.itextpdf.text.pdf.PdfContentByte
import com.itextpdf.text.PageSize
import java.io.InputStream
import java.io.OutputStream

class ReportService : AppCompatActivity() {

    private val CREATE_FILE_REQUEST_CODE = 1
    private val MAX_CHARACTERS = 3100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrar_reporte_activity)

        val etReporteRegistrar: EditText = findViewById(R.id.etReporteRegistrar)
        val fabRegistrarReporte: FloatingActionButton = findViewById(R.id.fabReporteAgregar)
        val fabRegresarReporteModificar: FloatingActionButton = findViewById(R.id.fabRegresarReporteAgregar)
        val tvCaracteresRestantes: TextView = findViewById(R.id.tvCaracteresRestantes)

        tvCaracteresRestantes.text = "Caracteres restantes: $MAX_CHARACTERS"

        etReporteRegistrar.addTextChangedListener(object : TextWatcher {
            private var isEditing = false

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (isEditing) return
                isEditing = true

                val currentText = charSequence.toString()
                if (currentText.length > MAX_CHARACTERS) {
                    val textoPermitido = currentText.substring(0, MAX_CHARACTERS)
                    etReporteRegistrar.setText(textoPermitido)
                    etReporteRegistrar.setSelection(MAX_CHARACTERS)
                    Toast.makeText(this@ReportService, "Has alcanzado el límite de caracteres.", Toast.LENGTH_SHORT).show()
                }

                isEditing = false
            }

            override fun afterTextChanged(editable: Editable?) {
                val caracteresRestantes = (MAX_CHARACTERS - (editable?.length ?: 0)).coerceAtLeast(0)

                tvCaracteresRestantes.text = "Caracteres restantes: $caracteresRestantes"

            }
        })


        fabRegistrarReporte.setOnClickListener {
            val reporteTexto = etReporteRegistrar.text.toString().trim()

            if (reporteTexto.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un reporte", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_TITLE, "reporte_${System.currentTimeMillis()}.pdf")
            }
            startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
        }

        fabRegresarReporteModificar.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri = data?.data ?: return

            val reporteTexto = findViewById<EditText>(R.id.etReporteRegistrar).text.toString()

            saveReportAsPdf(uri, reporteTexto)
        }
    }

    private fun saveReportAsPdf(uri: Uri, reporteTexto: String) {
        try {
            val maxLength = 3100
            val textoLimitado = if (reporteTexto.length > maxLength) {
                reporteTexto.substring(0, maxLength)
            } else {
                reporteTexto
            }

            val contentResolver: ContentResolver = contentResolver
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)

            if (outputStream != null) {
                val templateStream: InputStream = assets.open("template_empresa.pdf")
                val reader = PdfReader(templateStream)

                val stamper = PdfStamper(reader, outputStream)
                val content: PdfContentByte = stamper.getOverContent(1)

                // Definir la fuente y tamaño
                val font = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA).baseFont
                content.setFontAndSize(font, 12f)

                // Establecer la posición inicial
                var xPos = 50f  // Coordenada X inicial
                var yPos = 600f  // Coordenada Y inicial
                val marginRight = 50f  // Margen derecho de 50 unidades

                val lineHeight = 16f

                val phrase = com.itextpdf.text.Phrase(textoLimitado)

                var currentLine = StringBuilder()
                val maxLineWidth = PageSize.A4.width - marginRight - xPos

                for (char in phrase.content) {
                    currentLine.append(char)
                    val currentLineWidth = font.getWidthPoint(currentLine.toString(), 12f)

                    if (currentLineWidth > maxLineWidth) {
                        content.setTextMatrix(xPos, yPos)
                        content.showText(currentLine.toString().trim())
                        yPos -= lineHeight
                        currentLine = StringBuilder(char.toString())
                    }
                }

                if (currentLine.isNotEmpty()) {
                    content.setTextMatrix(xPos, yPos)
                    content.showText(currentLine.toString().trim())
                }

                stamper.close()
                reader.close()

                Toast.makeText(this, "Reporte guardado como PDF exitosamente.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error al abrir el archivo.", Toast.LENGTH_LONG).show()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar el reporte como PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

