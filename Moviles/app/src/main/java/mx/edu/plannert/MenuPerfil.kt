package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPerfil.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPerfil : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_menu_perfil, container, false)
        val tolbar = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerViewTolBar) as TolBarMenu


        val boton = view.findViewById<Button>(R.id.perfilPersonal)
        val botonUsuario = view.findViewById<Button>(R.id.perfilUsuario)
        val botonEmail = view.findViewById<Button>(R.id.perfilCorreo)
        val botonPassword = view.findViewById<Button>(R.id.perfiContraseña)
        val botonCuenta = view.findViewById<Button>(R.id.perfilEliminarCuenta)

        val icono = view.findViewById<ImageView>(R.id.iv_iconoPerfil)
        val nombreUsuario = view.findViewById<TextView>(R.id.tv_UsuarioPerfil)

        obtenerUsuarioActual { usuario ->
            if(usuario!=null) {
                if (usuario != null) {
                    if(usuario.Avatar=="uno"){

                        icono.setBackgroundResource(R.drawable.icono1)

                    }else if(usuario.Avatar=="dos"){

                        icono.setBackgroundResource(R.drawable.icono2)

                    }else if(usuario.Avatar=="tres"){

                        icono.setBackgroundResource(mx.edu.plannert.R.drawable.icono3)

                    }else if(usuario.Avatar=="cuatro"){

                        icono.setBackgroundResource(R.drawable.icono4)

                    }
                    nombreUsuario.setText(usuario.usuario)
                } else {
                    // Aquí manejas el caso en el que no se encontró el usuario o no hay un usuario autenticado
                    println("No se encontró el usuario o no hay un usuario autenticado.")
                }
            }
        }
     //   val etiquetaTolbar=view.findViewById<TextView>(R.id.etiquetaNavegacion)
        boton.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPersonal()
    tolbar.actualizarTexto("Menu / Personal")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        botonUsuario.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPerfilUsuario()
            tolbar.actualizarTexto("Perfil / Usuario")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        botonEmail.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPerfilEmail()
            tolbar.actualizarTexto("Perfil / Correo")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        botonPassword.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPerfilPassword()
            tolbar.actualizarTexto("Perfil / Contraseña")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        botonCuenta.setOnClickListener {
            // Reemplaza el fragmento actual por el nuevo fragmento
            val nuevoFragmento = MenuPerfilCuenta()
            tolbar.actualizarTexto("Perfil / Eliminar cuenta")

            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerViewCuerpo, nuevoFragmento)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        return view
    }

    fun obtenerUsuarioActual(callback: (Usuarios?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usuariosRef: DatabaseReference= database.getReference("usuarios")

            val usuarioReferencia = usuariosRef.orderByChild("email").equalTo(currentUser.email)

            usuarioReferencia.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usuarioSnapshot = dataSnapshot.children.firstOrNull()

                    val usuario = usuarioSnapshot?.getValue(Usuarios::class.java)
                    callback(usuario)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MenuPerfil.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPerfil().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}