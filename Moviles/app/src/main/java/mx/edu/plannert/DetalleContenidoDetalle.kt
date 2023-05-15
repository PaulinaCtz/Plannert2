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

        var intent = intent
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val urlImagen = intent.getStringExtra("urlImagen")
        val nombreImagen = intent.getStringExtra("nombreImagen")
        val categoria = intent.getStringExtra("categoria")
        val tipo = intent.getStringExtra("tipo")
        val fecha = intent.getStringExtra("fecha")

        val intent2 = Intent(this, listasEmergentes::class.java)
        intent2.putExtra("titulo", titulo)
        intent2.putExtra("descripcion", descripcion)
        intent2.putExtra("urlImagen", urlImagen)
        intent2.putExtra("categoria", categoria)
        intent2.putExtra("fecha", fecha)
        intent2.putExtra("nombreImagen", nombreImagen)
        intent2.putExtra("tipo", tipo)

//DE AQUI MANDAMOS GUARDAR


        Picasso.get().load(urlImagen).into(imagenView)
        descripcionview.setText(descripcion)
        tituloview.setText(titulo)

        agregar.setOnClickListener {

            val intent2 = Intent(this, listasEmergentes::class.java)
            intent2.putExtra("titulo", titulo)
            intent2.putExtra("descripcion", descripcion)
            intent2.putExtra("urlImagen", urlImagen)
            intent2.putExtra("categoria", categoria)
            intent2.putExtra("fecha", fecha)
            intent2.putExtra("nombreImagen", nombreImagen)
            intent2.putExtra("tipo", tipo)

            startActivity(intent2)
        }


    }


}