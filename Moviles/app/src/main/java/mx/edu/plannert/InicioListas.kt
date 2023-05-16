package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class InicioListas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_listas)

        val button = findViewById<Button>(R.id.btnInicioLista)
        val masTarde: TextView = findViewById(R.id.masTardeInicioListas)
        val icono: ImageView = findViewById(R.id.avatarImagen)
        val nombreUsuario: TextView = findViewById(R.id.nombreUsuariotxt)

        button.setOnClickListener {
            val intent = Intent(this, ListasNuevas::class.java)
            startActivity(intent)
        }

        masTarde.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }

        obtenerUsuarioActual { usuario ->
            if (usuario != null) {
                when (usuario.Avatar) {
                    "uno" -> icono.setBackgroundResource(R.drawable.icono1)
                    "dos" -> icono.setBackgroundResource(R.drawable.icono2)
                    "tres" -> icono.setBackgroundResource(R.drawable.icono3)
                    "cuatro" -> icono.setBackgroundResource(R.drawable.icono4)
                    else -> {
                        // Manejar el caso si el valor de avatar no coincide con ninguna opción
                    }
                }
                nombreUsuario.text = usuario.usuario
            } else {
                // Manejar el caso en el que no se encontró el usuario o no hay un usuario autenticado
                println("No se encontró el usuario o no hay un usuario autenticado.")
            }
        }
    }

    private fun obtenerUsuarioActual(callback: (Usuarios?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usuariosRef: DatabaseReference = database.getReference("usuarios")

            val usuarioReferencia = usuariosRef.orderByChild("email").equalTo(currentUser.email)

            usuarioReferencia.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usuarioSnapshot = dataSnapshot.children.firstOrNull()
                    val usuario = usuarioSnapshot?.getValue(Usuarios::class.java)
                    callback(usuario)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback(null)
                }
            })
        } else {
            callback(null)
        }
    }
}
