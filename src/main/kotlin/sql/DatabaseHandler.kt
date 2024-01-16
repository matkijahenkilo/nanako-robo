package org.matkija.bot.sql

import org.matkija.bot.Server
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class DatabaseHandler(server: Server) {

    private val conn: Connection

    init {
        val url = "jdbc:postgresql://%s/%s".format(server.ip, server.database)
        val props = Properties()
        props.setProperty("user", server.user)
        props.setProperty("password", server.password)
        conn = DriverManager.getConnection(url, props)
    }

    fun readData(statement: String) : MutableList<LinksTable> {
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

    fun prepareStatement(statement: String) {
        val st = conn.prepareStatement(statement)
        st.executeUpdate()
        st.close()
    }

}