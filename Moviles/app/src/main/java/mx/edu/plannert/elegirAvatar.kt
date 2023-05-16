package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [elegirAvatar.newInstance] factory method to
 * create an instance of this fragment.
 */
class elegirAvatar : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_elegir_avatar, container, false)

        val radioGroup = rootView.findViewById<RadioGroup>(R.id.radioGroupIconos)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            val selectedOption = radioButton.tag.toString()
            // Aquí puedes hacer lo que necesites con la opción seleccionada
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userEmail = currentUser?.email

            if (userEmail != null) {
                val database = FirebaseDatabase.getInstance()
                val usuariosRef = database.getReference("usuarios")

                usuariosRef.orderByChild("email").equalTo(userEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val usuarioActualRef = snapshot.ref

                                val interesesContenidoMap = HashMap<String, Any>()

                                    interesesContenidoMap["Avatar"] =selectedOption

                                usuarioActualRef.updateChildren(
                                    interesesContenidoMap
                                )
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Elementos guardado exitosamente",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            context,
                                            "Error al guardar el elemento",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Toast.makeText(
                                context,
                                "Error al buscar el usuario",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }

        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment elegirAvatar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            elegirAvatar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}