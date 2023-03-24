package net.codebot.backend.service

import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.SQLException

@Service
class UserService(databaseSvc: DatabaseService) {
    private final var conn: Connection? = databaseSvc.connection()

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
        try {
            if (conn != null) {
                val sql = "SELECT * FROM TEMP WHERE name = '$username'"
                println(sql)
                val query = conn!!.createStatement()
                try {
                    val results = query.executeQuery(sql)

                    var isFound = false
                    var isCorrect = false
                    while (results.next()) {
                        val password = results.getString("password")
                        println(password)
                        if (password == userPassword) {
                            retVal["success"] = true
                            isCorrect = true
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
            }
        } catch (ex: SQLException) {
            println(ex.message)
        }

        return retVal
    }
}
//
//
//    fun createFile(file: ContentDTO, user: String): MutableMap<String, String> {
//        val response = mutableMapOf<String, String>()
//        response["success"] = "false"
//        try {
//            if (conn != null) {
////                val sql = "insert into files values ('" + file.id + "', '" + file.name + "', '" + file.path + "')"
//                val sql = "INSERT INTO files VALUES (?, ?, ?)"
//                val preparedStatement: PreparedStatement = conn!!.prepareStatement(sql)
//                preparedStatement.setString(1, file.id)
//                preparedStatement.setString(2, file.name)
//                preparedStatement.setString(3, file.path)
//
//                val result: Int = preparedStatement.executeUpdate()
//
//                if (result != 0) {
//                    println("Successfully added file to the db")
//                    response["success"] = "true"
//                } else {
//                    println("Failed to add file to the db")
//                }
//            }
//        } catch (ex: SQLException) {
//            response["message"] = ex.message.toString()
//            println(ex.message)
//        }
//
//        return response
//    }
//
//    fun postFile(file: ContentDTO, user: String): MutableMap<String, String> {
//        val response = mutableMapOf<String, String>()
//        response["success"] = "false"
//        try {
//            if (conn != null) {
////                val sql = "insert into files values ('" + file.id + "', '" + file.name + "', '" + file.path + "')"
//                val sql = "UPDATE files SET name = ? AND path = ? WHERE id = ?"
//                val preparedStatement: PreparedStatement = conn!!.prepareStatement(sql)
//                preparedStatement.setString(3, file.id)
//                preparedStatement.setString(1, file.name)
//                preparedStatement.setString(2, file.path)
//
//                val result: Int = preparedStatement.executeUpdate()
//
//                if (result != 0) {
//                    println("Successfully updated file in the db")
//                    response["success"] = "true"
//                } else {
//                    println("Failed to update file in the db")
//                }
//            }
//        } catch (ex: SQLException) {
//            response["message"] = ex.message.toString()
//            println(ex.message)
//        }
//
//        return response
//    }
//
//    fun deleteFile(id: String, user: String): MutableMap<String, String> {
//        val response = mutableMapOf<String, String>()
//        response["success"] = "false"
//        try {
//            if (conn != null) {
//                val sql = "DELETE FROM files WHERE id = ?"
//                val preparedStatement: PreparedStatement = conn!!.prepareStatement(sql)
//                preparedStatement.setString(1, id)
//
//                val result: Int = preparedStatement.executeUpdate()
//
//                if (result != 0) {
//                    println("Successfully delete file from the db")
//                    response["success"] = "true"
//                } else {
//                    println("Failed to delete file from the db")
//                }
//            }
//        } catch (ex: SQLException) {
//            response["message"] = ex.message.toString()
//            println(ex.message)
//        }
//
//        return response
//    }
//}