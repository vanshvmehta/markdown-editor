package net.codebot.backend.service

import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.SQLException

@Service
class UserService(databaseSvc: DatabaseService) {
    private final var conn: Connection? = databaseSvc.connection()
    private final val basePath: String = "./fileContents"

    init {
        try {
            if (conn != null) {
                val sql = "CREATE TABLE TEMP (name VARCHAR(255), password VARCHAR(255), PRIMARY KEY (name))"
                val query = conn!!.createStatement()
                query.execute(sql)
                println("Created User Table:")
            }
        } catch (ex: SQLException) {
            println(ex.message)
        }
    }

    fun getUser(username: String, userPassword: String): MutableMap<String, Any> {
        var retVal = mutableMapOf<String, Any>()
        retVal["success"] = false
        retVal["message"] = "login failed"
        try {
            if (conn != null) {
                val sql = "SELECT * FROM TEMP WHERE name = '$username'"
                println(sql)
                val query = conn!!.createStatement()
                try {
                    val results = query.executeQuery(sql)

                    var isFound = false
                    while (results.next()) {
                        val password = results.getString("password")
                        println(password)
                        if (password == userPassword) {
                            retVal["success"] = true
                        }
                        isFound = true
                    }

                    if (!isFound) {
                        val sql = "INSERT INTO TEMP VALUES ('$username', '$userPassword')"
                        val query = conn!!.createStatement()
                        query.execute(sql)

                        retVal["success"] = true
                        retVal["message"] = "new user created"
                    }
                } catch (ex: Exception) {
                    println("Creating new results")
                    val sql = "INSERT INTO TEMP VALUES ('$username', '$userPassword')"
                    val query = conn!!.createStatement()
                    query.execute(sql)

                    retVal["success"] = true
                    retVal["message"] = "new user created"
                }

                if (retVal["success"] == true) {
                    val directoryPath = "$basePath/$username"
                    if (!File(directoryPath).isDirectory) {
                        Files.createDirectory(Paths.get(directoryPath))
                        Files.createDirectory(Paths.get("$basePath/$username/root"))
                        Files.createFile(Paths.get("$basePath/$username/config.txt"))
                    }
                }
            }
        } catch (ex: SQLException) {
            println(ex.message)
        }

        return retVal
    }
}
