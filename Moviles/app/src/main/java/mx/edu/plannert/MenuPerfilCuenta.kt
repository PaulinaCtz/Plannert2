package mx.edu.plannert

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPerfilCuenta.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPerfilCuenta : Fragment() {
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
        return inflater.inflate(R.layout.fragment_menu_perfil_cuenta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnEliminarCuenta = view.findViewById<Button>(R.id.btnEliminarCuenta)
        btnEliminarCuenta.setOnClickListener {
            eliminarCuenta()
        }
    }

    private fun eliminarCuenta() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            val email = currentUser.email

            val confirmationDialog = AlertDialog.Builder(requireContext())
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta?")
                .setPositiveButton("Eliminar") { dialog, _ ->
                    dialog.dismiss()

                    // Eliminar usuario de la autenticación
                    currentUser.delete()
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                // Cerrar sesión y regresar al MainActivity
                                auth.signOut()
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                requireActivity().finish()

                                // Eliminar registro del usuario en Firebase Realtime Database
                                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
                                val usuariosRef: DatabaseReference = database.getReference("usuarios")

                                usuariosRef.orderByChild("email").equalTo(email)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            for (usuarioSnapshot in dataSnapshot.children) {
                                                usuarioSnapshot.ref.removeValue()
                                            }

                                            // El usuario y su registro se eliminaron correctamente
                                            Toast.makeText(
                                                requireContext(),
                                                "¡Cuenta eliminada exitosamente!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Manejar el caso de error en la consulta de la base de datos
                                            Toast.makeText(
                                                requireContext(),
                                                "¡Ha ocurrido un error al eliminar la cuenta!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            } else {
                                // Manejar el caso de error al eliminar la cuenta en la autenticación
                                Toast.makeText(
                                    requireContext(),
                                    "¡Ha ocurrido un error al eliminar la cuenta!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            confirmationDialog.show()
        } else {
            // Manejar el caso de usuario no autenticado
            Toast.makeText(requireContext(), "¡No se ha iniciado sesión!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPerfilCuenta().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
