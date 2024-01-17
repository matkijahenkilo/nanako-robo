package org.matkija.bot.discordBot.helper

//isso é AMALDIÇOADO, arruma essa PORRA
object DatabaseAttributes {

    const val TABLE_NAME = "links"

    //attributes
    const val ID = "id"
    const val LINK = "link"
    const val ARTIST = "artist"
    const val DATE_ADDED = "dateAdded"

    //sql
    const val SELECT = "SELECT * FROM $TABLE_NAME"
    const val SELECT_WHERE_ID = "$SELECT WHERE $ID = %s"
    const val INSERT = "INSERT INTO $TABLE_NAME ($LINK, $ARTIST, $DATE_ADDED) VALUES ('%s', '%s', '%s')"
    const val DELETE = "DELETE FROM $TABLE_NAME WHERE $ID = %s"

}