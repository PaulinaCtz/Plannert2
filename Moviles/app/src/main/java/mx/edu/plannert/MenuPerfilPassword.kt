package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPerfilPassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPerfilPassword : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_menu_perfil_password, container, false)

        val txtContraseñaActual = view.findViewById<EditText>(R.id.etContraseñaActual)
        val txtContraseñaNueva = view.findViewById<EditText>(R.id.etContraseñaNueva)
        val txtConfirmarContraseña = view.findViewById<EditText>(R.id.etConfirmarContraseña)
        val btnModificarContraseña = view.findViewById<Button>(R.id.buttonCambiarContraseña)

        btnModificarContraseña.setOnClickListener {
            val contraseñaActual = txtContraseñaActual.text.toString()
            val contraseñaNueva = txtContraseñaNueva.text.toString()
            val confirmarContraseña = txtConfirmarContraseña.text.toString()

            modificarContraseña(contraseñaActual, contraseñaNueva, confirmarContraseña)
        }

        return view
    }

    private fun modificarContraseña(
        contraseñaActual: String,
        contraseñaNueva: String,
        confirmarContraseña: String
    ) {
        if (contraseñaActual.isEmpty() || contraseñaNueva.isEmpty() || confirmarContraseña.isEmpty()) {
            Toast.makeText(requireContext(), "¡Debes llenar todos los campos!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (contraseñaNueva != confirmarContraseña) {
            Toast.makeText(requireContext(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val email = currentUser.email

            val credential = email?.let { EmailAuthProvider.getCredential(it, contraseñaActual) }

            if (credential != null) {
                currentUser.reauthenticate(credential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            currentUser.updatePassword(contraseñaNueva)
                                .addOnCompleteListener { updatePasswordTask ->
                                    if (updatePasswordTask.isSuccessful) {
                                        // Contraseña actualizada correctamente en la autenticación
                                        Toast.makeText(
                                            requireContext(),
                                            "¡Contraseña modificada correctamente!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Actualizar contraseña en la base de datos en tiempo real
                                        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                                        val usuariosRef: DatabaseReference =
                                            database.getReference("usuarios")

                                        usuariosRef.orderByChild("email").equalTo(email)
                                            .addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                    for (usuarioSnapshot in dataSnapshot.children) {
                                                        val usuarioKey = usuarioSnapshot.key
                                                        usuariosRef.child(usuarioKey!!)
                                                            .child("contraseña")
                                                            .setValue(contraseñaNueva)
                                                            .addOnCompleteListener { updateDatabaseTask ->
                                                                if (updateDatabaseTask.isSuccessful) {
                                                                    // Contraseña actualizada correctamente en la base de datos
                                                                    Toast.makeText(
                                                                        requireContext(),
                                                                        "¡Contraseña modificada correctamente!",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                } else {
                                                                    // Manejar el caso de error al actualizar la contraseña en la base de datos
                                                                    Toast.makeText(
                                                                        requireContext(),
                                                                        "¡Ha ocurrido un error al modificar la contraseña en la base de datos!",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }
                                                        return
                                                    }

                                                    // Manejar el caso de correo no encontrado en la base de datos
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "¡No se encontró el correo en la base de datos!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {
                                                    // Manejar el caso de error en la consulta de la base de datos
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "¡Ha ocurrido un error al consultar la base de datos!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            })
                                    } else {
                                        // Manejar el caso de error al actualizar la contraseña en la autenticación
                                        Toast.makeText(
                                            requireContext(),
                                            "¡Ha ocurrido un error al modificar la contraseña en la autenticación!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            // Manejar el caso de error de autenticación
                            Toast.makeText(
                                requireContext(),
                                "¡La contraseña actual ingresada es incorrecta!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        } else {
            // Manejar el caso de usuario no autenticado
            Toast.makeText(requireContext(), "¡No se ha iniciado sesión!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPerfilUsuario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
