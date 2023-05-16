package mx.edu.plannert

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ListasFav2Activity : AppCompatActivity() {
    private lateinit var adapter: ContenidosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas_fav2)

        var gridview: GridView = findViewById(R.id.grid_titulos)
        val nombre = intent.getStringExtra("nombre")
        val categoria = intent.getStringExtra("categoria")

        val contenidos = intent.getParcelableArrayListExtra<DetallesPeliculas>("contenidos")
        val favorita = intent.getBooleanExtra("favorita", false)
        val icono = intent.getIntExtra("icono", 0)
        val usuario = intent.getStringExtra("usuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val tipo = intent.getStringExtra("tipo")

        val tv_TituloLista = findViewById<TextView>(R.id.tv_tituloLista)
        tv_TituloLista.text = nombre

        adapter = contenidos?.let { ContenidosAdapter(this, it) }!!
        gridview.adapter = adapter

        gridview.setOnItemClickListener { parent, view, position, id ->
            val item = contenidos?.get(position)
            item?.let {
                val intent = Intent(this, DetalleContenidoDetalle::class.java)
                intent.putExtra("titulo", it.titulo)
                intent.putExtra("descripcion", it.descripcion)
                intent.putExtra("urlImagen", it.urlImagen)
                intent.putExtra("categoria", it.categoria)
                intent.putExtra("fecha", it.fecha)
                intent.putExtra("nombreImagen", it.nombreImagen)
                intent.putExtra("tipo", it.tipo)
                startActivity(intent)
            }
        }

        registerForContextMenu(gridview)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contenidos, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.opcion_eliminarContenidos -> {
                val adapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val selectedPosition = adapterContextMenuInfo.position
                val nombreContenido = adapter.getNombreContenido(selectedPosition)

                val nombre = intent.getStringExtra("nombre")

                if (nombreContenido.isNotEmpty()) {
                    val databaseReference = FirebaseDatabase.getInstance().getReference("listas") // Reemplaza con el nombre del nodo en tu base de datos

                    val query = databaseReference.orderByChild("nombre").equalTo(nombre) // Consulta para obtener la lista con el nombre específico

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val listaId = snapshot.key // Obtén la clave de la lista
                                val listaRef = databaseReference.child(listaId!!) // Obtiene la referencia a la lista específica

                                listaRef.child("contenidos").addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(contenidoSnapshot: DataSnapshot) {
                                        for (contenidoChildSnapshot in contenidoSnapshot.children) {
                                            val contenidoId = contenidoChildSnapshot.key // Obtén el ID del contenido dentro del arreglo
                                            val contenido = contenidoChildSnapshot.getValue(Contenidos::class.java) // Obtiene el objeto Contenido

                                            // Verifica si el contenido coincide con el seleccionado
                                            if (contenido != null && contenido.titulo == nombreContenido) {
                                                listaRef.child("contenidos").child(contenidoId!!).removeValue() // Elimina el contenido del arreglo en Firebase
                                                adapter.removeContenido(selectedPosition)
                                                break // Finaliza el bucle si se encuentra y elimina el contenido
                                            }
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Toast.makeText(applicationContext, "Error al eliminar el contenido", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                            Toast.makeText(applicationContext, "Contenido eliminado", Toast.LENGTH_SHORT).show()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(applicationContext, "Error al eliminar el contenido", Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "No se ha seleccionado ningún contenido", Toast.LENGTH_SHORT).show()
                }

                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

class ContenidosAdapter(private val context: Context, private var contenidos: ArrayList<DetallesPeliculas>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        Picasso.get().load(contenidos[position].urlImagen).into(imageView)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(570, 770)
        return imageView
    }

    override fun getItem(position: Int): Any {
        return contenidos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return contenidos.size
    }

    fun getNombreContenido(position: Int): String {
        return contenidos[position].titulo
    }

    fun removeContenido(position: Int) {
        contenidos.removeAt(position)
        notifyDataSetChanged()
    }
}




