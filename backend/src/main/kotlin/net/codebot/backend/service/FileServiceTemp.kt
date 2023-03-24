//package net.codebot.backend.service
//
//import net.codebot.backend.dto.ContentDTO
//import org.springframework.stereotype.Service
//import java.sql.Connection
//import java.sql.PreparedStatement
//import java.sql.SQLException
//
//@Service
//class FileService(databaseSvc: DatabaseService) {
//    private final var conn: Connection? = databaseSvc.connection()
//
//    init {
//        try {
//            if (conn != null) {
//                val sql = "CREATE TABLE files (id VARCHAR(255), name VARCHAR(255), path VARCHAR(255), PRIMARY KEY (id))"
//                val query = conn!!.createStatement()
//                query.execute(sql)
//                println("Created File Table:")
//            }
//        } catch (ex: SQLException) {
//            println(ex.message)
//        }
//    }
//
//    fun getFiles(user: String): List<ContentDTO>? {
//        var retVal = mutableListOf<ContentDTO>()
//        try {
//            if (conn != null) {
//                val sql = "SELECT * FROM files"
//                val query = conn!!.createStatement()
//                val results = query.executeQuery(sql)
//                println("Fetched data:");
//                while (results.next()) {
//                    val id = results.getString("id")
//                    val name = results.getString("name")
//                    val path = results.getString("path")
//
//                    val file = ContentDTO(id, name, path)
//                    retVal.add(file)
//                }
//            }
//        } catch (ex: SQLException) {
//            println(ex.message)
//        }
//
//        return retVal
//    }
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