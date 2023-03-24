package net.codebot.backend.service

import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@Service
class FileService {
    private final val basePath: String = "./fileContents"

    fun getFile(user: String, path: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val completePath = "$basePath/$user/$path"
            val content = File(completePath).readText()

            response["body"] = content
            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
        }

        return response
    }

    fun putFile(user: String, path: String, name: String, content: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val directoryPath = "$basePath/$user"
            if (!File(directoryPath).isDirectory) {
                Files.createDirectory(Paths.get(directoryPath))
            }

            var completePath = "$basePath/$user/$path/$name"
            if (path == "") completePath = "$basePath/$user/$name"

            println("Path: $completePath")

            Files.createFile(Paths.get(completePath))
            File(completePath).writeText(content)

            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun postFile(user: String, path: String, content: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val completePath = "$basePath/$user/$path"
            File(completePath).writeText(content)

            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun deleteFile(user: String, path: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val completePath = "$basePath/$user/$path"
            File(completePath).delete()

            response["success"] = "true"
        } catch (e: Exception) {
            println(e.message)
        }

        return response
    }

    fun renameFile(user: String, path: String, oldName: String, newName: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val oldPath = "$basePath/$user/$path/$oldName"
            val newPath = "$basePath/$user/$path/$newName"
            File(oldPath).renameTo(File(newPath))

            response["success"] = "true"
        } catch (e: Exception) {
            println(e.message)
        }

        return response
    }
}