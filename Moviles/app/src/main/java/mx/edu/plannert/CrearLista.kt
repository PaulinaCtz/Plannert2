package mx.edu.plannert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class CrearLista : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_lista)
        val siguiente: TextView = findViewById(R.id.tv_siguientePasoListas)
        val titulo: TextView = findViewById(R.id.titulo)
        val layoutC: LinearLayout= findViewById(R.id.layoutCrearListas)

        val btn1: Button = findViewById(R.id.Rbtn1)
        val btn2: Button = findViewById(R.id.Rbtn2)
        val btn3: Button = findViewById(R.id.Rbtn3)
        val btn4: Button = findViewById(R.id.Rbtn4)

        btn1.setBackgroundResource(R.drawable.circuloseleccionado)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewCuerpo)

        siguiente.setOnClickListener {
            fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewCuerpo)
            if (fragment is TipoLista) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, Categorias())
                    .commit()
                titulo.setText("Categorias")
                btn1.setBackgroundResource(R.drawable.circulo)
                btn2.setBackgroundResource(R.drawable.circuloseleccionado)
                btn1.setBackgroundResource(R.drawable.circulo)
                btn1.setBackgroundResource(R.drawable.circulo)

            }else if(fragment is Categorias) {
                val imagenes = arrayListOf(
                    Contenidos(R.drawable.pluto,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.netflix,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.primevideo,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.cuevana,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hbo,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.diney,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.star,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.tubi,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.vix,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.appletv,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.paramount,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hulu,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),


                    )
               val titlesd = Interes.newInstance(imagenes,"Romance",true)
                titulo.setText("Escoge los titulos de tu interés ")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo,titlesd )
                    .commit()

                btn1.setBackgroundResource(R.drawable.circulo)
                btn2.setBackgroundResource(R.drawable.circulo)
                btn3.setBackgroundResource(R.drawable.circuloseleccionado)
                btn4.setBackgroundResource(R.drawable.circulo)
            }else if (fragment is Interes) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, IdentificacionLista())
                    .commit()
                titulo.setText("¡Ya casi terminas!")
                siguiente.setText("Terminar")
                layoutC.setBackgroundResource(R.drawable.fondotransparente)
                btn1.setBackgroundResource(R.drawable.circulo)
                btn2.setBackgroundResource(R.drawable.circulo)
                btn3.setBackgroundResource(R.drawable.circulo)
                btn4.setBackgroundResource(R.drawable.circuloseleccionado)

            }else if (fragment is IdentificacionLista) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, TerminacionCreacionLista())
                    .commit()
                titulo.setText("¡Ya casi terminas!")
                siguiente.setText("Terminar")
                layoutC.setBackgroundResource(R.drawable.fondotransparente)
                btn1.visibility= View.INVISIBLE
                btn2.visibility= View.INVISIBLE
                btn3.visibility= View.INVISIBLE
                btn4.visibility= View.INVISIBLE

                siguiente.setText("Ir a inicio")
                titulo.setText("¡Felicidades!")

                if(siguiente.text.equals("Ir a inicio")){
                    siguiente.setOnClickListener{
                        val intent = Intent(this,Inicio::class.java)
                        startActivity(intent)
                    }
                }


            }

            btn1.setOnClickListener{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, TipoLista())
                    .commit()
                titulo.setText("¿De qué será tu lista?")
                siguiente.setText("Siguiente")
                btn2.setBackgroundResource(R.drawable.circulo)
                btn1.setBackgroundResource(R.drawable.circuloseleccionado)
                btn3.setBackgroundResource(R.drawable.circulo)
                btn4.setBackgroundResource(R.drawable.circulo)

            }

            btn2.setOnClickListener{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, Categorias())
                    .commit()
                titulo.setText("Categorias")
                siguiente.setText("Siguiente")
                btn1.setBackgroundResource(R.drawable.circulo)
                btn2.setBackgroundResource(R.drawable.circuloseleccionado)
                btn3.setBackgroundResource(R.drawable.circulo)
                btn4.setBackgroundResource(R.drawable.circulo)

            }

            btn3.setOnClickListener{
                val imagenes = arrayListOf(
                    Contenidos(R.drawable.pluto,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.netflix,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.primevideo,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.cuevana,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hbo,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.diney,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.star,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.tubi,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.vix,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.appletv,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.paramount,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),
                    Contenidos(R.drawable.hulu,"Título 1", "Autor 1", "Descripción 1", "Fecha 1", "Tipo 1", "Categoría 1"),


                    )
                val titlesd = Interes.newInstance(imagenes,"Romance",true)
                titulo.setText("Escoge los titulos de tu interés ")
                siguiente.setText("Siguiente")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo,titlesd )
                    .commit()

                btn1.setBackgroundResource(R.drawable.circulo)
                btn2.setBackgroundResource(R.drawable.circulo)
                btn3.setBackgroundResource(R.drawable.circuloseleccionado)
                btn4.setBackgroundResource(R.drawable.circulo)

            }

            btn4.setOnClickListener{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewCuerpo, IdentificacionLista())
                    .commit()
                titulo.setText("Categorias")
                siguiente.setText("Terminar")
                btn2.setBackgroundResource(R.drawable.circulo)
                btn4.setBackgroundResource(R.drawable.circuloseleccionado)
                btn3.setBackgroundResource(R.drawable.circulo)
                btn1.setBackgroundResource(R.drawable.circulo)

            }



        }
    }
}