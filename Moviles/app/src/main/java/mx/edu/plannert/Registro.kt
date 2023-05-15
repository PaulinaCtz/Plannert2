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
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

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
                       ///////////////////////////////////////////////
                        /*
                        // Crea una referencia a la carpeta contenidos/
                        val folderRef = Firebase.storage.reference.child("contenidos/")

// Crea una referencia a la base de datos en tiempo real
                      //  val database = Firebase.database.reference

                        val imagenes = arrayListOf(
                            Contenidos(R.drawable.alien,"Alien", "La tripulación del remolcador espacial Nostromo atiende una señal de socorro y, sin saberlo, sube a bordo una letal forma de vida extraterrestre.",
                                "Fecha 1", "Pelicula", "Sci-Fi"),
                            Contenidos(R.drawable.blackswan,"Blackswan", "Una brillante bailarina que forma parte de una compañía de ballet de Nueva York, vive completamente absorbida por la danza. La presión de sus controladora madre y la rivalidad con su compañera Lily afectarán terriblemente a la niña.",
                                "Fecha 1", "Pelicula", "Terror"),
                            Contenidos(R.drawable.elvis,"Elvis", "Elvis Presley salta a la fama en la década de 1950, mientras mantiene una relación compleja con su manager, el coronel Tom Parker.",
                                "Fecha 1", "Pelicula", "Romance"),
                            Contenidos(R.drawable.fightclub,"Fightclub", "Un empleado de oficina insomne en busca de una manera de cambiar su vida se cruza con un vendedor y crean un club de lucha clandestino como forma de terapia.",
                                "Fecha 1", "Pelicula", "Accion"),
                            Contenidos(R.drawable.hollywood,"Once up time in hollywood", "A finales de los 60, Hollywood empieza a cambiar y el actor Rick Dalton tratará de seguir el cambio. Junto a su doble, ambos verán como sus raíces les complican el cambio, y al mismo tiempo su relación con la actriz Sharon Tate, que fue víctima de los Manson en la matanza de 1969, y que esta acaba de casarse con el prestigioso director Roman Polanski.",
                                "Fecha 1", "Pelicula", "Accion"),
                            Contenidos(R.drawable.ironman,"IronMan", "Tony Stark es un inventor de armamento brillante que es secuestrado en el extranjero. Sus captores son unos terroristas que le obligan a construir una máquina destructiva pero Tony se construirá una armadura para poder enfrentarse a ellos y escapar.",
                                "Fecha 1", "Pelicula", "Accion"),
                            Contenidos(R.drawable.lightyear,"Lightyear", "Buzz Lightyear se embarca en una aventura intergaláctica con un grupo de reclutas ambiciosos y su compañero robot.",
                                "Fecha 1", "Pelicula", "Accion"),
                            Contenidos(R.drawable.loveactually,"Love actually", "Las vidas de varias parejas se entrecruzan en Londres, poco antes de las fiestas de Navidad, con resultados románticos, divertidos y agridulces.",
                                "Fecha 1", "Pelicula", "Romance"),
                            Contenidos(R.drawable.prodigy,"The prodigy", "Una madre preocupada por el comportamiento perturbador e irracional de su hijo, está convencida de que algo sobrenatural está transformando la vida del pequeño y de quienes lo rodean, poniéndolos en peligro.",
                                "Fecha 1", "Pelicula", "Terror"),
                            Contenidos(R.drawable.quantumania,"Quantumania", "Los superhéroes Scott y Cassie Lang, Hope van Dyne y sus padres, se encuentran accidentalmente atrapados en el reino cuántico y deben enfrentarse a un nuevo enemigo, Kang el Conquistador.",
                                "Fecha 1", "Pelicula", "Sci-Fi"),
                            Contenidos(R.drawable.shanchi,"Shang-Chi",  "El maestro de artes marciales Shang-Chi se enfrenta al pasado que creía haber dejado atrás cuando se ve envuelto en la red de la misteriosa organización de los Diez Anillos.",
                                "Fecha 1", "Pelicula", "Sci-Fi"),
                            Contenidos(R.drawable.shrek,"Shrek",  "Hace mucho tiempo, en una lejana ciénaga, vivía un ogro llamado Shrek. Un día, su preciada soledad se ve interrumpida por un montón de personajes de cuento de hadas que invaden su casa. Todos fueron desterrados de su reino por el malvado Lord Farquaad. Decidido a devolverles su reino y recuperar la soledad de su ciénaga, Shrek llega a un acuerdo con Lord Farquaad y va a rescatar a la princesa Fiona, la futura esposa del rey. Sin embargo, la princesa esconde un oscuro secreto.",
                                "Fecha 1", "Pelicula", "Accion"),


                            )

// Obtén una lista de todos los elementos en la carpeta contenidos/
                        folderRef.listAll()
                            .addOnSuccessListener { result ->
                                // Itera sobre cada elemento en la carpeta
                                result.items.forEach { imageRef ->
                                    var c=0
                                    // Obtén la URL de descarga de la imagen
                                    imageRef.downloadUrl.addOnSuccessListener { url ->
                                        // Guarda la información de la imagen en la base de datos
                                        val imageInfo = hashMapOf(
                                            "titulo" to imagenes[c].titulo,
                                            "descripcion" to imagenes[c].descripcion,
                                            "nombreImagen" to imageRef.name,
                                            "urlImagen" to url.toString(),
                                            "fecha" to ServerValue.TIMESTAMP,
                                            "categoria" to imagenes[c].categoria,
                                            "tipo" to imagenes[c].tipo
                                        )
                                        databaseReference.child("detalleContenido").push().setValue(imageInfo)
                                   c++
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Maneja los errores aquí
                                println("Error al obtener imágenes: $exception")
                            }*/
/////////////////////////////////////////////////////////////////////////////////////
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