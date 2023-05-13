package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPerfilEmail.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPerfilEmail : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_menu_perfil_email, container, false)

        val txtEmailActual = view.findViewById<EditText>(R.id.etEmailActual)
        val txtNuevoEmail = view.findViewById<EditText>(R.id.etNuevoEmail)
        val txtConfirmarEmail = view.findViewById<EditText>(R.id.confirmarEmail)
        val btnModificarEmail = view.findViewById<Button>(R.id.buttonCambiarEmail)

        btnModificarEmail.setOnClickListener {
            val emailActual = txtEmailActual.text.toString()
            val nuevoEmail = txtNuevoEmail.text.toString()
            val confirmarEmail = txtConfirmarEmail.text.toString()

            modificarEmail(emailActual, nuevoEmail, confirmarEmail)
        }

        return view
    }

    private fun modificarEmail(emailActual: String, nuevoEmail: String, confirmarEmail: String) {
        if (emailActual.isEmpty() || nuevoEmail.isEmpty()) {
            Toast.makeText(requireContext(), "¡Debes llenar todos los campos!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (nuevoEmail != confirmarEmail) {
            Toast.makeText(requireContext(), "¡Los correos no coinciden!", Toast.LENGTH_SHORT).show()
            return
        }

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val currentEmail = currentUser.email

            if (currentEmail == emailActual) {
                // Actualizar el correo electrónico en la autenticación
                currentUser.updateEmail(nuevoEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // El correo electrónico se actualizó correctamente en la autenticación

                            // Actualizar el correo electrónico en la base de datos
                            usuariosRef.orderByChild("email").equalTo(emailActual)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        for (usuarioSnapshot in dataSnapshot.children) {
                                            val usuarioKey = usuarioSnapshot.key
                                            usuariosRef.child(usuarioKey!!).child("email")
                                                .setValue(nuevoEmail)
                                                .addOnCompleteListener { emailTask ->
                                                    if (emailTask.isSuccessful) {
                                                        // El correo electrónico se actualizó correctamente en la base de datos
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "¡Correo electrónico modificado!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        // Manejar el caso de error al actualizar el correo electrónico en la base de datos
                                                        Toast.makeText(
                                                            requireContext(),
                                                            "¡Ha ocurrido un error!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            return
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Manejar el caso de error en la consulta de la base de datos
                                        Toast.makeText(
                                            requireContext(),
                                            "¡Ha ocurrido un error!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        } else {
                            // Manejar el caso de error al actualizar el correo electrónico en la autenticación
                            Toast.makeText(
                                requireContext(),
                                "¡Ha ocurrido un error!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Manejar el caso de que el correo electrónico actual no coincida con el del usuario autenticado
                Toast.makeText(
                    requireContext(),
                    "¡El correo electrónico actual no coincide!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Manejar el caso de usuario no autenticado
            Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
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