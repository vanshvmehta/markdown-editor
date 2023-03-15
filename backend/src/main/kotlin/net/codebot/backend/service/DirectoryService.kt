package net.codebot.backend.service

import net.codebot.backend.dto.ContentDTO
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@Service
class DirectoryService {
    private final val basePath: String = "./fileContents"
    init {
        try {
            Files.createDirectory(Paths.get(basePath))
            println("Successfully created fileContents directory")
        } catch (e: IOException) {
            println("fileContents directory already exists")
        }
    }
    fun getDirectory(user: String, path: String): MutableMap<String, Any?> {
        val response = mutableMapOf<String, Any?>()
        response["success"] = "false"

        try {
            val completePath = "$basePath/$user/$path"
            val directory = File(path)

            val contents: MutableList<ContentDTO> = mutableListOf()
            if (directory.isDirectory) {
                for (file in directory.listFiles()!!) {
                    if (file.isDirectory) contents.add(ContentDTO("directory", file.name))
                    else contents.add(ContentDTO("file", file.name))
                }
            } else throw Exception("path is not a directory")

            response["body"] = contents
            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
        }

        return response
    }

    fun createDirectory(user: String, path: String, name: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            Files.createDirectory(Paths.get("$basePath/$user/$path/$name"))
            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun renameDirectory(user: String, path: String, oldName: String, newName: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val oldPath = "$basePath/$user/$path/$oldName"
            val newPath = "$basePath/$user/$path/$newName"
            val oldDirectory = File(oldPath)
            val newDirectory = File(newPath)

            oldDirectory.renameTo(newDirectory)
            response["success"] = "true"
        } catch (e: Exception) {
            response["message"] = e.message.toString()
            println(e)
        }

        return response
    }

    fun deleteDirectory(user: String, path: String): MutableMap<String, String> {
        val response = mutableMapOf<String, String>()
        response["success"] = "false"

        try {
            val completePath = "$basePath/$user/$path"
            File(completePath).deleteRecursively()

            response["success"] = "true"
        } catch (e: Exception) {
            println(e.message)
        }

        return response
    }
}