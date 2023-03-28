package com.example.models.benevoles

import kotlinx.serialization.Serializable

@Serializable
data class Benevole(var id: Int?=null,
                    var id_festival:Int?=null,
                    var nom: String?=null,
                    var prenom: String?=null,
                    var email: String?=null,
                    var login: String?=null,
                    var motdepasse: String?=null,
                    var autorisation: Int?=null,
                    var dates: ArrayList<String>? = null

) {
    override fun toString(): String {
        return "Benevole(id=$id,id_festival=$id_festival, nom=$nom, prenom=$prenom, email=$email, login=$login, motdepasse=$motdepasse, autorisation=$autorisation, dates=$dates)"
    }
}
