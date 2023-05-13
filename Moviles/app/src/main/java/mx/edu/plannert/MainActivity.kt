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
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {


    private lateinit var databaseReference: DatabaseReference

    //private lateinit var auth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var client:GoogleSignInClient
    val LOG_OUT = 234

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

        try{
            val options = GoogleSignInOptions.Builder().requestEmail().build()
            client = GoogleSignIn.getClient(this,options)
            inicioGoogle.setOnClickListener {
                val intent = client.signInIntent
                startActivityForResult(intent,100)
            }
        }catch (e:ApiException){

        }


        botonIS.setOnClickListener {

            if(emailET.text.isNotEmpty() && pswET.text.isNotEmpty() ){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailET.text.toString(),
                    pswET.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome()
                    }else{
                        showAlert("Se produjo un error autenticando al usuario.")
                    }
                }
            }else{
                showAlert("Debes llenar todos los campos")
            }
            /*if (emailET.text.isEmpty() || pswET.text.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                if (emailET.text.isNotEmpty() && pswET.text.isNotEmpty()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        emailET.text.toString().trim(),
                        pswET.text.toString().trim()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Introductorio::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // El inicio de sesión falló, mostrar mensaje de error
                            Toast.makeText(
                                this,
                                "Inicio de sesión fallido, verifica la contraseña",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }*/

            /*
            val email = email_txt.text.toString().trim()
            val contraseña = contraseña_txt.text.toString().trim()

            if (email.isEmpty() || contraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                // Iniciar sesión con el usuario y la contraseña
                auth.signInWithEmailAndPassword(email, contraseña)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // El inicio de sesión fue exitoso, redirigir a la página Introductorio
                            val intent = Intent(this, Introductorio::class.java)
                            startActivity(intent)
                            finish() // Evitar que el usuario regrese a la pantalla de inicio de sesión
                        } else {
                            // El inicio de sesión falló, mostrar mensaje de error
                            Toast.makeText(this, "Inicio de sesión fallido, verifica la contraseña", Toast.LENGTH_SHORT).show()
                        }
                    }
            }*/
        }

        necesitoAyuda.setOnClickListener {
            val intent = Intent(this, help::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100){
            try{
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                val credencial = GoogleAuthProvider.getCredential(account.id,null)
                FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener{
                    if(task.isSuccessful){

                        val i = Intent(this,Inicio::class.java)
                        i.putExtra("name",FirebaseAuth.getInstance().currentUser?.displayName)
                        startActivity(i)

                    }else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:ApiException){

            }
            }

        if(requestCode==LOG_OUT){
            signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null){
            val i = Intent(this,Inicio::class.java)
            i.putExtra("name",FirebaseAuth.getInstance().currentUser?.email)
            startActivity(i)
        }
    }

    private fun showAlert (msg:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.setPositiveButton( "Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(){
        val homeIntent= Intent(this, Introductorio::class.java)
        startActivity(homeIntent)
    }

    private fun signOut(){
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
