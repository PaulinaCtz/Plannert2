package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPersonalGenero.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPersonalGenero : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_menu_personal_genero, container, false)

        val btnCambiarGenero = view.findViewById<Button>(R.id.buttonCambiarGenero)
        val radioGroupGenero = view.findViewById<RadioGroup>(R.id.radioGroupGenero)
        val radioButtonFemenino = view.findViewById<RadioButton>(R.id.radioButtonFemenino)
        val radioButtonMasculino = view.findViewById<RadioButton>(R.id.radioButtonMasculino)
        val radioButtonOtro = view.findViewById<RadioButton>(R.id.radioButtonOtro)
        val etOtroGenero = view.findViewById<EditText>(R.id.etOtroGenero)

        radioGroupGenero.check(radioButtonOtro.id)

        btnCambiarGenero.setOnClickListener {
            val selectedId = radioGroupGenero.checkedRadioButtonId

            if (selectedId == -1) {
                Toast.makeText(requireContext(), "¡Debes seleccionar un género!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val genero = when (selectedId) {
                radioButtonFemenino.id -> "Femenino"
                radioButtonMasculino.id -> "Masculino"
                radioButtonOtro.id -> etOtroGenero.text.toString().trim()
                else -> ""
            }

            if (genero.isEmpty()) {
                Toast.makeText(requireContext(), "¡Debes ingresar un género personalizado!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí puedes agregar el código para modificar el género en la base de datos
            // Utiliza la variable 'genero' para actualizar el valor correspondiente

            // Ejemplo de código para actualizar el género en Realtime Database
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usuariosRef: DatabaseReference = database.getReference("usuarios")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            val currentUser = auth.currentUser
            if (currentUser != null) {
                val email = currentUser.email

                usuariosRef.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (usuarioSnapshot in dataSnapshot.children) {
                                val usuarioKey = usuarioSnapshot.key
                                usuariosRef.child(usuarioKey!!).child("genero").setValue(genero)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // El género se actualizó correctamente
                                            Toast.makeText(
                                                requireContext(),
                                                "¡Género modificado!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Manejar el caso de error al actualizar el género
                                            Toast.makeText(
                                                requireContext(),
                                                "¡Ha ocurrido un error!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                return
                            }

                            // Manejar el caso de email no encontrado en la base de datos
                            Toast.makeText(
                                requireContext(),
                                "¡El email actual que has ingresado es incorrecto!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Manejar el caso de error en la consulta de la base de datos
                            Toast.makeText(
                                requireContext(),
                                "¡Ha ocurrido un error en la consulta de la base de datos!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
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
         * @return A new instance of fragment MenuPersonalGenero.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPersonalGenero().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
