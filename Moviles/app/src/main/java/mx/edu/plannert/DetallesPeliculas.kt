package mx.edu.plannert

import android.os.Parcel
import android.os.Parcelable

data class DetallesPeliculas(
    var categoria: String,
    var descripcion: String,
    var fecha: String,
    var nombreImagen: String,
    var tipo: String,
    var titulo: String,
    var urlImagen: String
) : Parcelable {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoria)
        parcel.writeString(descripcion)
        parcel.writeString(fecha)
        parcel.writeString(nombreImagen)
        parcel.writeString(tipo)
        parcel.writeString(titulo)
        parcel.writeString(urlImagen)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetallesPeliculas> {
        override fun createFromParcel(parcel: Parcel): DetallesPeliculas {
            return DetallesPeliculas(parcel)
        }

        override fun newArray(size: Int): Array<DetallesPeliculas?> {
            return arrayOfNulls(size)
        }
    }
}
