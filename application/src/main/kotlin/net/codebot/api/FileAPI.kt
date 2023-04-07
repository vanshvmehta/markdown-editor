package net.codebot.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


val baseURL = "http://ec2-18-218-223-84.us-east-2.compute.amazonaws.com:8080/file/content"
val renameURL = "http://localhost:8080/file/rename"

@Serializable
data class FileResponse(
    var success: String,
    var body: String
)

fun getFile(user: String, path: String?): FileResponse  {
    val data = URL("$baseURL?user=$user&path=$path").readText()
    val json = Json {prettyPrint = true}
    return json.decodeFromString<FileResponse>(data)
}

fun putFile(user: String, path: String, name: String, content: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path&name=$name"))
        .PUT(HttpRequest.BodyPublishers.ofString(content))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun postFile(user: String, path: String, content: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path"))
        .POST(HttpRequest.BodyPublishers.ofString(content))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun deleteFile(user: String, path: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path"))
        .DELETE()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun renameFile(user: String, path: String, oldName: String, newName: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$renameURL?user=$user&path=$path&oldName=$oldName&newName=$newName"))
        .DELETE()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}