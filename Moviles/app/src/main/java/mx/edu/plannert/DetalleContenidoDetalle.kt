package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class DetalleContenidoDetalle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_contenido_detalle)

        val imagenView = findViewById<ImageView>(R.id.imagenContenido)
        val descripcionview = findViewById<TextView>(R.id.txtDescripcion)
        val tituloview = findViewById<TextView>(R.id.tituloContenido)
        val agregar=findViewById<ImageView>(R.id.agregarContenido)

        val intent = intent
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val imagen = intent.getStringExtra("imagen")

        Picasso.get().load(imagen).into(imagenView)
        descripcionview.setText(descripcion)
        tituloview.setText(titulo)

        agregar.setOnClickListener {
            val intent = Intent(this, listasEmergentes::class.java)
            startActivity(intent)
        }


    }
}