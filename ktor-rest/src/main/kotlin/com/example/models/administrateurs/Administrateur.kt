package com.example.models.administrateurs

import kotlinx.serialization.Serializable

@Serializable
data class Administrateur(
    var id: Int? = null,
    var nom: String? = null,
    var prenom: String? = null,
    var email: String? = null,
    var login: String? = null,
    var motdepasse: String? = null,
) {
        override fun toString(): String {
            return "Festivals(id=$id,nom=$nom,prenom=$prenom,email=$email,login=$login,motdepasse=$motdepasse)"
        }
}

