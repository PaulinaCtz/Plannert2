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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

        val detallesPeliculas: DetallesPeliculas = DetallesPeliculas()
        detallesPeliculas.urlImagen= urlImagen.toString()
        detallesPeliculas.categoria=categoria.toString()
        detallesPeliculas.fecha=fecha.toString()
        detallesPeliculas.tipo=tipo.toString()
        detallesPeliculas.descripcion=descripcion.toString()
        detallesPeliculas.nombreImagen=nombreImagen.toString()
        detallesPeliculas.titulo=titulo.toString()


      //  gridView.adapter = adapter



        obtenerListasDeUsuario { listasDeUsuario ->

            if (listasDeUsuario != null) {




                val adapter = BotonesAdapter(this, listasDeUsuario as ArrayList<Lista>,detallesPeliculas)
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
    var isUpdating = false
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
        val contenidos = botones[position].contenidos
        var encontrado = false

        for (item in contenidos) {
            if (item.titulo == contenido.titulo) {
                encontrado = true
                break
            }
        }

        if (encontrado) {
            button.isEnabled = false
        }


        button.setOnClickListener {
        button.isEnabled=false



                val listaSeleccionada = botones[position]

                obtenerLista(listaSeleccionada) { listaDetalleContenido ->

                    val contenidos: ArrayList<DetallesPeliculas> =
                        ArrayList(listaDetalleContenido.get(0).contenidos)
                    Toast.makeText(context, listaSeleccionada.usuario, Toast.LENGTH_SHORT).show()

                    //comparar con el id de la lista seleccionada
                    contenidos.add(contenido)
                    actualizarLista(contenidos, listaSeleccionada, context,contenido)



            }


        }
            return button

    }

    //AQUI TERMINA
    fun obtenerLista(contenido: Lista, callback: (ArrayList<Lista>) -> Unit) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val listasRef: DatabaseReference = database.getReference("listas")
        val listaDetalleContenido = ArrayList<Lista>()

        // Consultar el usuario que coincida con el email actual
        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val queryUsuario = usuariosRef.orderByChild("email").equalTo(currentUser?.email)

        queryUsuario.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usuarioSnapshot = dataSnapshot.children.firstOrNull()
                val usuarioKey = usuarioSnapshot?.key

                // Obtener la lista que coincida con el nombre
                val queryLista = listasRef.orderByChild("nombre").equalTo(contenido.nombre)

                queryLista.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listaDetalleContenido.clear()
                        for (registroSnapshot in dataSnapshot.children) {
                            val registro = registroSnapshot.getValue(Lista::class.java)
                            registro?.let {
                                if (it.idUsuario == usuarioKey) {
                                    listaDetalleContenido.add(it)
                                }
                            }
                        }
                        // Llamar al callback con la lista actualizada
                        callback(listaDetalleContenido)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejar errores
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }


    fun actualizarLista(contenidos: List<DetallesPeliculas>, listaSeleccionada: Lista, context: Context, contenido: DetallesPeliculas) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val listasRef: DatabaseReference = database.getReference("listas")

        val listaReferencia = listasRef.orderByChild("idUsuario")
            .equalTo(listaSeleccionada.idUsuario)

        listaReferencia.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val registro = dataSnapshot.children.firstOrNull()

                if (registro != null) {
                    val lista = registro.getValue(Lista::class.java)
                    val listaContenidos = lista?.contenidos

                    if (listaContenidos != null) {
                        val contenidoExistente = listaContenidos.find { it.titulo == contenido.titulo }

                        if (contenidoExistente == null) {
                            listaContenidos.add(contenido)

                            registro.ref.child("contenidos").setValue(listaContenidos)
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        context,
                                        "Contenido agregado a la lista",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Error al agregar contenido a la lista",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            // Toast.makeText(context, "El contenido ya está en la lista", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "No se encontraron contenidos en la lista", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No se encontró la lista en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Error al obtener la lista de la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
    }








}