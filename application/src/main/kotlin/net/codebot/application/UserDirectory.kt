package net.codebot.application

import net.codebot.api.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

fun deleteDirectory(path: String) {
    val file = File(path)
    try {
        file.deleteRecursively()
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
    println("Downloading files for user: " + user)
    val rootData = getDirectory(user, "root").body
    // for every file from the user's online directory
    for (obj: Map<String, String> in rootData) {
        val tempFile = File(userPath.resolve(Paths.get(obj.get("name"))).toString())
        tempFile.writeText(getFile(user, "root/" + obj.get("name")).body)
    }
}

fun updateFile(user: String, path: String?) {
    if (user.isEmpty()) {
        println("Guest user requires no update")
        return
    }
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
            println("Updating file: " + file.name)
            println("For user: " + user)
            postFile(user, "root/" + file.name, file.readText())
            return
        }
    }
    println("No update for remote directory.")
}

fun uploadFile(user: String, file: File) {
    if (user.isEmpty()) {
        println("Guest user requires no upload")
        return
    }
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
            println("Uploading file: " + file.name)
            println("For user: " + user)
            putFile(user, "root", file.name, file.readText())
            postFile(user, "root/" + file.name, file.readText())
            return
        }
    }
    println("No upload for remote directory.")
}

fun delFile(user : String, path : String?) {
    if (user.isEmpty()) {
        println("Guest user requires no delete")
        return
    }
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
            println("Deleting file: " + file.name)
            println("For user: " + user)
            deleteFile(user, "root/" + file.name)
            return
        }
    }
    println("No delete for remote directory.")
}