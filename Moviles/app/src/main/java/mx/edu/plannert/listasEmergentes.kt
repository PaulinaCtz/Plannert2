package mx.edu.plannert

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class listasEmergentes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas_emergentes)

        val gridView = findViewById<GridView>(R.id.gridView)
        val cancelar = findViewById<Button>(R.id.btnCancelarEmergentes)

        val intent = intent
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val urlImagen = intent.getStringExtra("urlImagen")
        val nombreImagen = intent.getStringExtra("nombreImagen")
        val categoria = intent.getStringExtra("categoria")
        val tipo = intent.getStringExtra("tipo")
        val fecha = intent.getStringExtra("fecha")
     //  val botones = arrayOf("Botón 1", "Botón 2", "Botón 3", "Botón 4", "Botón 5")
       // val adapter = BotonesAdapter(this, botones)
        val detallesPeliculas: DetallesPeliculas = DetallesPeliculas()
        detallesPeliculas.urlImagen= urlImagen.toString()
        detallesPeliculas.categoria=categoria.toString()
        detallesPeliculas.fecha=fecha.toString()
        detallesPeliculas.tipo=tipo.toString()
        detallesPeliculas.descripcion=descripcion.toString()
        detallesPeliculas.nombreImagen=nombreImagen.toString()
        detallesPeliculas.titulo=titulo.toString()


      //  gridView.adapter = adapter

/*
            val intent = Intent(this, busqueda::class.java)
            intent.putExtra("fragmento", "FragmentoLista")
            startActivity(intent)*/
        val listas = ArrayList<String>()

        obtenerListasDeUsuario { listasUsuario ->
            // Utilizar la lista actualizada
            if (listasUsuario != null) {
                for (lista in listasUsuario) {
                    listas.add(lista.nombre)
                }

                val adapter = BotonesAdapter(this, listasUsuario as ArrayList<Lista>,detallesPeliculas)
                gridView.adapter = adapter
            }

        }

        cancelar.setOnClickListener{
            finish()
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



class BotonesAdapter(private val context: Context, private val botones: ArrayList<Lista>,private val contenido:DetallesPeliculas) : BaseAdapter() {

    override fun getCount(): Int {
        return botones.size
    }

    override fun getItem(position: Int): Any {
        return botones[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val button = Button(context)
        button.text = botones[position].nombre
        button.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        button.setOnClickListener {


            val listaSeleccionada = botones[position]
            //Toast.makeText(context, "Botón seleccionado: $listaSeleccionada", Toast.LENGTH_SHORT).show()

            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val listasRef: DatabaseReference = database.getReference("listas")



          //  val listaReferencia = listasRef.child("listas").child(listaSeleccionada.nombre)
            val listaReferencia = listasRef.orderByChild("nombre")
                .equalTo(listaSeleccionada.nombre)

            val nuevoContenido = HashMap<String, Any>()
            nuevoContenido["contenidos"] = listOf(contenido)

            listaReferencia.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Obtener la referencia al registro de la lista a actualizar
                    val registro = dataSnapshot.children.first()

                    // Actualizar el campo "contenidos" del registro con el nuevo valor
                    registro.ref.updateChildren(nuevoContenido).addOnCompleteListener {
                        Toast.makeText(context, "Contenido agregado a la lista", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Error al agregar contenido a la lista", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Error al obtener la lista de la base de datos", Toast.LENGTH_SHORT).show()
                }
            })




        }
        return button
    }

    //AQUI TERMINA


}