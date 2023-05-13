package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val opcionPerfil= findViewById<Button>(R.id.opcionPerfil)
        val opcionConfiguracion= findViewById<Button>(R.id.opcionConfiguracion)
        val opcionNosotros= findViewById<Button>(R.id.opcionNosotros)
        val opcionAyuda= findViewById<Button>(R.id.opcionAyuda)
        val regresaInicio = findViewById<ImageView>(R.id.menuu)


        opcionPerfil.setOnClickListener{
            val intent = Intent(this, OpcionesMenu::class.java)
            startActivity(intent)
        }

        opcionAyuda.setOnClickListener{
            val intent = Intent(this, help::class.java)
            startActivity(intent)
        }

        opcionNosotros.setOnClickListener{
            val intent = Intent(this, us::class.java)
            startActivity(intent)
        }

        opcionConfiguracion.setOnClickListener {
            val intent = Intent(this, ConfigIdioma::class.java)
            startActivity(intent)
        }
        regresaInicio.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }
    }
}