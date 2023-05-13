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
 * Use the [MenuPersonalTelefono.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPersonalTelefono : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_menu_personal_telefono, container, false)

        val txtTelefonoActual = view.findViewById<EditText>(R.id.etTelefonoActual)
        val txtTelefonoNuevo = view.findViewById<EditText>(R.id.etTelefonoNuevo)
        val btnModificarTelefono = view.findViewById<Button>(R.id.buttonModificarTelefono)

        btnModificarTelefono.setOnClickListener {
            val telefonoActual = txtTelefonoActual.text.toString()
            val telefonoNuevo = txtTelefonoNuevo.text.toString()

            modificarTelefono(telefonoActual, telefonoNuevo)
        }

        return view
    }

    private fun modificarTelefono(telefonoActual: String, telefonoNuevo: String) {
        if (telefonoActual.isEmpty() || telefonoNuevo.isEmpty()) {
            Toast.makeText(requireContext(), "¡Debes llenar todos los campos!", Toast.LENGTH_SHORT).show()
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
                        if (usuario?.telefono == telefonoActual) {
                            usuariosRef.child(usuarioKey!!).child("telefono").setValue(telefonoNuevo)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // El teléfono se actualizó correctamente
                                        Toast.makeText(requireContext(), "¡Teléfono modificado!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Manejar el caso de error al actualizar el teléfono
                                        Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            return
                        }
                    }

                    // Manejar el caso de teléfono actual no encontrado en la base de datos
                    Toast.makeText(requireContext(), "¡El teléfono actual que has ingresado es incorrecto!", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el caso de error en la consulta de la base de datos
                    Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
                }
            })
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
            MenuPersonalTelefono().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
