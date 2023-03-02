package net.codebot.backend.service

import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.Objects

@Service
class DatabaseService {
    fun connection(): Connection? {
        var conn: Connection? = null;
        try {
            val url = "jdbc:h2:./database"
            conn = DriverManager.getConnection(url)
            println("Connection to SQLite has been established.")
        } catch (e: SQLException) {
            println(e.message)
        }
        return conn
    }
}