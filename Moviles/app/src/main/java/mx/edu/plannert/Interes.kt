package mx.edu.plannert

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Interes.newInstance] factory method to
 * create an instance of this fragment.
 */
class Interes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  lateinit var boton1: RadioButton
    private  lateinit var boton2: RadioButton
    private  lateinit var boton3: RadioButton
    private  lateinit var boton4: RadioButton

    private lateinit var subtitulo: TextView
    private lateinit var titulo: TextView


    private lateinit var gridView: GridView

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
        val view = inflater.inflate(R.layout.fragment_interes, container, false)


        val imagenes = arguments?.getParcelableArrayList<DetallesPeliculas>("imagenes")
        val sub = arguments?.getString("subtitulo")
        val ocultar= arguments?.getBoolean("ocultar")
        val busqueda= arguments?.getBoolean("busqueda")

        val descripcionLista = arguments?.getString("descripcionLista")
        val ocultarBotones= arguments?.getBoolean("ocultarBotones")
        var title=arguments?.getString("titulo")





            val gridView = view.findViewById<GridView>(R.id.grid_interes)
            val mensaje = view.findViewById<TextView>(R.id.txtMensaje)
        subtitulo = view.findViewById(R.id.txtSubtiuloInteres)
        titulo=view.findViewById(R.id.txtMensaje)
        boton1 = view.findViewById(R.id.btn1)
        boton2 = view.findViewById(R.id.btn2)
        boton3 = view.findViewById(R.id.btn3)
        boton4 = view.findViewById(R.id.btn4)

            subtitulo.setText(sub)
            if (ocultar != null) {
                if (ocultar == true) {
                    boton1.visibility = View.INVISIBLE
                    boton2.visibility = View.INVISIBLE
                    boton3.visibility = View.INVISIBLE
                    boton4.visibility = View.INVISIBLE
                    mensaje.visibility = View.INVISIBLE

                }
            }

        if(ocultarBotones==true){
            boton1.visibility = View.INVISIBLE
            boton2.visibility = View.INVISIBLE
            boton3.visibility = View.INVISIBLE
            boton4.visibility = View.INVISIBLE

            subtitulo.setText(descripcionLista)
            titulo.setText(title)
        }

        if (busqueda != null) {
            if (busqueda == true) {
                subtitulo.setText("Titulos populares")
                titulo.setText("Categorias")
                boton1.setBackgroundResource(R.drawable.opcionesmenu)
                boton1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                boton1.setText("Terror")
                boton1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                boton2.setBackgroundResource(R.drawable.opcionesmenurosa)
                boton2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                boton2.setText("Romance")
                boton2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                boton3.setBackgroundResource(R.drawable.opcionesmenuamarillo)
                boton3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                boton3.setText("Acción")
                boton3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)

                boton4.setBackgroundResource(R.drawable.opcionesmenuazul)
                boton4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                boton4.setText("Sci -Fi")
                boton4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)



            }
        }



        if (sub == "Peliculas") {
                mensaje.setText("Selecciona 5 intereses")
            } else if (sub == "Plataformas") {
                mensaje.setText("¿Qué plataformas sueles utilizar?")
            }

        if(imagenes!=null ) {


            if(busqueda==true) {

                //val adapter = ImageAdapter(requireContext(), imagenes as ArrayList<Contenidos>, true)
                val adapter = PeliculaAdapter(requireContext(), imagenes as ArrayList<DetallesPeliculas>, true,"no")
                gridView.adapter = adapter
            }else{

                Toast.makeText(context, "Elemento guardado exitosamente", Toast.LENGTH_SHORT).show()

                if (sub == "Peliculas") {
                    val adapter = PeliculaAdapter(requireContext(),imagenes,false,"Peliculas")
                    gridView.adapter = adapter
                } else if (sub == "Plataformas") {
                    val adapter = PeliculaAdapter(requireContext(), imagenes,false,"Plataformas")
                    gridView.adapter = adapter}
            }
               // val adapter = PeliculaAdapter(requireContext(), imagenes as ArrayList<DetallesPeliculas>, false,"no")
               // gridView.adapter = adapter


        }else{
            /*
            val imagenes2 = arrayListOf(
                Contenidos(R.drawable.prodigy,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.alien,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.ironman,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.shanchi,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.quantumania,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.lightyear,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.shrek,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.elvis,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.fightclub,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.tres,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.blackswan,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.hollywood,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),

            )*/

            val database = Firebase.database
            val detalleContenidoRef = database.reference.child("detalleContenido")

            val listaDetalleContenido = ArrayList<DetallesPeliculas>()

            detalleContenidoRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Obtener los datos de los registros en detalleContenido
                    listaDetalleContenido.clear()
                    for (registroSnapshot in dataSnapshot.children) {
                        var registro = registroSnapshot.getValue(DetallesPeliculas::class.java)
                        println(registro)
                        listaDetalleContenido.add(registro!!)
                        println("")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores
                }
            })


            if(busqueda==true){
                val adapter = PeliculaAdapter(requireContext(), listaDetalleContenido,true,"no")
                gridView.adapter = adapter
            }else{
                if (sub == "Peliculas") {
                    val adapter = PeliculaAdapter(requireContext(), listaDetalleContenido,false,"Peliculas")
                    gridView.adapter = adapter
                } else if (sub == "Plataformas") {
                    val adapter = PeliculaAdapter(requireContext(), listaDetalleContenido,false,"Plataformas")
                      gridView.adapter = adapter}
                }


        }






            return view



    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView = view.findViewById(R.id.grid_interes)
        subtitulo = view.findViewById(R.id.txtSubtiuloInteres)
        boton1 = view.findViewById(R.id.btn1)
        boton2 = view.findViewById(R.id.btn2)
        boton3 = view.findViewById(R.id.btn3)
        boton4 = view.findViewById(R.id.btn4)


    }

    fun setSubtitulo(subt: String) {


        subtitulo.text=subt

    }

    fun setTitulo(title: String) {


        titulo.setText(title)

    }
    fun setBackgroundABusqueda(idDrawableTeror: Int,idDrawableRomance: Int,idDrawableAccion: Int,idDrawableSciFi: Int) {
        // Cambia el background del botón

        boton1.setBackgroundResource(idDrawableTeror)
        boton1.setText("Terror")
        boton1.invalidate()
        boton2.setBackgroundResource(idDrawableRomance)
        boton2.setText("Romance")
        boton3.setBackgroundResource(idDrawableAccion)
        boton3.setText("Accion")
        boton4.setBackgroundResource(idDrawableSciFi)
        boton4.setText("Sci -Fi")

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Interes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Interes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }



        fun newInstance(imagenes: ArrayList<DetallesPeliculas>,subtitulo:String,ocultar: Boolean): Interes {
            val fragment = Interes()
            val args = Bundle()
            args.putParcelableArrayList("imagenes",imagenes)
            args.putString("subtitulo",subtitulo)
            args.putBoolean("ocultar",ocultar)
            fragment.arguments = args
            return fragment
        }

        /*
        fun newInstance(images: ArrayList<Contenidos>,subtitulo:String): Interes {
            val fragment = Interes()
            val args = Bundle()
            args.putParcelableArrayList("imagenes",images)
            args.putString("subtitulo",subtitulo)
            fragment.arguments = args
            return fragment
        }*/

        fun newInstance(images: ArrayList<DetallesPeliculas>,subtitulo:String): Interes {
            val fragment = Interes()
            val args = Bundle()
            args.putParcelableArrayList("imagenes",images)
            args.putString("subtitulo",subtitulo)
            fragment.arguments = args
            return fragment
        }



        fun newInstance(imagenes: ArrayList<DetallesPeliculas>): Interes {
            val fragment = Interes()
            val args = Bundle()
            args.putParcelableArrayList("imagenes",imagenes)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(imagenes: ArrayList<DetallesPeliculas>,busqueda: Boolean): Interes {
            val fragment = Interes()
            val args = Bundle()

            args.putParcelableArrayList("imagenes",imagenes)
            args.putBoolean("busqueda",busqueda)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(imagenes: ArrayList<DetallesPeliculas>,ocultarBotones: Boolean,descripcion:String,titulo:String): Interes {
            val fragment = Interes()
            val args = Bundle()

            args.putParcelableArrayList("imagenes",imagenes)
            args.putString("descripcionLista",descripcion)
            args.putString("titulo",titulo)
            args.putBoolean("ocultarBotones",ocultarBotones)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(imagenes: ArrayList<DetallesPeliculas>,ocultarBotones: Boolean,descripcion:String,titulo:String,busqueda:Boolean): Interes {
            val fragment = Interes()
            val args = Bundle()

            args.putParcelableArrayList("imagenes",imagenes)
            args.putString("descripcionLista",descripcion)
            args.putString("titulo",titulo)
            args.putBoolean("busqueda",busqueda)
            args.putBoolean("ocultarBotones",ocultarBotones)
            fragment.arguments = args
            return fragment
        }
    }


}