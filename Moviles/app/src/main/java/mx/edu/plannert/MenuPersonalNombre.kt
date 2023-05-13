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
 * Use the [MenuPersonalNombre.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPersonalNombre : Fragment() {

    private var nombre: String? = null
    private var apellido: String? = null
    private var apodo: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu_personal_nombre, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCambiarNombre = view.findViewById<Button>(R.id.btnCambiarNombre)
        val etNombre = view.findViewById<EditText>(R.id.etNombre)
        val etApellido = view.findViewById<EditText>(R.id.etApellido)
        val etApodo = view.findViewById<EditText>(R.id.etApodo)

        btnCambiarNombre.setOnClickListener {
            nombre = etNombre.text.toString().trim()
            apellido = etApellido.text.toString().trim()
            apodo = etApodo.text.toString().trim()

            if (nombre.isNullOrEmpty() || apellido.isNullOrEmpty() || apodo.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "¡Debes completar todos los campos!", Toast.LENGTH_SHORT).show()
            } else {
                guardarInformacionPersonal()
            }
        }
    }

    private fun guardarInformacionPersonal() {
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

                        if (usuarioKey != null) {
                            val nombreCompleto = "$nombre $apellido"

                            val nuevoUsuario = HashMap<String, Any>()
                            nuevoUsuario["nombre"] = nombreCompleto
                            nuevoUsuario["apodo"] = apodo!!

                            usuariosRef.child(usuarioKey).updateChildren(nuevoUsuario)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Los datos se guardaron correctamente
                                        Toast.makeText(requireContext(), "¡Datos guardados!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        // Manejar el caso de error al guardar los datos
                                        Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            return
                        }
                    }

                    // Manejar el caso de email no encontrado en la base de datos
                    Toast.makeText(requireContext(), "¡El email actual que has ingresado es incorrecto!", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el caso de error en la consulta de la base de datos
                    Toast.makeText(requireContext(), "¡Ha ocurrido un error en la consulta de la base de datos!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
