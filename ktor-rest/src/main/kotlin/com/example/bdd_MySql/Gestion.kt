package com.serveur.bdd_MySql

import com.example.models.administrateurs.Administrateur
import com.example.models.benevoles.Benevole
import com.example.models.festivals.Festival


class Gestion() {
    var laConnexion = Connexion("jdbc:mysql://localhost/mydb", "root", "")
    var arDates = ArrayList<String>()

    init {
        laConnexion.voirLesBdd().forEach { bdd -> println(bdd) }
    }


    fun readFestivals(): ArrayList<Festival> {
        var arFestivals = ArrayList<Festival>()
        var festivals = Festival()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "SELECT f.id,f.nom,df.dates\n" +
                        "FROM festivals f\n" +
                        "JOIN dates_festivals df ON f.id = df.festivals_id_festival"
            )
        var rs = prepStatement.executeQuery()
        var existe = -1
        var arDates = ArrayList<String>()
        while (rs.next()) {
            var idFestival = rs.getInt("f.id")

             for (i in 0..arFestivals.size - 1) {
                  println("idFestival $idFestival == festival.id ${arFestivals.get(i).id}")
                  if (idFestival == arFestivals.get(i).id) {
                      existe = i
                      break
                  } else
                      existe = -1
              }

            if (existe > -1) {
                println("existe")
                arFestivals.get(existe).dates!!.add(rs.getString("dates"))
            } else {
                println("n'existe pas")
                var arDates = ArrayList<String>()
                arDates.add(rs.getString("dates"))
                festivals = Festival()
                festivals.id = idFestival
                festivals.nom = (rs.getString("nom"))
                festivals.dates = arDates
                arFestivals.add(festivals)
            }
        }
        return arFestivals
    }


    fun readFestival(id: String): Festival {
        var festival = Festival()
        var arDates = ArrayList<String>()
        var idf: Int = -1
        var nom: String = ""
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "SELECT f.id,f.nom,df.dates\n" +
                        "FROM festivals f\n" +
                        "JOIN dates_festivals df ON f.id = df.festivals_id_festival" +
                        " where f.id=?"
            )
        prepStatement.setString(1, id)
        var rs = prepStatement.executeQuery()
        while (rs.next()) {
            if (arDates.isEmpty()) {
                idf = rs.getInt("id")
                nom = (rs.getString("nom"))
            }
            arDates.add(rs.getString("dates"))
        }
        festival.id = idf
        festival.nom = nom
        festival.dates = arDates
        return festival
    }


    fun updateFestivalsNom(festival: Festival): Int {
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement("UPDATE festivals SET nom=? WHERE id=?")
        prepStatement.setString(1, festival.nom)
        prepStatement.setInt(2, festival.id!!)
        return prepStatement.executeUpdate()
    }

    fun updateDatesFestival(festival: Festival, lesdatesFestivalDejaSelectionner: ArrayList<String>?) {
        var lesNouvellesDates = festival.dates
        //ajouter les nouvelles dates
        lesNouvellesDates!!.forEach {
            if (!lesdatesFestivalDejaSelectionner!!.contains(it)) {
                //ajouter la date
                var prepStatement = laConnexion.getConnexion()
                    .prepareStatement("INSERT INTO dates_festivals (festivals_id_festival, dates) VALUES (?, ?)")
                prepStatement.setInt(1, festival.id!!)
                prepStatement.setString(2, it)
                prepStatement.executeUpdate()
            }
        }
        //supprime les ancienne dates
        println(lesdatesFestivalDejaSelectionner)
        lesdatesFestivalDejaSelectionner!!.forEach {
            if (!lesNouvellesDates!!.contains(it)) {
                println("supprime")
                var prepStatement = laConnexion.getConnexion()
                    .prepareStatement("DELETE FROM dates_festivals  WHERE (festivals_id_festival=? AND dates=?)")
                prepStatement.setInt(1, festival.id!!)
                prepStatement.setString(2, it)
                println(prepStatement.toString())
                prepStatement.execute()
            }
        }

    }

    //ok
    fun readAdministrateurs(): ArrayList<Administrateur> {
        var administrateurs = ArrayList<Administrateur>()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "SELECT * FROM administrateurs ORDER BY id"
            )
        var rs = prepStatement.executeQuery()
        while (rs.next()) {
            administrateurs.add(
                Administrateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("motdepasse")
                )
            )
        }
        println("dans gestion")
        administrateurs.forEach { println(it.login) }
        return administrateurs
    }

    //ok
    fun readBenevolesFull(): ArrayList<Benevole> {
        var arBenevoles = ArrayList<Benevole>()
        var arIdStand = ArrayList<Int>()
        var benevole = Benevole()
        var arDatesBenevoles = ArrayList<String>()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "SELECT DISTINCT b.id ,f.id,b.nom,b.prenom,b.autorisation\n" +
                        "FROM benevoles b\n" +
                        "    JOIN festivals f ON f.id = b.festivals_id_festival\n" +
                        "ORDER BY b.id"
            )
        var rs = prepStatement.executeQuery()

        while (rs.next()) {
            benevole = Benevole()
            benevole.id = rs.getInt("b.id")
            benevole.id_festival = rs.getInt("f.id")
            benevole.nom = (rs.getString("b.nom"))
            benevole.prenom = (rs.getString("prenom"))
            benevole.autorisation = (rs.getInt("autorisation"))
            benevole.dates= ArrayList<String>()
            var prepStatementDates = laConnexion.getConnexion()
                .prepareStatement(
                    "SELECT df.dates\n" +
                            "FROM benevoles b\n" +
                            "         JOIN dates_joint_benevoles djb on b.id = djb.benevole_id_benevole\n" +
                            "         JOIN dates_festivals df on djb.dates_festivals_id = df.id\n" +
                            "WHERE b.id = ?\n" +
                            "ORDER BY df.dates DESC"
                )
            prepStatementDates.setInt(1, benevole.id!!)
            var rsDates = prepStatementDates.executeQuery()
            while (rsDates.next()) {
                benevole.dates?.add(rsDates.getString("dates"))
            }
            arBenevoles.add(benevole)

        }
        return arBenevoles
    }

    fun readBenevolesSimple(): ArrayList<Benevole> {
        var arBenevoles = ArrayList<Benevole>()
        var arIdStand = ArrayList<Int>()
        var benevole = Benevole()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "SELECT DISTINCT b.id,\n" +
                        "       f.id,\n" +
                        "       b.nom,\n" +
                        "       b.prenom,\n" +
                        "       b.autorisation\n" +
                        "FROM benevoles b\n" +
                        "         JOIN benevoles_joint_stands bjs ON bjs.benevoles_id_benevoles = b.id\n" +
                        "         JOIN stands s ON s.id = bjs.stands_id_stands\n" +
                        "         JOIN festivals f ON f.id = s.festivals_id_festival\n" +
                        "ORDER BY s.id;"
            )
        var rs = prepStatement.executeQuery()


        /*   prepStatement = laConnexion.getConnexion()
               .prepareStatement(
                   "SELECT db.dates\n" +
                           "    FROM benevoles b\n" +
                           "JOIN dates_joint_benevoles djb on b.id = djb.benevole_id_benevole\n" +
                           "JOIN dates_benevoles db on db.id = djb.date_benevole_id_date\n" +
                           "WHERE b.id=?" +
                           "ORDER BY db.dates DESC"
               )*/
        while (rs.next()) {
            benevole = Benevole()
            benevole.id = rs.getInt("b.id")
            benevole.id_festival = rs.getInt("f.id")
            benevole.nom = (rs.getString("b.nom"))
            benevole.prenom = (rs.getString("prenom"))
            benevole.autorisation = (rs.getInt("autorisation"))
            arBenevoles.add(benevole)
        }
        return arBenevoles
    }

    //ok
    fun readBenevole(id: String): Benevole {
        var benevole = Benevole()
        var arDates = ArrayList<String>()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                " SELECT DISTINCT  b.id,f.id,b.nom,b.prenom,b.email,b.login,b.motdepasse,b.autorisation\n" +
                        "FROM benevoles b\n" +
                        "JOIN festivals f  ON f.id = b.festivals_id_festival\n" +
                        "WHERE b.id=?"
            )
        prepStatement.setString(1, id)
        var rs = prepStatement.executeQuery()
        while (rs.next()) {
            benevole.id = (rs.getInt("b.id"))
            benevole.id_festival = rs.getInt("f.id")
            benevole.nom = rs.getString("b.nom")
            benevole.prenom = rs.getString("b.prenom")
            benevole.email = rs.getString("b.email")
            benevole.login = rs.getString("b.login")
            benevole.motdepasse = rs.getString("b.motdepasse")
            benevole.autorisation = rs.getInt("b.autorisation")
            benevole.dates=arDates
        }

        prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "  SELECT df.dates\n" +
                        "                FROM benevoles b\n" +
                        "        JOIN dates_joint_benevoles djb on b.id = djb.benevole_id_benevole\n" +
                        "        JOIN dates_festivals df on djb.dates_festivals_id = df.id\n" +
                        "        WHERE b.id = ?"
            )
        prepStatement.setString(1, id)
        rs = prepStatement.executeQuery()
        while (rs.next()) {
            arDates.add(rs.getString("dates"))
        }
        println(benevole)
        return benevole
    }

  /*  fun readBenevole(id: String): Benevole {
        var benevole = Benevole()
        var arDates = ArrayList<String>()
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                " SELECT DISTINCT b.id,\n" +
                        "                f.id,\n" +
                        "                b.nom,\n" +
                        "                b.prenom,\n" +
                        "                b.email,\n" +
                        "                b.login,\n" +
                        "                b.motdepasse,\n" +
                        "                b.autorisation,\n" +
                        "                df.dates,\n" +
                        "                s.nom\n" +
                        "FROM benevoles b\n" +
                        "         JOIN dates_joint_benevoles djb ON b.id = djb.benevole_id_benevole\n" +
                        "         JOIN dates_festivals df on df.id = djb.dates_festivals_id\n" +
                        "         JOIN festivals f on f.id = df.festivals_id_festival\n" +
                        "         JOIN benevoles_joint_stands bjs ON b.id = bjs.benevoles_id_benevoles\n" +
                        "         JOIN stands s on s.id = bjs.stands_id_stands\n" +
                        "WHERE b.id=?"
            )
        prepStatement.setString(1, id)
        var rs = prepStatement.executeQuery()
        while (rs.next()) {
            if (!(arDates.contains(rs.getString("dates"))))
                arDates.add(rs.getString("dates"))
            benevole.id = (rs.getInt("b.id"))
            benevole.id_festival = rs.getInt("f.id")
            benevole.nom = rs.getString("b.nom")
            benevole.prenom = rs.getString("b.prenom")
            benevole.email = rs.getString("b.email")
            benevole.login = rs.getString("b.login")
            benevole.motdepasse = rs.getString("b.motdepasse")
            benevole.autorisation = rs.getInt("b.autorisation")
        }
        benevole.dates = arDates

        return benevole
    }*/

    /*
        fun readBenevolesFull(): ArrayList<Benevole> {
            println("dans readBenevolesFull()")
            var arBenevoles = ArrayList<Benevole>()
            var arIdStand = ArrayList<Int>()
            var benevole = Benevole()
            var prepStatement = laConnexion.getConnexion()
                .prepareStatement(
                    " SELECT            b.id,\n" +
                            "                f.id,\n" +
                            "                b.nom,\n" +
                            "                b.prenom,\n" +
                            "                b.email,\n" +
                            "                b.login,\n" +
                            "                b.motdepasse,\n" +
                            "                b.autorisation,\n" +
                            "                df.dates,\n" +
                            "                s.nom\n" +
                            "FROM benevoles b\n" +
                            "         JOIN dates_joint_benevoles djb ON b.id = djb.benevole_id_benevole\n" +
                            "         JOIN dates_festivals df on df.id = djb.dates_festivals_id\n" +
                            "         JOIN festivals f on f.id = df.festivals_id_festival\n" +
                            "         JOIN benevoles_joint_stands bjs ON b.id = bjs.benevoles_id_benevoles\n" +
                            "         JOIN stands s on s.id = bjs.stands_id_stands"
                )
            var rs = prepStatement.executeQuery()
            var existeIdBenevole = -1
            while (rs.next()) {
                var idBenevole = rs.getInt("b.id")
                arBenevoles.forEachIndexed { index, benevole ->
                    if (idBenevole == benevole.id) {
                        existeIdBenevole = index
                        return@forEachIndexed
                    }
                    else
                        existeIdBenevole = -1
                }
                if (existeIdBenevole != -1) {
                    if (!(arBenevoles.get(existeIdBenevole).dates!!.contains(rs.getString("dates"))))
                        arBenevoles.get(existeIdBenevole).dates!!.add(rs.getString("dates"))
                }
                if (existeIdBenevole == -1) {
                    var arDates = ArrayList<String>()
                    arDates.add(rs.getString("dates"))
                    benevole = Benevole()
                    benevole.id = idBenevole
                    benevole.id_festival = rs.getInt("f.id")
                    benevole.nom = (rs.getString("b.nom"))
                    benevole.prenom = (rs.getString("prenom"))
                    benevole.email = (rs.getString("email"))
                    benevole.login = (rs.getString("login"))
                    benevole.motdepasse = (rs.getString("motdepasse"))
                    benevole.autorisation = (rs.getInt("autorisation"))
                    benevole.dates = arDates
                    arBenevoles.add(benevole)
                }
            }
            println(arBenevoles)
            return arBenevoles
        }
    */

    fun updateBenevole(benevole: Benevole): Int {
        helpers_miseAjourJointureBenevolesDates(benevole)
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "UPDATE benevoles " +
                        "SET nom=?,prenom=?,email=?,login=?,motdepasse=?,autorisation=? " +
                        "WHERE id=?"
            )
        prepStatement.setString(1, benevole.nom)
        prepStatement.setString(2, benevole.prenom)
        prepStatement.setString(3, benevole.email)
        prepStatement.setString(4, benevole.login)
        prepStatement.setString(5, benevole.motdepasse)
        prepStatement.setInt(6, benevole.autorisation!!)
        prepStatement.setInt(7, benevole.id!!)
        return prepStatement.executeUpdate()
    }

    fun addBenevole(benevole: Benevole): Int {
        helpers_miseAjourJointureBenevolesDates(benevole)
        var prepStatement = laConnexion.getConnexion()
            .prepareStatement(
                "INSERT \n" +
                        "INTO benevoles (nom, prenom, email, login, motdepasse, autorisation)\n" +
                        "VALUES (?,?,?,?,?,?)"
            )
        prepStatement.setString(1, benevole.nom)
        prepStatement.setString(2, benevole.prenom)
        prepStatement.setString(3, benevole.email)
        prepStatement.setString(4, benevole.login)
        prepStatement.setString(5, benevole.motdepasse)
        prepStatement.setInt(6, benevole.autorisation!!)
        println(prepStatement.toString())
        return prepStatement.executeUpdate()
    }

    fun helpers_miseAjourJointureBenevolesDates(benevole: Benevole) {
        //controle si une jointure date benevole existe pour toutes les dates
        benevole.dates!!.forEach {
            var prepStatement = laConnexion.getConnexion()
                .prepareStatement(
                    " SELECT djb.id_date_joint_benevole\n" +
                            "    FROM dates_joint_benevoles djb\n" +
                            "WHERE (djb.benevole_id_benevole=?)\n" +
                            "    AND\n" +
                            "       (djb.dates_festivals_id=(SELECT df.id\n" +
                            "        FROM dates_festivals df\n" +
                            "        WHERE df.dates = ?))"
                )
            //attention ici remonter par jinture acev la table  festival->dates_festival (pour concerver la clef festival
            prepStatement.setInt(1, benevole.id!!)
            prepStatement.setString(2, it)
            var rs = prepStatement.executeQuery()
            if (!rs.next()) {
                //ajoute de la nouvelle jointure date du benevole
                println("ajout dans la table de jointure date benvole")
                //A FINIR !!!! 26/03/2023 A 22H58
                prepStatement = laConnexion.getConnexion()
                    .prepareStatement(
                        " INSERT\n" +
                                "    INTO dates_joint_benevoles  (benevole_id_benevole, dates_festivals_id)\n" +
                                "        VALUES (?,\n" +
                                "                   (SELECT df.id\n" +
                                "                FROM dates_festivals df\n" +
                                "                WHERE df.dates = ?));"
                    )
                prepStatement.setInt(1, benevole.id!!)
                prepStatement.setString(2, it)

            }


        }

        /*    benevole.dates!!.forEach {
                prepStatement = laConnexion.getConnexion()
                    .prepareStatement(
                        "INSERT\n" +
                                "INTO dates_joint_benevoles (date_benevole_id_date, benevole_id_benevole)\n" +
                                "VALUES ((SELECT  db.id\n" +
                                "FROM dates_benevoles db\n" +
                                "WHERE db.dates=?), ?)"
                    )
                prepStatement.setString(1, it)
                prepStatement.setInt(2, benevole.id!!)
                rs = prepStatement.execute()
            }*/
    }

    /* fun lireUnEtudiants(id: String): Etudiant {
     lateinit var etudiant: Etudiant
     var prepStatement = laConnexion.getConnexion().prepareStatement("SELECT * from info where id=?")
     prepStatement.setString(1, id)
     var rs = prepStatement.executeQuery()
     while (rs.next()) {
         etudiant = Etudiant(
             rs.getInt("id"),

             rs.getString("firstname"),
             rs.getString("lastname"),
             rs.getString("age"),
             rs.getString("image")
         )
     }
     println(etudiant)
     return etudiant
    }

    fun ajoutEtudiant(etudiant: Etudiant): Int {
     var prepStatement = laConnexion.getConnexion()
         .prepareStatement("insert into etudiants.info(firstname, lastname, age, image) VALUES (?, ?, ?, ?) ")
     prepStatement.setString(1, etudiant.firstname)
     prepStatement.setString(2, etudiant.lastname)
     prepStatement.setString(3, etudiant.age)
     prepStatement.setString(4, etudiant.image)
     return prepStatement.executeUpdate()
    }

    fun supprimerUnEtudiant(id: Int): Int {
     var prepStatement = laConnexion.getConnexion()
         .prepareStatement("delete from etudiants.info where id=?")
     prepStatement.setInt(1, id)
     return prepStatement.executeUpdate()
    }

    fun miseAJourEtudiant(etudiant: Etudiant): Int {
     var prepStatement = laConnexion.getConnexion()
         .prepareStatement("UPDATE info SET firstname = ?,lastname = ?,age = ?,image = ? WHERE id = ?")
     prepStatement.setString(1, etudiant.firstname)
     prepStatement.setString(2, etudiant.lastname)
     prepStatement.setString(3, etudiant.age)
     prepStatement.setString(4, etudiant.image)
     prepStatement.setInt(5, etudiant.id!!)
     return prepStatement.executeUpdate()
    }*/

}