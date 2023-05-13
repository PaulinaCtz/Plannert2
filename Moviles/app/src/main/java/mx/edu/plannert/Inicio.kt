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

class Inicio : AppCompatActivity() {

    private lateinit var client:GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        var menu1: ImageView = findViewById(R.id.menup)

        menu1.setOnClickListener {
            val intent = Intent(this,menu::class.java)
            startActivity(intent)
        }

        val tvname = findViewById<TextView>(R.id.tvname)

        val bundle = intent.extras
        if (bundle != null){
            val name = bundle.getString("name")
            tvname.setText(name)
        }

        val options = GoogleSignInOptions.Builder().requestEmail().build()
        client = GoogleSignIn.getClient(this,options)

        val cerrar: TextView = findViewById(R.id.cerrar)

        cerrar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            client.signOut()
            val i:Intent = Intent(this,MainActivity::class.java)
            startActivity(i)
        }

    }
}