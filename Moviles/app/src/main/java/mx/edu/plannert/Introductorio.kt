package mx.edu.plannert

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage

class Introductorio : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introductorio)

        val siguiente: TextView=findViewById(R.id.tv_siguientePaso)
        val myLinearLayout = findViewById<LinearLayout>(R.id.layoutI)

        val omitir: TextView=findViewById(R.id.textoOmitir)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        var Rbtn1:RadioButton=findViewById(R.id.Rbtn1)
        var Rbtn2:RadioButton=findViewById(R.id.Rbtn2)
        var Rbtn3:RadioButton=findViewById(R.id.Rbtn3)
        var Rbtn4:RadioButton=findViewById(R.id.Rbtn4)
        var Rbtn5:RadioButton=findViewById(R.id.Rbtn5)
        var actual: String? =null
        Rbtn1.setBackgroundResource(R.drawable.circuloseleccionado)



        siguiente.setOnClickListener {
            fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)




            obtenerDetalleContenido { listaDetalleContenido ->
                print(listaDetalleContenido)
            }


            if(fragment is Bienvenida) {
                actual="peliculas"


                /*
                val imagenes = arrayListOf(
                    Contenidos(R.drawable.prodigy,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.alien,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.ironman,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.shanchi,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.quantumania,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.lightyear,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.shrek,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.elvis,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.fightclub,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.tres,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.blackswan,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hollywood,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),


                    )*/




                //  ESTO ES LO NUEVO!!!!
                print("llego aqui")
               // print(listaDetalleContenido.get(3))
                obtenerDetalleContenido { listaDetalleContenido ->
                    // Utilizar la lista actualizada
                    val interes=Interes.newInstance(listaDetalleContenido,"Peliculas")

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, interes)
                        .commit()
                }

/////////////////////////////////////////////////////////////////////////////////////////////////////


                Rbtn2.setBackgroundResource(R.drawable.circuloseleccionado)
                Rbtn3.setBackgroundResource(R.drawable.circulo)
                Rbtn1.setBackgroundResource(R.drawable.circulo)
                Rbtn4.setBackgroundResource(R.drawable.circulo)

            }else if(fragment is Interes&& actual=="peliculas"){

                    actual = "plataformas"
                /*
                val imagenesP = arrayListOf(
                    Contenidos(R.drawable.pluto,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.netflix,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.primevideo,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.cuevana,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hbo,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.diney,"Título 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.star,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.tubi,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.vix,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.appletv,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.paramount,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hulu,"Título 1",  "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),


                    )
                    */

                obtenerPlataformas { listaDetalleContenido ->
                    // Hacer lo que necesites con la lista de objetos DetallesPeliculas
                    val interes2 = Interes.newInstance(listaDetalleContenido, "Plataformas")



                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, interes2)
                        .commit()
                }

                    Rbtn1.setBackgroundResource(R.drawable.circulo)
                    Rbtn2.setBackgroundResource(R.drawable.circulo)
                Rbtn5.setBackgroundResource(R.drawable.circulo)
                Rbtn4.setBackgroundResource(R.drawable.circulo)
                    Rbtn3.setBackgroundResource(R.drawable.circuloseleccionado)

            }else if(fragment is Interes&& actual=="plataformas"){
              //  Toast.makeText(this, "HOLLA", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, elegirAvatar())
                        .commit()

                    Rbtn1.setBackgroundResource(R.drawable.circulo)
                    Rbtn2.setBackgroundResource(R.drawable.circulo)
                    Rbtn3.setBackgroundResource(R.drawable.circulo)
                    Rbtn5.setBackgroundResource(R.drawable.circulo)
                    Rbtn4.setBackgroundResource(R.drawable.circuloseleccionado)
                }else if(fragment is elegirAvatar){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, nombrarAvatar())
                    .commit()

                Rbtn1.setBackgroundResource(R.drawable.circulo)
                Rbtn2.setBackgroundResource(R.drawable.circulo)
                Rbtn3.setBackgroundResource(R.drawable.circulo)
                Rbtn5.setBackgroundResource(R.drawable.circuloseleccionado)
                Rbtn4.setBackgroundResource(R.drawable.circulo)

            }else if(fragment is nombrarAvatar){
                val intent = Intent(this, InicioListas::class.java)
                startActivity(intent)

            }

            myLinearLayout.setBackgroundResource(R.drawable.fondomorado)

        }

        Rbtn1.setOnClickListener{

            fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Bienvenida())
                .commit()

            Rbtn1.setBackgroundResource(R.drawable.circuloseleccionado)
            Rbtn2.setBackgroundResource(R.drawable.circulo)
            Rbtn3.setBackgroundResource(R.drawable.circulo)
            Rbtn4.setBackgroundResource(R.drawable.circulo)
            myLinearLayout.setBackgroundResource(R.drawable.fondotransparente)


        }

        Rbtn2.setOnClickListener{

            obtenerDetalleContenido { listaDetalleContenido ->
                // Utilizar la lista actualizada
                val interes=Interes.newInstance(listaDetalleContenido,"Peliculas")

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, interes)
                    .commit()
            }
                Rbtn1.setBackgroundResource(R.drawable.circulo)
                Rbtn3.setBackgroundResource(R.drawable.circulo)
            Rbtn5.setBackgroundResource(R.drawable.circulo)
            Rbtn4.setBackgroundResource(R.drawable.circulo)

                Rbtn2.setBackgroundResource(R.drawable.circuloseleccionado)
            myLinearLayout.setBackgroundResource(R.drawable.fondomorado)




        }

        Rbtn3.setOnClickListener{


            obtenerPlataformas { listaDetalleContenido ->
                // Hacer lo que necesites con la lista de objetos DetallesPeliculas
                val interes2 = Interes.newInstance(listaDetalleContenido, "Plataformas")



                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, interes2)
                    .commit()
            }

                Rbtn1.setBackgroundResource(R.drawable.circulo)
            Rbtn2.setBackgroundResource(R.drawable.circulo)
            Rbtn4.setBackgroundResource(R.drawable.circulo)
            Rbtn5.setBackgroundResource(R.drawable.circulo)
                Rbtn3.setBackgroundResource(R.drawable.circuloseleccionado)

            myLinearLayout.setBackgroundResource(R.drawable.fondomorado)
        }

        Rbtn4.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, elegirAvatar())
                .commit()
            Rbtn1.setBackgroundResource(R.drawable.circulo)
            Rbtn2.setBackgroundResource(R.drawable.circulo)
            Rbtn3.setBackgroundResource(R.drawable.circulo)
            Rbtn5.setBackgroundResource(R.drawable.circulo)
            Rbtn4.setBackgroundResource(R.drawable.circuloseleccionado)

            myLinearLayout.setBackgroundResource(R.drawable.fondomorado)
        }

        Rbtn5.setOnClickListener{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, nombrarAvatar())
                .commit()
            Rbtn1.setBackgroundResource(R.drawable.circulo)
            Rbtn2.setBackgroundResource(R.drawable.circulo)
            Rbtn3.setBackgroundResource(R.drawable.circulo)
            Rbtn5.setBackgroundResource(R.drawable.circuloseleccionado)
            Rbtn4.setBackgroundResource(R.drawable.circulo)

            myLinearLayout.setBackgroundResource(R.drawable.fondomorado)

        }

        omitir.setOnClickListener {
            val intent =Intent(this,InicioListas::class.java)
            startActivity(intent)
        }



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




private fun FragmentTransaction.replace(fragmentContainerView: Int, elegirAvatar: elegirAvatar.Companion) {

}
