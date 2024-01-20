package org.matkija.bot.sql

import org.matkija.bot.discordBot.helper.DatabaseAttributes
import java.sql.Connection
import java.sql.DriverManager

class DatabaseHandler(dbName: String) {

    private val conn: Connection

    init {
        val url = "jdbc:sqlite:%s".format(dbName)
        conn = DriverManager.getConnection(url)
        runStatement(
            """
            CREATE TABLE IF NOT EXISTS links (
                id        INTEGER PRIMARY KEY AUTOINCREMENT,
                link      varchar(255) NOT NULL,
                artist    varchar(255),
                dateAdded date
            )
        """.trimIndent()
        )
    }

    fun readData(statement: String): List<LinksTable> {
        val st = conn.createStatement()
        val rs = st.executeQuery(statement)
        val ret = mutableListOf<LinksTable>()
        while (rs.next()) {
            ret.add(
                LinksTable(
                    id = rs.getLong(1),
                    link = rs.getString(2),
                    artist = rs.getString(3),
                    dateAdded = rs.getString(4)
                )
            )
        }
        rs.close()
        st.close()
        return ret
    }

    fun selectAll(): List<LinksTable> =
        readData(DatabaseAttributes.SELECT)

    fun selectAllLinks(): List<String> {
        val st = conn.createStatement()
        val rs = st.executeQuery(DatabaseAttributes.SELECT_LINK)
        val ret = mutableListOf<String>()
        while (rs.next()) {
            ret.add(rs.getString(1))
        }
        rs.close()
        st.close()
        return ret
    }

    fun runStatement(statement: String) {
        val st = conn.prepareStatement(statement)
        st.executeUpdate()
        st.close()
    }

}