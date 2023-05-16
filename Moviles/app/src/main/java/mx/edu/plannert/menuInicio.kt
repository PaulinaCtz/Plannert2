package mx.edu.plannert


import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent

import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [menuInicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class menuInicio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var client: GoogleSignInClient
    val options = GoogleSignInOptions.Builder().requestEmail().build()

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
        val view=inflater.inflate(R.layout.fragment_menu_inicio, container, false)
        val botonBuscar = view.findViewById<ImageView>(R.id.iconoBuscar)
        val botonListas = view.findViewById<ImageView>(R.id.iconoListas)
        val botonNueva = view.findViewById<Button>(R.id.nuevaLista)
        val botonInicio = view.findViewById<ImageView>(R.id.iconoInicio)
        val botonCerrar = view.findViewById<ImageView>(R.id.cerrar)
        val options = GoogleSignInOptions.Builder().requestEmail().build()
        client = GoogleSignIn.getClient(this.requireActivity(),options)

        botonCerrar.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            client.signOut()
            val i: Intent = Intent(activity, MainActivity::class.java)
            startActivity(i)
        }
        botonBuscar.setOnClickListener{
            val intent = Intent(activity, busqueda::class.java)

            startActivity(intent)
        }

        botonInicio.setOnClickListener{
            val intent = Intent(activity, Inicio::class.java)

            startActivity(intent)
        }

        botonListas.setOnClickListener {
            val intent = Intent(activity, MisListas::class.java)
            startActivity(intent)
        }

        botonNueva.setOnClickListener {
            val intent = Intent(activity, ListasNuevas::class.java)
            startActivity(intent)
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
         * @return A new instance of fragment menuInicio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            menuInicio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}