package mx.edu.plannert

import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Notificaciones : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        // Obtener el spinner por su ID y asignar un listener
        val spinner = findViewById<Spinner>(R.id.my_spinner)
        spinner.onItemSelectedListener = this

        // Crear el adapter con los valores que deseas mostrar en el spinner
        val valores = arrayOf("Tono predeterminado", "Ninguno", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone", "Ringtone")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, valores)

        // Configurar el adaptador para mostrar los valores en el spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al spinner
        spinner.adapter = adapter

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Mostrar el botón de volver atrás en la barra de herramientas

        // Mostrar el botón de volver atrás en la barra de herramientas
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        // Cambiar el título de la barra de herramientas
        //getSupportActionBar()?.setTitle("Volver / Necesito ayuda");

        getSupportActionBar()?.setTitle(Html.fromHtml("<font color='#ffffff' size='5sp' font-family='@font/intermedium'>Configuración / Idioma</font>"))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Obtener el valor seleccionado del spinner y mostrar un Toast
        val item = parent?.getItemAtPosition(position).toString()
        Toast.makeText(this, "Seleccionaste: $item", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Método requerido, pero no se utiliza en este ejemplo
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
