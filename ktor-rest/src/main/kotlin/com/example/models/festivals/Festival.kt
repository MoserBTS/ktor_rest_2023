package com.example.models.festivals

import kotlinx.serialization.Serializable

@Serializable
data class Festival(
    var id: Int? = null,
    var nom: String? = null,
    var dates: ArrayList<String>? = null
) {
    override fun toString(): String {
        return "Festivals(id=$id,nom_festival=$nom, dates=$dates)"
    }
}

