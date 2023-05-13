package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Bienvenida2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida2)
        var boton: Button =findViewById(R.id.btnCrearLista)
        val later : TextView = findViewById(R.id.despues)

        boton.setOnClickListener{
            val intent = Intent(this, CrearLista::class.java)
            startActivity(intent)
        }
        later.setOnClickListener{
            val intent = Intent(this,Inicio::class.java)
            startActivity(intent)
        }
    }
}