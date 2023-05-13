package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class listasEmergentes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas_emergentes)

        val lista1=findViewById<Button>(R.id.botonLista1)

        lista1.setOnClickListener {

            //Se supone que estas son las de la lista 1 ya despues se obtienen de BD
            val imagenes = arrayListOf(
                Contenidos(R.drawable.prodigy,"Prodigy", "Autor 1", "Descripción 1 shhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhbb  sggdgsgsgagag dhgcgshksskmvkfsdajnvhsfbadncz", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.alien,"Título de prueba largo hhhh lbg", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.ironman,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.shanchi,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.quantumania,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.lightyear,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.shrek,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.elvis,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.fightclub,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.tres,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.blackswan,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                Contenidos(R.drawable.hollywood,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),


                )

            val intent = Intent(this, busqueda::class.java)
            intent.putExtra("fragmento", "FragmentoLista")
            startActivity(intent)




        }

    }
}