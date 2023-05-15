package mx.edu.plannert


import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PeliculaAdapter(private val context: Context, private val contenidos: ArrayList<DetallesPeliculas>,private val busqueda:Boolean) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
       // imageView.setImageResource(contenidos[position].imagen)
        Picasso.get().load(contenidos[position].urlImagen).into(imageView)

        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = AbsListView.LayoutParams(365, 490)


        // para mostrar el detalle :)
        imageView.setOnClickListener {
            // verificica si se esta usando en busqueda para ver si se activa la funcionalidad del clickOnListener de los elementos del gridview
            if(busqueda==true) {
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



            }
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


