package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPersonal.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPersonal : Fragment() {
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
        //return inflater.inflate(R.layout.fragment_menu_perfil, container, false)
        val view = inflater.inflate(R.layout.fragment_menu_personal, container, false)
        val tolbar = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerViewTolBar) as TolBarMenu


        val boton = view.findViewById<Button>(R.id.personalNombre)
        //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonalNombre()
            tolbar.actualizarTexto("Menu / Personal / Nombre")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val boton2 = view.findViewById<Button>(R.id.personalEdad)
        //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton2.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonalEdad()
            tolbar.actualizarTexto("Menu / Personal / Edad")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val boton3 = view.findViewById<Button>(R.id.personalGenero)
        //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton3.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonalGenero()
            tolbar.actualizarTexto("Menu / Personal / Género")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val boton4 = view.findViewById<Button>(R.id.personalNumeroTelefono)
        //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton4.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonalTelefono()
            tolbar.actualizarTexto("Menu / Personal / Teléfono")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val boton5 = view.findViewById<Button>(R.id.personalPlataformas)
        //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton5.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonalPlataformas()
            tolbar.actualizarTexto("Menu / Personal / Plataformas")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuPersonal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPersonal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}