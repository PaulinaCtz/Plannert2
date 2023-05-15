package mx.edu.plannert

data class Lista(
    val categoria: String,
    val favorita: Boolean,
    val icono: String,
    val idUsuario: String,
    val nombre: String,
    val tipo: String,
    val usuario: String,
    val contenidos:ArrayList<DetallesPeliculas>
) {
    constructor() : this("", false, "", "", "", "", "", ArrayList())
}
