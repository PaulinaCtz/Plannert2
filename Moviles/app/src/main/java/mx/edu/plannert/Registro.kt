package mx.edu.plannert

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.NumberKeyListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class Registro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Obten las instancias
        val email_txt: EditText = findViewById(R.id.txt_email)
        val nombre_txt: EditText = findViewById(R.id.txt_usuario)
        val telefono_txt: EditText = findViewById(R.id.txt_telefono)
        val contraseña_txt: EditText = findViewById(R.id.txt_contraseña)
        val txt_confirmacion : EditText = findViewById(R.id.txt_confirmar)
        val genero_text: EditText = findViewById(R.id.txt_genero)
        val fecha_txt: EditText = findViewById(R.id.txt_fecha)

        limitEditTextLength(email_txt, 40)
        limitEditTextLength(nombre_txt, 50)
        limitEditTextLength(telefono_txt, 10)
        limitEditTextLength(contraseña_txt, 25)
        limitEditTextLength(txt_confirmacion, 25)
        limitEditTextLength(genero_text, 20)
        limitEditTextLength(fecha_txt, 10)

        disableNumericInput(nombre_txt)
        disableNumericInput(genero_text)

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


            // Obtén los valores de los campos del formulario
            val email = email_txt.text.toString()
            val nombre = nombre_txt.text.toString()
            val telefono = telefono_txt.text.toString()
            val contraseña = contraseña_txt.text.toString()
            val genero = genero_text.text.toString()
            val fechaNacimiento = fecha_txt.text.toString()
            val confirmarContraseña = txt_confirmacion.text.toString()


            // Verifica si las contraseñas coinciden
            if (contraseña != confirmarContraseña) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifica si el email es valido.

            if(!isValidEmail(email)){
                Toast.makeText(this, "¡Ingresa un correo electrónico válido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Valida contraseña

            if (!isValidPassword(contraseña) || !isValidPassword(confirmarContraseña)){
                Toast.makeText(this, "¡Ingresa una contraseña válida!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            //Valida fecha

            if(!isValidDate(fechaNacimiento)){
                Toast.makeText(this, "¡Ingresa una fecha válida!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            // Verifica si el correo electrónico ya está registrado
            auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods != null && signInMethods.isNotEmpty()) {
                        Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show()
                    } else {
                        // Crea el usuario en Firebase Authentication
                        auth.createUserWithEmailAndPassword(email, contraseña)
                            .addOnCompleteListener(this) { authTask ->
                                if (authTask.isSuccessful) {
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
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    // Si ocurre un error al crear el usuario, muestra un mensaje de error
                                    Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Error al verificar el correo electrónico", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun disableNumericInput(editText: EditText) {
        editText.keyListener = object : NumberKeyListener() {
            override fun getInputType(): Int {
                return InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }

            override fun getAcceptedChars(): CharArray {
                return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray()
            }
        }
    }

    fun isValidDate(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        return try {
            dateFormat.parse(dateString)
            true
        } catch (e: ParseException) {
            false
        }
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$")
        return passwordPattern.matches(password)
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]+".toRegex()
        return email.matches(pattern) && email.length <= 40
    }

    private fun limitEditTextLength(editText: EditText, maxLength: Int) {
        editText.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }
}