package mx.edu.plannert

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [toolBarBusqueda.newInstance] factory method to
 * create an instance of this fragment.
 */
class toolBarBusqueda : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tool_bar_busqueda, container, false)
        val busqueda = rootView.findViewById<EditText>(R.id.inputBusqueda)
        val buscar = rootView.findViewById<ImageView>(R.id.botonBuscar)


        buscar.setOnClickListener {

            consultarDetalleContenidoPorTitulo(busqueda.text.toString().trim()) { contenidos ->
                // Aquí puedes utilizar los resultados obtenidos
               //p Toast.makeText(context, busqueda.text, Toast.LENGTH_SHORT).show()
                busqueda.setText("")

                if(contenidos.size>0) {
                    val intent = Intent(context, DetalleContenidoDetalle::class.java)
                    intent.putExtra("titulo", contenidos.get(0).titulo)
                    intent.putExtra("descripcion", contenidos.get(0).descripcion)
                    intent.putExtra("urlImagen", contenidos.get(0).urlImagen)
                    intent.putExtra("categoria", contenidos.get(0).categoria)
                    intent.putExtra("fecha", contenidos.get(0).fecha)
                    intent.putExtra("nombreImagen", contenidos.get(0).nombreImagen)
                    intent.putExtra("tipo", contenidos.get(0).tipo)

                    context?.startActivity(intent)
                }


            }
        }

        return rootView
    }


    fun consultarDetalleContenidoPorTitulo(titulo: String, callback: (List<DetallesPeliculas>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val detalleContenidoRef = database.getReference("detalleContenido")

        detalleContenidoRef.orderByChild("titulo").equalTo(titulo)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val detalleContenidoList = mutableListOf<DetallesPeliculas>()
                    for (childSnapshot in dataSnapshot.children) {
                        val detalleContenido = childSnapshot.getValue(DetallesPeliculas::class.java)
                        detalleContenido?.let {
                            detalleContenidoList.add(it)
                        }
                    }
                    callback(detalleContenidoList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el error aquí si la consulta es cancelada
                    callback(emptyList())
                }
            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment toolBarBusqueda.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            toolBarBusqueda().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}