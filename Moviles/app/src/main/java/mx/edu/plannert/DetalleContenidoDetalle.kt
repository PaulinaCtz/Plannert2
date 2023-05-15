package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

            obtenerListasDeUsuario { listasDeUsuario ->

                if (listasDeUsuario?.size?:0> 0) {


                    startActivity(intent2)

                }else{
                    Toast.makeText(
                        this,
                        "No hay listas ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }


    }

    fun obtenerListasDeUsuario( callback: (listas: List<Lista>?) -> Unit) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val listasRef: DatabaseReference = database.getReference("listas")
        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()



        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email



            usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usuarioSnapshot in dataSnapshot.children) {
                        val usuarioKey = usuarioSnapshot.key


                        listasRef.orderByChild("idUsuario").equalTo(usuarioKey)
                            .addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val listas = mutableListOf<Lista>()

                                    for (listaSnapshot in dataSnapshot.children) {
                                        val lista = listaSnapshot.getValue(Lista::class.java)
                                        lista?.let {
                                            listas.add(it)
                                        }
                                    }

                                    callback(listas)
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    callback(null)
                                }
                            })






                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

        }
    }
}