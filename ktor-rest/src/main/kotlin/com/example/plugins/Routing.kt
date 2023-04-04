package com.example.plugins

import com.example.models.benevoles.Benevole
import com.example.models.festivals.Festival
import com.example.models.stands.Stand
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import com.serveur.bdd_MySql.Gestion
import io.ktor.server.request.*
import kotlin.collections.ArrayList

fun Application.configureRouting() {
    var maGestion = Gestion()
    var lesdatesFestivalDejaSelectionner: ArrayList<String>? = null
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/bdd") {

            route("/administrateurs") {
                get("/readAdministrateurs") {
                    var lesAdministrateurs = maGestion.readAdministrateurs()
                    when (lesAdministrateurs.size > 0) {
                        true -> {
                            call.respond(HttpStatusCode.OK, lesAdministrateurs)
                            println("readAdministrateur-> $lesAdministrateurs")

                        }

                        false -> {
                            call.respond(HttpStatusCode.NotFound)
                            println("readAdministrateur-> NotFound ")

                        }
                    }
                }
            }

            route("/festivals") {
                get("/readFestivals") {
                    var lesFestivals = maGestion.readFestivals()
                    println("readFestivals-> $lesFestivals ")
                    when (lesFestivals.size > 0) {
                        true -> {
                            call.respond(HttpStatusCode.OK, lesFestivals)
                        }

                        false -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get("/readFestival/{id}") {
                    var festival = maGestion.readFestival(call.parameters["id"]!!)
                    println("readFestivals/{Id}-> $festival ")
                    when (festival.id != -1) {
                        true -> {
                            call.respond(HttpStatusCode.OK, festival)
                            lesdatesFestivalDejaSelectionner?.clear()
                            lesdatesFestivalDejaSelectionner = festival.dates
                        }
                        false -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                put("/updateFestivals") {
                    val festival = call.receive<Festival>()
                    val etat = maGestion.updateFestivalsNom(festival)
                    when (etat) {
                        1 -> {
                            call.respond(HttpStatusCode.OK)
                            println(festival.toString())
                            println("${festival.id} , ${festival.nom} est bien modifier")
                            //compare les dates
                            if (festival.dates == lesdatesFestivalDejaSelectionner) {
                                println("par de changement pour les dates!")
                            } else {
                                println("changement de dates!")
                                maGestion.updateDatesFestival(festival,lesdatesFestivalDejaSelectionner)
                            }
                        }

                        else -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                post ("/addFestival"){
                    val festival = call.receive<Festival>()
                    println("/addFestival $festival")
                    val etat = maGestion.addFestival(festival)
                    println("resultatSet de addBenevole $etat")
                    when (etat) {
                        1 -> {
                            call.respond(HttpStatusCode.OK)
                            println("le festival ${festival} , est bien ajouter")
                        }
                        else -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                delete("/deleteFestival/{id}"){
                    maGestion.deleteFestival(call.parameters["id"]!!)
                    println("deleteFestival/{Id}-> $call.parameters[\"id\"]!!")
                }
            }

            route("/benevoles"){
                get("/readBenevolesFull") {
                    var lesBenevoles = maGestion.readBenevolesFull()
                    lesBenevoles.forEach { println(it) }
                    lesBenevoles = lesBenevoles.distinct() as ArrayList<Benevole>
                    lesBenevoles.sortBy { it.nom }
                    when (lesBenevoles.size > 0) {
                        true -> {
                            call.respond(HttpStatusCode.OK, lesBenevoles)
                            println("readBenevolesFull-> $lesBenevoles")
                        }

                        false -> {
                            call.respond(HttpStatusCode.NotFound)
                            println("readBenevolesFull-> NotFound ")
                        }
                    }
                }
                get("/readBenevole/{id}"){
                    var benevole = maGestion.readBenevole(call.parameters["id"]!!)
                    println("readBenevole/{Id}-> $benevole ")
                    when (benevole.id != -1) {
                        true->{
                            call.respond(HttpStatusCode.OK, benevole)
                        }
                        false->{
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                put("/updateBenevole"){
                    val benevole = call.receive<Benevole>()
                    println("/updateBenevole $benevole")
                    val etat = maGestion.updateBenevole(benevole)
                    when (etat) {
                        1 -> {
                            call.respond(HttpStatusCode.OK)
                            println("le bénévole ${benevole} , est bien modifier")
                        }
                        else -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                post ("/addBenevole"){
                    val benevole = call.receive<Benevole>()
                    println("/addBenevole $benevole")
                    val etat = maGestion.addBenevole(benevole)
                    println("resultatSet de addBenevole $etat")
                    when (etat) {
                        1 -> {
                            call.respond(HttpStatusCode.OK)
                            println("le bénévole ${benevole} , est bien ajouter")
                        }
                        else -> {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                delete("/deleteBenevole/{id}"){
                    maGestion.deleteBenevole(call.parameters["id"]!!)
                    println("deleteBenevole/{Id}-> $call.parameters[\"id\"]!!")
                    }
            }

            route("/stands"){
                get("/readStandsFull") {
                    var lesStands = maGestion.readStandsFull()
                    lesStands.forEach { println(it) }
                    lesStands = lesStands.distinct() as ArrayList<Stand>
                    lesStands.sortBy { it.id_festival}
                    when (lesStands.size > 0) {
                        true -> {
                            call.respond(HttpStatusCode.OK, lesStands)
                            println("readBenevolesFull-> $lesStands")
                        }
                        false -> {
                            call.respond(HttpStatusCode.NotFound)
                            println("readBenevolesFull-> NotFound ")
                        }
                    }
                }
                get("/readStand/{id}"){
                    var benevole = maGestion.readStand(call.parameters["id"]!!)
                    println("readBenevole/{Id}-> $benevole ")
                    when (benevole.id != -1) {
                        true->{
                            call.respond(HttpStatusCode.OK, benevole)
                        }
                        false->{
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                get("/searchStands/{term}"){
                    var stands = maGestion.searchStands(call.parameters["term"]!!)
                    println("searchStands/{term}-> $stands ")
                    when (stands.size>0) {
                        true->{
                            call.respond(HttpStatusCode.OK, stands)
                        }
                        false->{
                            call.respond(HttpStatusCode.NotFound)
                        }
                    }
                }
                /* put("/updateStand"){
                     val benevole = call.receive<Benevole>()
                     println("/updateBenevole $benevole")
                     val etat = maGestion.updateBenevole(benevole)
                     when (etat) {
                         1 -> {
                             call.respond(HttpStatusCode.OK)
                             println("le bénévole ${benevole} , est bien modifier")
                         }
                         else -> {
                             call.respond(HttpStatusCode.NotFound)
                         }
                     }
                 }

                 post ("/addStand"){
                     val benevole = call.receive<Benevole>()
                     println("/addBenevole $benevole")
                     val etat = maGestion.addBenevole(benevole)
                     println("resultatSet de addBenevole $etat")
                     when (etat) {
                         1 -> {
                             call.respond(HttpStatusCode.OK)
                             println("le bénévole ${benevole} , est bien ajouter")
                         }
                         else -> {
                             call.respond(HttpStatusCode.NotFound)
                         }
                     }
                 }
                 delete("/deleteStand/{id}"){
                     maGestion.deleteBenevole(call.parameters["id"]!!)
                     println("deleteBenevole/{Id}-> $call.parameters[\"id\"]!!")
                 }*/
            }

        }
    }
}
