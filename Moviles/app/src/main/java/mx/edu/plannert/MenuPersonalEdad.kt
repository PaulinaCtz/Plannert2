package mx.edu.plannert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuPersonalEdad.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuPersonalEdad : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // Declarar una variable para almacenar la fecha seleccionada
    private var fechaSeleccionada: Long = 0

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
        val view = inflater.inflate(R.layout.fragment_menu_personal_edad, container, false)

        val btnCambiarFecha = view.findViewById<Button>(R.id.buttonCambiarEdad)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Almacenar la fecha seleccionada en una variable
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            fechaSeleccionada = calendar.timeInMillis
        }

        btnCambiarFecha.setOnClickListener {
            // Verificar que se haya seleccionado una fecha
            if (fechaSeleccionada == 0L) {
                Toast.makeText(
                    requireContext(),
                    "¡Debes seleccionar una fecha de nacimiento!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Obtener la referencia a la base de datos
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usuariosRef: DatabaseReference = database.getReference("usuarios")
            val auth: FirebaseAuth = FirebaseAuth.getInstance()

            // Obtener el usuario actual
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val email = currentUser.email

                usuariosRef.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (usuarioSnapshot in dataSnapshot.children) {
                                val usuarioKey = usuarioSnapshot.key
                                // Actualizar la fecha de nacimiento en la base de datos
                                usuariosRef.child(usuarioKey!!).child("fechaNacimiento")
                                    .setValue(getFormattedDate())
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // La fecha de nacimiento se actualizó correctamente
                                            Toast.makeText(
                                                requireContext(),
                                                "¡Fecha de nacimiento modificada!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Manejar el caso de error al actualizar la fecha de nacimiento
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

    // Método para obtener la fecha formateada en "dd/MM/yyyy"
    private fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = fechaSeleccionada

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuPersonalEdad().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}