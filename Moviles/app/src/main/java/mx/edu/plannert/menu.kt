package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val opcionPerfil= findViewById<Button>(R.id.opcionPerfil)
        val opcionConfiguracion= findViewById<Button>(R.id.opcionConfiguracion)
        val opcionNosotros= findViewById<Button>(R.id.opcionNosotros)
        val opcionAyuda= findViewById<Button>(R.id.opcionAyuda)
        val regresaInicio = findViewById<ImageView>(R.id.menuu)

        val icono = findViewById<ImageView>(R.id.iv_iconoMenu)

        val nombreUsuario = findViewById<TextView>(R.id.tv_nombreUsuarioMenu)
        obtenerUsuarioActual { usuario ->

            if (usuario != null) {
               if(usuario.Avatar=="uno"){

                   icono.setBackgroundResource(R.drawable.icono1)

               }else if(usuario.Avatar=="dos"){

                   icono.setBackgroundResource(R.drawable.icono2)

               }else if(usuario.Avatar=="tres"){

                   icono.setBackgroundResource(mx.edu.plannert.R.drawable.icono3)

               }else if(usuario.Avatar=="cuatro"){

                   icono.setBackgroundResource(R.drawable.icono4)

               }
                nombreUsuario.setText(usuario.usuario)
            } else {
                // Aquí manejas el caso en el que no se encontró el usuario o no hay un usuario autenticado
                println("No se encontró el usuario o no hay un usuario autenticado.")
            }
        }


        opcionPerfil.setOnClickListener{
            val intent = Intent(this, OpcionesMenu::class.java)
            startActivity(intent)
        }

        opcionAyuda.setOnClickListener{
            val intent = Intent(this, help::class.java)
            startActivity(intent)
        }

        opcionNosotros.setOnClickListener{
            val intent = Intent(this, us::class.java)
            startActivity(intent)
        }

        opcionConfiguracion.setOnClickListener {
            val intent = Intent(this, ConfigIdioma::class.java)
            startActivity(intent)
        }
        regresaInicio.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
        }
    }


    fun obtenerUsuarioActual(callback: (Usuarios?) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val usuariosRef: DatabaseReference= database.getReference("usuarios")

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