package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private val LOG_OUT = 234

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Plannert)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val registro: TextView = findViewById(R.id.tv_registrate)
        val botonIS: Button = findViewById(R.id.btnIniciarSesion)
        val necesitoAyuda: TextView = findViewById(R.id.necesitoAyuda)
        val inicioGoogle: TextView = findViewById(R.id.inicioGoogle)

        val emailET: EditText = findViewById(R.id.txt_emailInicio)
        val pswET: EditText = findViewById(R.id.txt_contraseñaInicio)

        registro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        try {
            val options = GoogleSignInOptions.Builder().requestEmail().build()
            client = GoogleSignIn.getClient(this, options)
            inicioGoogle.setOnClickListener {
                val intent = client.signInIntent
                startActivityForResult(intent, 100)
            }
        } catch (e: ApiException) {
            // Manejar la excepción
        }

        botonIS.setOnClickListener {
            if (emailET.text.isNotEmpty() && pswET.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailET.text.toString(),
                    pswET.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        UsuariosEstados()
                    } else {
                        showAlert("Se produjo un error autenticando al usuario.")
                    }
                }
            } else {
                showAlert("Debes llenar todos los campos")
            }
        }

        necesitoAyuda.setOnClickListener {
            val intent = Intent(this, help::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val credencial = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credencial)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            UsuariosEstados()
                        } else {
                            showAlert("Error al iniciar sesión con Google.")
                        }
                    }
            } catch (e: ApiException) {
                // Manejar la excepción
            }
        }

        if (requestCode == LOG_OUT) {
            signOut()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    private fun UsuariosEstados() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val email = currentUser.email

            // Verificar si el correo existe en la base de datos
            val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
            val query = databaseReference.orderByChild("email").equalTo(email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val user = dataSnapshot.children.first().getValue(Usuarios::class.java)

                        if (user != null) {
                            if (user.usuario != null && user.usuario.isNotEmpty()) {
                                // El usuario ya tiene el atributo "usuario", redirigir a la página de inicio
                                startActivity(Intent(this@MainActivity, Inicio::class.java))
                            } else {
                                // El usuario no tiene el atributo "usuario", redirigir a la página de introducción
                                startActivity(Intent(this@MainActivity, Introductorio::class.java))
                            }
                        } else {
                            // El usuario no tiene información en la base de datos, redirigir a la página de introducción
                            startActivity(Intent(this@MainActivity, Introductorio::class.java))
                        }
                    } else {
                        // El correo no existe en la base de datos, redirigir a la página de introducción
                        startActivity(Intent(this@MainActivity, Introductorio::class.java))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showAlert("Se produjo un error al acceder a la base de datos.")
                }
            })
        }
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun signOut() {
        client.signOut().addOnCompleteListener(this) {
            Toast.makeText(
                this,
                "Sesión terminada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun generarPassword(longitud: Int): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = java.security.SecureRandom()
        val sb = StringBuilder(longitud)
        for (i in 0 until longitud) {
            val index = random.nextInt(caracteres.length)
            sb.append(caracteres[index])
        }
        return sb.toString()
    }
}

