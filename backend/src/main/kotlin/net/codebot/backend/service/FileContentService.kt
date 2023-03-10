package net.codebot.backend.service

import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class FileContentService {
    private final val basePath: String = "./fileContents"
    init {
        try {
            Files.createDirectory(Paths.get(basePath))
            println("Successfully created fileContents directory")
        } catch (e: IOException) {
            println("fileContents directory already exists")
        }
    }
    fun getFileContent(user: String, id: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val path = "$basePath/$user/$id"
            val content = File(path).readText()

            response["body"] = content
            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e.message)
        }

        return response
    }

    fun putFileContent(user: String, id: String, content: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val directoryPath = "$basePath/$user"
            val fileDirectory = File(directoryPath)
            if (!fileDirectory.isDirectory) {
                Files.createDirectory(Paths.get(directoryPath))
            }

            val path = "$basePath/$user/$id"
            Files.createFile(Paths.get(path))
            File(path).writeText(content)

            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun postFileContent(user: String, id: String, content: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val path = "$basePath/$user/$id"
            File(path).writeText(content)

            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun deleteFileContent(user: String, id: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val path = "$basePath/$user/$id"
            File(path).delete()

            response["success"] = "true"
        } catch (e: Exception) {
            println(e.message)
        }

        return response
    }
}