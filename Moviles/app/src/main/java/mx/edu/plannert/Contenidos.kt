package mx.edu.plannert

import android.os.Parcel
import android.os.Parcelable

data class Contenidos(
    val imagen: Int = 0,
    val titulo: String? = null,
    val descripcion: String? = null,
    val fecha: String? = null,
    val tipo: String? = null,
    val categoria: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(imagen)
        parcel.writeString(titulo)
        parcel.writeString(descripcion)
        parcel.writeString(fecha)
        parcel.writeString(tipo)
        parcel.writeString(categoria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contenidos> {
        override fun createFromParcel(parcel: Parcel): Contenidos {
            return Contenidos(parcel)
        }

        override fun newArray(size: Int): Array<Contenidos?> {
            return arrayOfNulls(size)
        }
    }
}

