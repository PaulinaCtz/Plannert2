package mx.edu.plannert

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Obtén una referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().reference

        auth = FirebaseAuth.getInstance()

        val inicia: TextView = findViewById(R.id.tv_iniciaSesion)
        val btnGuardar: Button = findViewById(R.id.btn_guardar)

        inicia.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnGuardar.setOnClickListener {

            // Obten las instancias
            val email_txt: EditText = findViewById(R.id.txt_email)
            val nombre_txt: EditText = findViewById(R.id.txt_usuario)
            val telefono_txt: EditText = findViewById(R.id.txt_telefono)
            val contraseña_txt: EditText = findViewById(R.id.txt_contraseña)
            val txt_confirmacion : EditText = findViewById(R.id.txt_confirmar)
            val genero_text: EditText = findViewById(R.id.txt_genero)
            val fecha_txt: EditText = findViewById(R.id.txt_fecha)

            // Obtén los valores de los campos del formulario
            val email = email_txt.text.toString()
            val nombre = nombre_txt.text.toString()
            val telefono = telefono_txt.text.toString()
            val contraseña =contraseña_txt.text.toString()
            val genero = genero_text.text.toString()
            val fechaNacimiento = fecha_txt.text.toString()
            val confirmarContraseña = txt_confirmacion.text.toString()

            // Verifica si las contraseñas coinciden
            if (contraseña != confirmarContraseña) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crea el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, contraseña)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""

                        // Genera una clave única para el nuevo registro en la base de datos
                        val nuevoRegistroKey = databaseReference.child("usuarios").push().key

                        // Crea un objeto con los datos del nuevo registro
                        val nuevoRegistro = HashMap<String, Any>()
                        nuevoRegistro["email"] = email
                        nuevoRegistro["nombre"] = nombre
                        nuevoRegistro["telefono"] = telefono
                        nuevoRegistro["contraseña"] = contraseña
                        nuevoRegistro["genero"] = genero
                        nuevoRegistro["fechaNacimiento"] = fechaNacimiento

                        // Guarda el nuevo registro en la base de datos
                        databaseReference.child("usuarios").child(nuevoRegistroKey!!)
                            .setValue(nuevoRegistro)

                        // Aquí puedes realizar alguna acción adicional después de crear el usuario
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        // Si ocurre un error al crear el usuario, muestra un mensaje de error
                        Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}