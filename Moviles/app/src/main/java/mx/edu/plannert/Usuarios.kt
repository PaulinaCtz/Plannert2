package mx.edu.plannert
data class Usuarios(
    val contraseña: String = "",
    val email: String = "",
    val fechaNacimiento: String = "",
    val genero: String = "",
    val telefono: String = "",
    val usuario: String = "",
    val nombre : String
) {
    // Constructor sin argumentos
    constructor() : this("", "", "", "", "", "", "")
}