package mx.edu.plannert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class Ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Mostrar el botón de volver atrás en la barra de herramientas

        // Mostrar el botón de volver atrás en la barra de herramientas
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        // Cambiar el título de la barra de herramientas
        //getSupportActionBar()?.setTitle("Volver / Necesito ayuda");

        getSupportActionBar()?.setTitle(Html.fromHtml("<font color='#ffffff' size='5sp' font-family='@font/intermedium'>Menu / Ayuda</font>"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}