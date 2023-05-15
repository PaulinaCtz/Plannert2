package mx.edu.plannert

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso

class ListasFav2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas_fav2)
        ////EN PROCESO, LO TUVE QUE BORRAR
        var gridview:GridView = findViewById(R.id.grid_titulos)
        val nombre = intent.getStringExtra("nombre")
        val categoria = intent.getStringExtra("categoria")
       // val contenidos = intent.getStringExtra("contenidos")

        val contenidos = intent.getParcelableArrayListExtra<DetallesPeliculas>("contenidos")

        val favorita = intent.getBooleanExtra("favorita", false)
        val icono = intent.getIntExtra("icono", 0)
        val usuario = intent.getStringExtra("usuario")
        val idUsuario = intent.getStringExtra("idUsuario")
        val tipo = intent.getStringExtra("tipo")

        val tv_TituloLista = findViewById<TextView>(R.id.tv_tituloLista)

        tv_TituloLista.setText(nombre)

        val adapter = contenidos?.let { contenidosAdapter(this, it) }

// Establecer el adaptador en el GridView
        gridview.adapter = adapter



    }
}


class contenidosAdapter(private val context: Context, private val contenidos: ArrayList<DetallesPeliculas>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        // imageView.setImageResource(contenidos[position].imagen)
        Picasso.get().load(contenidos[position].urlImagen).into(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(570, 770)


        // para mostrar el detalle :)

        imageView.setOnClickListener {
            Toast.makeText(context, "Aun no jala", Toast.LENGTH_SHORT).show()

            /*
            // verificica si se esta usando en busqueda para ver si se activa la funcionalidad del clickOnListener de los elementos del gridview

                val activity = context as busqueda


                val detalleFragment = detalleContenido.newInstance(contenidos[position])


                val intent = Intent(context, DetalleContenidoDetalle::class.java)
                intent.putExtra("titulo", contenidos[position].titulo)
                intent.putExtra("descripcion", contenidos[position].descripcion)
                intent.putExtra("urlImagen", contenidos[position].urlImagen)
                intent.putExtra("categoria", contenidos[position].categoria)
                intent.putExtra("fecha", contenidos[position].fecha)
                intent.putExtra("nombreImagen", contenidos[position].nombreImagen)
                intent.putExtra("tipo", contenidos[position].tipo)

                context.startActivity(intent)


                /*
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewBusquedaTolbar, toolBarIcono())
                    .commit()

                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewElementosBusqueda, detalleFragment)
                    .commit()*/

*/


        }

        return imageView



    }

    override fun getItem(position: Int): Any {
        return contenidos.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return contenidos.size
    }


}
