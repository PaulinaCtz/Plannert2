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
import org.w3c.dom.Text

class busqueda : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)


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
        val titlesd = Interes.newInstance(imagenes,true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewElementosBusqueda,titlesd )
            .commit()

// :)

        //Obtiene con los extra el texto "FragmentoLista" para abrir una lista ya despues falta buscar con BD
        val fragmento = intent.getStringExtra("fragmento")
        if (fragmento == "FragmentoLista") {
            val fragment = Interes.newInstance(imagenes,true,"Titulos en la lista","Nombre de la lista",true)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewElementosBusqueda, fragment)
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewBusquedaTolbar, toolBarIcono())
                .commit()
        }
        }




    }









