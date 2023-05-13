package mx.edu.plannert

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
 * Use the [nombrarAvatar.newInstance] factory method to
 * create an instance of this fragment.
 */
class nombrarAvatar : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_nombrar_avatar, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombreUsuario = view.findViewById<EditText>(R.id.etNombreUsuario)

        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val nombreUsuario = etNombreUsuario.text.toString().trim()

                if (nombreUsuario.isNotEmpty()) {
                    guardarNombreUsuario(nombreUsuario)
                } else {
                    Toast.makeText(requireContext(), "¡Debes ingresar un nombre de usuario!", Toast.LENGTH_SHORT).show()
                }

                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    private fun guardarNombreUsuario(nombreUsuario: String) {
        // Aquí puedes agregar el código para guardar el nombre en la base de datos
        // Utiliza la variable 'nombreUsuario' para guardar el valor correspondiente

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
                        usuariosRef.child(usuarioKey!!).child("usuario").setValue(nombreUsuario)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // El nombre se guardó correctamente
                                    Toast.makeText(requireContext(), "¡Nombre guardado!", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Manejar el caso de error al guardar el nombre
                                    Toast.makeText(requireContext(), "¡Ha ocurrido un error!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        return
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment nombrarAvatar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            nombrarAvatar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

