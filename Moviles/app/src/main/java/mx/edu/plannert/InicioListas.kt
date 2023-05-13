package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InicioListas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_listas)

        val button = findViewById<Button>(R.id.btnInicioLista)
        val masTarde:TextView=findViewById(R.id.masTardeInicioListas)
        button.setOnClickListener {
            val intent = Intent(this, CrearLista::class.java)
            startActivity(intent)
        }

        masTarde.setOnClickListener{
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }
    }
}