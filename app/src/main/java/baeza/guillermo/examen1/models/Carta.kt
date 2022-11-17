package baeza.guillermo.examen1.models

import androidx.annotation.DrawableRes

data class Carta(
    val nombre: String,
    var likes: Int,
    @DrawableRes var imagen: Int,
    var descripcion: String
)
