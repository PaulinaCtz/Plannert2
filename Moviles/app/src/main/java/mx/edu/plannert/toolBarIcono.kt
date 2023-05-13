package mx.edu.plannert

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [toolBarIcono.newInstance] factory method to
 * create an instance of this fragment.
 */
class toolBarIcono : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_tool_bar_icono, container, false)

        var menu1: ImageView = view.findViewById(R.id.menup)
        var texto:TextView=view.findViewById(R.id.textoNavegacion)

        val fondo= arguments?.getBoolean("fondo")



        if(fondo==true){

            view.setBackgroundResource(R.color.moradoOscuro)
            view.invalidate()

        }
        menu1.setOnClickListener() {
            val intent = Intent(activity, menu::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment toolBarIcono.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            toolBarIcono().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun newInstance(fondo: Boolean): toolBarIcono {
            val fragment = toolBarIcono()
            val args = Bundle()


            args.putBoolean("fondo",fondo)
            fragment.arguments = args
            return fragment
        }
    }
}