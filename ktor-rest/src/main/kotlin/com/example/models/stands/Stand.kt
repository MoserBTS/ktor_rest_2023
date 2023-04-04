package com.example.models.stands
import kotlinx.serialization.Serializable

@Serializable
data class Stand(
    var id: Int? = null,
    var id_festival:Int?=null,
    var nom: String? = null
)
{
    override fun toString(): String {
        return "Stand(id=$id,id_festival=$id_festival, nom=$nom)"
    }
}

