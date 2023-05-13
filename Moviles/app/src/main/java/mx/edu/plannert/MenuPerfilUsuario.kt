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
 * Use the [MenuPerfilUsuario.newInstance] factory method to
 * create an instance of this fragment.
 */

class MenuPerfilUsuario : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var usuarioActual: Usuarios

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
        val view = inflater.inflate(R.layout.fragment_menu_perfil_usuario, container, false)
        val txtNombreActual = view.findViewById<EditText>(R.id.etNombreActual)
        val txtNombreNuevo = view.findViewById<EditText>(R.id.etNombreNuevo)
        val txtConfirmarNombre = view.findViewById<EditText>(R.id.etConfirmarNombre)
        val btnModificarNombre = view.findViewById<Button>(R.id.buttonCambiarUsuario)

        obtenerUsuarioActual { usuario ->
            if (usuario != null) {
                usuarioActual = usuario


                val nombreUsuario = usuario.usuario ?: "Nombre de usuario desconocido"

                txtNombreActual.setText(nombreUsuario)
            } else {
                // Manejar el caso en que no se encontró el usuario actual
                Toast.makeText(
                    requireContext(),
                    "¡No hay usuario actual!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }



        btnModificarNombre.setOnClickListener {
            val nombreActual = txtNombreActual.text.toString()
            val nombreNuevo = txtNombreNuevo.text.toString()
            val confirmarNombre = txtConfirmarNombre.text.toString()

          //  modificarNombre(nombreActual, nombreNuevo, confirmarNombre)

            modificarNombre(nombreActual, nombreNuevo, confirmarNombre,
                onSuccess = { nuevoUsuario ->
                    txtNombreActual.setText(nuevoUsuario)
                    txtNombreNuevo.setText("")
                    txtConfirmarNombre.setText("")
                }
            )
        }


        return view
    }

    private fun obtenerUsuarioActual(callback: (Usuarios?) -> Unit) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            consultarUsuarioPorCorreo() { usuario ->
                callback(usuario)
            }
        } else {
            callback(null)
        }
    }

    private fun consultarUsuarioPorCorreo(callback: (Usuarios?) -> Unit) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            usuariosRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (usuarioSnapshot in dataSnapshot.children) {
                            val usuarioKey = usuarioSnapshot.key
                            val usuario = usuarioSnapshot.getValue(Usuarios::class.java)
                            callback(usuario)
                            return
                        }
                        callback(null)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        callback(null)
                    }
                })
        }
    }

    private fun modificarNombre(
        nombreActual: String,
        nombreNuevo: String,
        confirmarNombre: String,
        onSuccess: (String) -> Unit
    ) {
        if (nombreActual.isEmpty() || nombreNuevo.isEmpty() || confirmarNombre.isEmpty()) {
            Toast.makeText(requireContext(), "¡Debes de llenar todo!", Toast.LENGTH_SHORT).show()
            return
        }

        if (nombreNuevo != confirmarNombre) {
            Toast.makeText(requireContext(), "¡Los usuarios no coinciden!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email

            usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usuarioSnapshot in dataSnapshot.children) {
                        val usuarioKey = usuarioSnapshot.key
                        val usuario = usuarioSnapshot.getValue(Usuarios::class.java)
                        if (usuario?.usuario == nombreActual) {
                            usuariosRef.child(usuarioKey!!).child("usuario").setValue(nombreNuevo)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // El nombre se actualizó correctamente
                                        onSuccess(nombreNuevo)
                                        Toast.makeText(
                                            requireContext(),
                                            "¡Usuario modificado!",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    } else {
                                        // Manejar el caso de error al actualizar el nombre
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

                    // Manejar el caso de nombre actual no encontrado en la base de datos
                    Toast.makeText(
                        requireContext(),
                        "¡El usuario actual que has ingresado es incorrecto!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el caso de error en la consulta de la base de datos
                    Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            // Manejar el caso de usuario no autenticado
            Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()

        }
    }
}