package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Inicio : AppCompatActivity() {

    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        var menu1: ImageView = findViewById(R.id.menup)
        menu1.setOnClickListener {
            val intent = Intent(this, menu::class.java)
            startActivity(intent)
        }

        val tvname = findViewById<TextView>(R.id.tvname)

        val options = GoogleSignInOptions.Builder().requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        val databaseReference = FirebaseDatabase.getInstance().getReference()
        val query = databaseReference.child("usuarios").orderByChild("email").equalTo(currentUser?.email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var nombreUsuario: String? = null

                for (snapshot in dataSnapshot.children) {
                    val usuario = snapshot.getValue(Usuarios::class.java)
                    if (usuario != null && usuario.email == currentUser?.email) {
                        nombreUsuario = usuario.usuario
                        break
                    }
                }

                if (nombreUsuario != null) {
                    tvname.text = nombreUsuario
                } else {
                    tvname.text = "Nombre de usuario no encontrado"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error de la consulta
                tvname.text = "Error al obtener el nombre de usuario"
            }
        })
    }

    override fun onResume() {
        super.onResume()

        // Volver a obtener y mostrar el nombre de usuario
        val currentUser = auth.currentUser

        val tvname = findViewById<TextView>(R.id.tvname)

        val databaseReference = FirebaseDatabase.getInstance().getReference()
        val query = databaseReference.child("usuarios").orderByChild("email").equalTo(currentUser?.email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var nombreUsuario: String? = null

                for (snapshot in dataSnapshot.children) {
                    val usuario = snapshot.getValue(Usuarios::class.java)
                    if (usuario != null && usuario.email == currentUser?.email) {
                        nombreUsuario = usuario.usuario
                        break
                    }
                }

                if (nombreUsuario != null) {
                    tvname.text = nombreUsuario
                } else {
                    tvname.text = "Nombre de usuario no encontrado"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar el error de la consulta
                tvname.text = "Error al obtener el nombre de usuario"
            }
        })
    }
}
