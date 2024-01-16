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

    fun readData(statement: String) : MutableList<String> {
        val st = conn.createStatement()
        val rs = st.executeQuery(statement)
        val ret = mutableListOf<String>()
        while (rs.next()) {
            ret.add(rs.getString(2))
        }
        rs.close()
        st.close()
        return ret
    }

}