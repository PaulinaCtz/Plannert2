package mx.edu.plannert


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class PeliculaAdapter(private val context: Context, private val contenidos: ArrayList<DetallesPeliculas>,private val busqueda:Boolean,private val tipo:String) : BaseAdapter() {
    val elementosSeleccionados = mutableListOf<DetallesPeliculas>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
       // imageView.setImageResource(contenidos[position].imagen)
        Picasso.get().load(contenidos[position].urlImagen).into(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(365, 490)


        // para mostrar el detalle :)
        imageView.setOnClickListener {
            // verificica si se esta usando en busqueda para ver si se activa la funcionalidad del clickOnListener de los elementos del gridview
            if (busqueda == true) {
                val activity = context as busqueda


                val detalleFragment = detalleContenido.newInstance(contenidos[position])


                val intent = Intent(context, DetalleContenidoDetalle::class.java)
                intent.putExtra("titulo", contenidos[position].titulo)
                intent.putExtra("descripcion", contenidos[position].descripcion)
                intent.putExtra("urlImagen", contenidos[position].urlImagen)
                intent.putExtra("categoria", contenidos[position].categoria)
                intent.putExtra("fecha", contenidos[position].fecha)
                intent.putExtra("nombreImagen", contenidos[position].nombreImagen)
                intent.putExtra("tipo", contenidos[position].tipo)

                context.startActivity(intent)


                /*
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewBusquedaTolbar, toolBarIcono())
                    .commit()

                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewElementosBusqueda, detalleFragment)
                    .commit()*/


            } else {
                if (tipo == "Peliculas" || tipo == "Plataformas") {
                    //AQUI VA lo de reigstrar en usuario
                    // Verificar si el elemento ya está seleccionado
// Verificar si el elemento ya está seleccionado
                    if (elementosSeleccionados.size < 5) {
                        elementosSeleccionados.add(contenidos[position])
                        imageView.isEnabled = false
                        imageView.setImageResource(R.drawable.cuadradonoseleccionado)

                        // Verificar si se han seleccionado exactamente 5 elementos
                        if (elementosSeleccionados.size == 5) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val userEmail = currentUser?.email

                            if (userEmail != null) {
                                val database = FirebaseDatabase.getInstance()
                                val usuariosRef = database.getReference("usuarios")

                                usuariosRef.orderByChild("email").equalTo(userEmail)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            for (snapshot in dataSnapshot.children) {
                                                val usuarioActualRef = snapshot.ref

                                                val interesesContenidoMap = HashMap<String, Any>()
                                                if (tipo == "Peliculas") {
                                                    interesesContenidoMap["interesesContenido"] =
                                                        elementosSeleccionados
                                                } else if (tipo == "Plataformas") {
                                                    interesesContenidoMap["interesesPlataformas"] =
                                                        elementosSeleccionados
                                                }

                                                usuarioActualRef.updateChildren(
                                                    interesesContenidoMap
                                                )
                                                    .addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Elementos guardado exitosamente",
                                                            Toast.LENGTH_SHORT
                                                        ).show()


                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            context,
                                                            "Error al guardar el elemento",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            Toast.makeText(
                                                context,
                                                "Error al buscar el usuario",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }
                        }
                    } else {
                        // Si se han seleccionado más de 5 elementos, mostrar un Toast o cualquier mensaje indicando que solo se pueden seleccionar 5 elementos
                        Toast.makeText(
                            context,
                            "Solo puedes seleccionar hasta 5 elementos",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Quitar el elemento seleccionado de la lista
                    }


                }
            }
        }

        return imageView



    }

    override fun getItem(position: Int): Any {
        return contenidos.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return contenidos.size
    }


}


