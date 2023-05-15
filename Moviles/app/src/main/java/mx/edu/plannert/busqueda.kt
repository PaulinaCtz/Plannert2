package mx.edu.plannert

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text

class busqueda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

// Remover el fragment actual

        // Obtener el fragmento actual
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewElementosBusqueda)

// Si el fragmento existe, eliminarlo
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }



        obtenerDetalleContenido { listaDetalleContenido ->
            // Utilizar la lista actualizada
            val titlesd = Interes.newInstance(listaDetalleContenido,true)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerViewElementosBusqueda,titlesd )

                .commit()

        }


// :)

        //Obtiene con los extra el texto "FragmentoLista" para abrir una lista ya despues falta buscar con BD
        val fragmento = intent.getStringExtra("fragmento")
        if (fragmento == "FragmentoLista") {





            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewBusquedaTolbar, toolBarIcono())
                .addToBackStack(null)
                .commit()
        }
        }


    fun obtenerDetalleContenido(callback: (ArrayList<DetallesPeliculas>) -> Unit) {

        val database = Firebase.database
        val detalleContenidoRef = database.reference.child("detalleContenido")
        val listaDetalleContenido = ArrayList<DetallesPeliculas>()

        detalleContenidoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listaDetalleContenido.clear()
                for (registroSnapshot in dataSnapshot.children) {
                    var registro = registroSnapshot.getValue(DetallesPeliculas::class.java)
                    listaDetalleContenido.add(registro!!)
                }
                // Llamar al callback con la lista actualizada
                callback(listaDetalleContenido)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        })
    }
    fun obtenerPlataformas(callback: (ArrayList<DetallesPeliculas>) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("plataformas/")
        val listaDetalleContenido = ArrayList<DetallesPeliculas>()

        storageRef.listAll().addOnSuccessListener { result ->
            result.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    val registro = DetallesPeliculas(
                        urlImagen = uri.toString(),
                        categoria = "",
                        descripcion = "",
                        fecha = "",
                        nombreImagen = "",
                        tipo = "",
                        titulo = ""
                    )
                    listaDetalleContenido.add(registro)
                    callback(listaDetalleContenido)
                }
            }
        }.addOnFailureListener {
            // Manejar errores
        }
    }


    }









