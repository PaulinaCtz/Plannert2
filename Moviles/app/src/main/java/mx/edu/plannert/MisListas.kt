package mx.edu.plannert

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MisListas : AppCompatActivity() {

    lateinit var favoritas: RecyclerView
    lateinit var recientes: RecyclerView
    lateinit var listas: RecyclerView
    private var portadaAdapter1: PortadaAdapter? = null
    private var portadaAdapter2: PortadaAdapter? = null
    private var portadas = ArrayList<Portada>()
    private var favs = ArrayList<Portada>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_listas)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Mostrar el botón de volver atrás en la barra de herramientas
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        supportActionBar?.title =
            Html.fromHtml("<font color='#ffffff' size='5sp' font-family='@font/intermedium'>Inicio / Mis listas</font>")

        // Inicializar RecyclerViews
        favoritas = findViewById(R.id.listasFavoritas)
        recientes = findViewById(R.id.listasRecientes)
        listas = findViewById(R.id.listas)

        //  obtenerListaPortadas()
        val gridLayoutManager1 = GridLayoutManager(this, 4)
        val gridLayoutManager2 = GridLayoutManager(this, 4)
        val gridLayoutManager5 = GridLayoutManager(this, 3)

        favoritas.layoutManager = gridLayoutManager1
        recientes.layoutManager = gridLayoutManager2
        listas.layoutManager = gridLayoutManager5

        obtenerListaPortadas(object : ObtenerListaPortadasCallback {
            override fun onListaPortadasObtenida(portadas: ArrayList<Portada>) {
                // Aquí puedes hacer algo con la lista de portadas
                // Configurar GridLayoutManager para los RecyclerViews

                var favo: ArrayList<Portada> = ArrayList()

                for (portada in portadas) {
                    Log.d("Lista",portada.toString())
                    // Acceder a los atributos de cada Portada
                    if(portada.favorita==true){
                        favo.add(portada)
                    }

                }

                this@MisListas.portadas.clear()
                this@MisListas.portadas.addAll(portadas)
                this@MisListas.favs.clear()
                if (favo != null) {
                    this@MisListas.favs.addAll(favo)
                }

                // Inicializar y asignar adaptadores a los RecyclerViews
                portadaAdapter1 = PortadaAdapter(portadas)
                portadaAdapter2 = PortadaAdapter(favo)

                favoritas.adapter = portadaAdapter2
                recientes.adapter = portadaAdapter1
                listas.adapter = portadaAdapter1
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    interface ObtenerListaPortadasCallback {
        fun onListaPortadasObtenida(portadas: ArrayList<Portada>)
    }


    fun obtenerListaPortadas(callback: ObtenerListaPortadasCallback) {
        val database = FirebaseDatabase.getInstance()
        val databaseReferenceListas = database.getReference("listas")

        val portadas = ArrayList<Portada>()
        val favoritass = ArrayList<Portada>()

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email

            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("usuarios")

            databaseReference.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usuarioSnapshot in dataSnapshot.children) {
                        val usuarioKey = usuarioSnapshot.key
                     // Aqui ya que obtengo el usuario tome el key para comparar con el de la sesion y mostrar solo las listas de el

                        databaseReferenceListas.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                //Limpiar

                                portadas.clear()

                                // Manejar los datos obtenidos de los registros
                                for (registroSnapshot in dataSnapshot.children) {
                                    // Obtener los datos del registro
                                    val categoria = registroSnapshot.child("icono").getValue(String::class.java)
                                    //    val contenidos = registroSnapshot.child("contenidos").getValue(String::class.java)
                                    val favorita = registroSnapshot.child("favorita").getValue(Boolean::class.java)
                                    val icono = registroSnapshot.child("icono").getValue(String::class.java)
                                    val nombre = registroSnapshot.child("nombre").getValue(String::class.java)
                                    val tipo = registroSnapshot.child("tipo").getValue(String::class.java)
                                    val usuario = registroSnapshot.child("idUsuario").getValue(String::class.java)

                                    if(usuarioKey==usuario) {

                                            if (icono == "icono1") {
                                                val nombreLista = nombre ?: ""
                                                var favorita = favorita?:false
                                                portadas.add(
                                                    Portada(
                                                        R.drawable.portadalistados,
                                                        nombreLista,
                                                        favorita
                                                    )
                                                )
                                        }
                                        if (icono == "icono2") {

                                                val nombreLista = nombre ?: ""
                                            var favorita = favorita?:false
                                                portadas.add(
                                                    Portada(
                                                        R.drawable.portadalistatres,
                                                        nombreLista,favorita
                                                    )
                                                )

                                        }
                                        if (icono == "icono3") {

                                                val nombreLista = nombre ?: ""
                                            var favorita = favorita?:false
                                                portadas.add(
                                                    Portada(
                                                        R.drawable.portadalistauno,
                                                        nombreLista,favorita
                                                    )
                                                )


                                        }
                                        if (icono == "icono4") {

                                                val nombreLista = nombre ?: ""
                                            var favorita = favorita?:false
                                                portadas.add(
                                                    Portada(
                                                        R.drawable.portadalistacuatro,
                                                        nombreLista,favorita
                                                    )
                                                )

                                        }
                                    }


                                    // Manejar los datos obtenidos del registro
                                }
                                callback.onListaPortadasObtenida(portadas)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                // Manejar el error si lo hay
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar el error si lo hay
                }
            })
        } else {
            // El usuario actual no está autenticado
            Toast.makeText(this, "El usuario actual no está autenticado.", Toast.LENGTH_SHORT).show()
        }

    }
    class PortadaAdapter(private val listas: List<Portada>) :
        RecyclerView.Adapter<PortadaAdapter.PortadaViewHolder>() {

        private lateinit var context: Context

        inner class PortadaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnCreateContextMenuListener {
            val imagenPortada: ImageView = itemView.findViewById(R.id.iv_portada)
            private var listaNombre: String = ""

            init {
                itemView.setOnCreateContextMenuListener(this)
            }

            fun setListaNombre(nombre: String) {
                listaNombre = nombre
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                menu?.setHeaderTitle(listaNombre) // Establecer el nombre de la lista como encabezado
                // Resto del código...

                //menu?.setHeaderTitle(getListaNombre()) // Establecer el nombre de la lista como encabezado
                //menu?.setHeaderTitle("Nombre de la lista")
                val inflater = MenuInflater(context)
                inflater.inflate(R.menu.menu_portada, menu)
                setMenuBackground()
                menu?.findItem(R.id.opcion_cambiarNombre)?.setOnMenuItemClickListener {
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    alertDialogBuilder.setTitle("Cambiar nombre de la lista")
                    alertDialogBuilder.setMessage("Ingresa el nuevo nombre para la lista")

                    val inputEditText = EditText(itemView.context)
                    alertDialogBuilder.setView(inputEditText)

                    alertDialogBuilder.setPositiveButton("Aceptar") { dialog, _ ->
                        val nuevoNombre = inputEditText.text.toString()


                        val position = adapterPosition

                        // Obtener el nombre de la lista que se va a cambiar
                        val nombreLista = listas[position].nombre

                        // Actualizar el nombre en la base de datos
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference("listas")

                        // Consultar y actualizar el registro con el nombre de la lista
                        databaseReference.orderByChild("nombre").equalTo(nombreLista).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (registroSnapshot in dataSnapshot.children) {
                                    // Obtener la referencia al nodo específico que contiene el nombre
                                    val nombreRef = registroSnapshot.child("nombre")

                                    // Cambiar el valor del nombre
                                    nombreRef.ref.setValue(nuevoNombre)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Manejar el error si lo hay
                            }
                        })

                        dialog.dismiss()
                    }

                    alertDialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    true
                }


                menu?.findItem(R.id.opcion_eliminar)?.setOnMenuItemClickListener {
                    // Lógica para la opción "Eliminar"
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    alertDialogBuilder.setTitle("Eliminar lista")
                    alertDialogBuilder.setMessage("¿Estás seguro de que deseas eliminar esta lista?")
                    alertDialogBuilder.setPositiveButton("Sí") { dialog, _ ->
                        // Obtener la posición del elemento en el RecyclerView
                        val position = adapterPosition

                        // Obtener el nombre de la lista que se va a eliminar
                        val nombreLista = listas[position].nombre

                        // Eliminar el registro de la base de datos
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference("listas")

                        // Consultar y eliminar el registro con el nombre de la lista
                        databaseReference.orderByChild("nombre").equalTo(nombreLista).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (registroSnapshot in dataSnapshot.children) {
                                    registroSnapshot.ref.removeValue()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Manejar el error si lo hay
                            }
                        })

                        dialog.dismiss()
                    }
                    alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    true
                }
                menu?.findItem(R.id.opcion_favoritos)?.setOnMenuItemClickListener {
                    // Lógica para la opción "Eliminar"
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    alertDialogBuilder.setTitle("Agregar a favoritas")
                    alertDialogBuilder.setMessage("¿Estás seguro de que deseas agregar esta lista a favoritos?")
                    alertDialogBuilder.setPositiveButton("Sí") { dialog, _ ->
                        // Obtener la posición del elemento en el RecyclerView
                        val position = adapterPosition

                        // Obtener el nombre de la lista que se va a eliminar
                        val nombreLista = listas[position].nombre

                        // Eliminar el registro de la base de datos
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference("listas")

                        // Consultar y eliminar el registro con el nombre de la lista
                        databaseReference.orderByChild("nombre").equalTo(nombreLista).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (registroSnapshot in dataSnapshot.children) {
                                    val favorita = registroSnapshot.ref.child("favorita")
                                    favorita.ref.setValue(true)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Manejar el error si lo hay
                            }
                        })

                        dialog.dismiss()
                    }
                    alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()

                    true
                }

                menu?.findItem(R.id.opcion_mostrarContenido)?.setOnMenuItemClickListener {
                    // Lógica para la opción "Eliminar"


                        // Obtener la posición del elemento en el RecyclerView
                        val position = adapterPosition

                        // Obtener el nombre de la lista que se va a eliminar
                        val nombreLista = listas[position].nombre

                        // Eliminar el registro de la base de datos
                        val database = FirebaseDatabase.getInstance()
                        val databaseReference = database.getReference("listas")

                    databaseReference.orderByChild("nombre").equalTo(nombreLista).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (registroSnapshot in dataSnapshot.children) {
                                // Obtener los datos de la lista que coincida con el nombre
                                val lista: Lista? = registroSnapshot.getValue(Lista::class.java)


                                if (lista != null) {

                                    val nombre = lista.nombre
                                    val categoria= lista.categoria
                                    val contenidos = lista.contenidos
                                    val favorita =lista.favorita
                                    val icono =lista.icono
                                    val usuario=lista.usuario
                                    val idUsuario=lista.idUsuario
                                     val tipo= lista.tipo


                                    val intent = Intent(context, ListasFav2Activity::class.java)
                                    intent.putExtra("nombre", nombre)
                                    intent.putExtra("categoria", categoria)
                                    intent.putParcelableArrayListExtra("contenidos", contenidos)
                                    intent.putExtra("favorita", favorita)
                                    intent.putExtra("icono", icono)
                                    intent.putExtra("usuario", usuario)
                                    intent.putExtra("idUsuario", idUsuario)
                                    intent.putExtra("tipo", tipo)
                                    context.startActivity(intent)

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Manejar el error si lo hay
                        }
                    })


                    true
                }


            }

            private fun setMenuBackground() {
                //Sin funcionar
                // val container = itemView.parent as View
                // container.setBackgroundResource(R.drawable.background_menu)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortadaViewHolder {
            context = parent.context
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_listas, parent, false)
            return PortadaViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: PortadaViewHolder, position: Int) {
            val lista = listas[position]
            holder.imagenPortada.setImageResource(lista.Imagen)
            holder.setListaNombre(lista.nombre)

        }

        override fun getItemCount(): Int {
            return listas.size
        }
    }
}





