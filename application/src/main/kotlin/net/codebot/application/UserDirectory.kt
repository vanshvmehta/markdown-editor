package net.codebot.application

import net.codebot.api.getDirectory
import net.codebot.api.getFile
import net.codebot.api.postFile
import net.codebot.api.putFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

fun deleteDirectory(path: String) {
    val file = File(path)
    try {
        file.deleteRecursively()
        println("Directory deleted successfully.")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun getUserDirectory (user: String) {
    val rootPath = Paths.get(System.getProperty("user.home"))
    val partialPath = Paths.get(".MarkDown/" + user)
    val resolvedPath: Path = rootPath.resolve(partialPath)

    // clean old user data
    val isDir = Files.isDirectory(resolvedPath)
    if (isDir) {
        deleteDirectory(resolvedPath.toString())
    }

    // create new directory
    val userPath = resolvedPath.resolve(Paths.get("root"))
    Files.createDirectories(userPath)

    // grab data from backend and write to user's directory
    val rootData = getDirectory(user, "root").body
    // for every file from the user's online directory
    for (obj: Map<String, String> in rootData) {
        val tempFile = File(userPath.resolve(Paths.get(obj.get("name"))).toString())
        tempFile.writeText(getFile(user, "root/" + obj.get("name")).body)
    }
}

fun updateFile(user: String, path: String?) {
    val file = File(path)
    val realPath = Paths.get(file.path)

    val mdPath = Paths.get(System.getProperty("user.home"))
        .resolve(Paths.get(".Markdown"))
        .resolve(Paths.get(user))
        .resolve(Paths.get("root"))
        .resolve(Paths.get(file.name))

    println("Comparing paths:")
    println(realPath)
    println(mdPath)

    if (Files.exists(mdPath)) {
        if (Files.isSameFile(mdPath, realPath)) {
            println("Updating file: " + file.name + " for user: " + user + "!")
            postFile(user, "root/" + file.name, file.readText())
        }
    }
}

fun uploadFile(user: String, file: File) {
    val realPath = Paths.get(file.path)

    val mdPath = Paths.get(System.getProperty("user.home"))
        .resolve(Paths.get(".Markdown"))
        .resolve(Paths.get(user))
        .resolve(Paths.get("root"))
        .resolve(Paths.get(file.name))

    println("Comparing paths:")
    println(realPath)
    println(mdPath)

    if (Files.exists(mdPath)) {
        if (Files.isSameFile(mdPath, realPath)) {
            putFile(user, "root", file.name, file.readText())
            postFile(user, "root/" + file.name, file.readText())
        }
    }
}
