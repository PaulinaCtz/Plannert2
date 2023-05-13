package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import android.content.Context


class ListasNuevas : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas_nuevas)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val radioButtonSeries = findViewById<RadioButton>(R.id.btn_series)
        val radioButtonLibros = findViewById<RadioButton>(R.id.btn_libros)

        val radioButtonPeliculas = findViewById<RadioButton>(R.id.btn_pelicula)

        val radioButtonVideojuegos = findViewById<RadioButton>(R.id.btn_videojuegos)
        val crear = findViewById<Button>(R.id.btnCrear)



        // Mostrar el botón de volver atrás en la barra de herramientas

        // Mostrar el botón de volver atrás en la barra de herramientas
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        // Cambiar el título de la barra de herramientas
        //getSupportActionBar()?.setTitle("Volver / Necesito ayuda");

        crear.setOnClickListener{

            val radioGroupIconos = findViewById<RadioGroup>(R.id.RadioGroupIconos)
            val iconoId = radioGroupIconos.checkedRadioButtonId

            val radioGroupTipos = findViewById<RadioGroup>(R.id.RadioGroupTipo)
            val tipoId = radioGroupTipos.checkedRadioButtonId

            val radioGroupCategorias= findViewById<RadioGroup>(R.id.RadioGroupCategoria)
            val categoriasId = radioGroupCategorias.checkedRadioButtonId

            val nombreLista = findViewById<TextView>(R.id.txtNombreLista)

            var iconoSeleccionado = ""
            var tipoSeleccionado = ""
            var categoriaSeleccionado = ""

            if (iconoId != -1) {
                // Obtener la instancia del RadioButton seleccionado
                val selectedRadioButton = findViewById<RadioButton>(iconoId)

                // Obtener el texto del RadioButton seleccionado
                 iconoSeleccionado = selectedRadioButton.tag.toString()


                if (tipoId != -1) {
                    // Obtener la instancia del RadioButton seleccionado
                    val selectedRadioButton = findViewById<RadioButton>(tipoId)

                    // Obtener el texto del RadioButton seleccionado
                    tipoSeleccionado = selectedRadioButton.tag.toString()
                    if (categoriasId != -1) {
                        // Obtener la instancia del RadioButton seleccionado
                        val selectedRadioButton = findViewById<RadioButton>(categoriasId)

                        // Obtener el texto del RadioButton seleccionado
                        categoriaSeleccionado = selectedRadioButton.text.toString()
                        val nombre=nombreLista.text.toString()
                        guardarLista(iconoSeleccionado,nombre,tipoSeleccionado,categoriaSeleccionado)




                    } else {
                        Toast.makeText(this, "Seleccione una categoria", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Toast.makeText(this, "Seleccione de que sera su lista", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Seleccione un icono", Toast.LENGTH_SHORT).show()
            }

        }



        getSupportActionBar()?.setTitle(Html.fromHtml("<font color='#ffffff' size='5sp' font-family='@font/intermedium'>Inicio / Nueva lista</font>"))

         radioButtonSeries.setOnClickListener {

             radioButtonSeries.setBackgroundResource(R.drawable.circulomorado)
             radioButtonVideojuegos.setBackgroundResource(R.drawable.circulomoradorecurso)
             radioButtonPeliculas.setBackgroundResource(R.drawable.circulomoradorecurso)
             radioButtonLibros.setBackgroundResource(R.drawable.circulomoradorecurso)
         }

        radioButtonPeliculas.setOnClickListener {

            radioButtonSeries.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonVideojuegos.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonPeliculas.setBackgroundResource(R.drawable.circulomorado)
            radioButtonLibros.setBackgroundResource(R.drawable.circulomoradorecurso)
        }

        radioButtonVideojuegos.setOnClickListener {

            radioButtonSeries.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonVideojuegos.setBackgroundResource(R.drawable.circulomorado)
            radioButtonPeliculas.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonLibros.setBackgroundResource(R.drawable.circulomoradorecurso)
        }

        radioButtonLibros.setOnClickListener {

            radioButtonSeries.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonVideojuegos.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonPeliculas.setBackgroundResource(R.drawable.circulomoradorecurso)
            radioButtonLibros.setBackgroundResource(R.drawable.circulomorado)
        }

    }

    private fun guardarLista(icono: String, nombre: String, tipo: String,categoria:String) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val usuariosRef: DatabaseReference = database.getReference("usuarios")
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference




        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email

            usuariosRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usuarioSnapshot in dataSnapshot.children) {
                        val usuarioKey = usuarioSnapshot.key
                        val usuario = usuarioSnapshot.getValue(Usuarios::class.java)


                        // Genera una clave única para el nuevo registro en la base de datos
                        val nuevoRegistroKey = databaseReference.child("listas").push().key

                       // val listaContenidos = listOf<Contenidos>()

                        // Crea un objeto con los datos del nuevo registro(lista)
                        val nuevoRegistro = HashMap<String, Any>()
                        nuevoRegistro["usuario"] = usuario?.usuario.toString()
                        nuevoRegistro["icono"] = icono
                        nuevoRegistro["nombre"] = nombre
                        nuevoRegistro["tipo"] = tipo
                        nuevoRegistro["categoria"] = categoria
                        nuevoRegistro["favorita"] = false
                        nuevoRegistro["idUsuario"] = usuarioKey.toString()
                      //  nuevoRegistro["contenidos"] =listaContenidos

                        // Guarda el nuevo registro de la lista en la base de datos
                        databaseReference.child("listas").child(nuevoRegistroKey!!)
                            .setValue(nuevoRegistro)
                            .addOnSuccessListener {
                                Toast.makeText(this@ListasNuevas, "Lista creada con exito", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@ListasNuevas, MisListas::class.java)
                                startActivity(intent)
                                this@ListasNuevas.finish()
                            }








                        // return

                    }


                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}