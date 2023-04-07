package net.codebot.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Serializable
data class DirectoryResponse(
    var success: String,
    var body: List<Map<String, String>>
)

fun getDirectory(user: String, path: String): DirectoryResponse  {
    val baseURL = "http://ec2-18-218-223-84.us-east-2.compute.amazonaws.com:8080/directory/content"
    val data = URL("$baseURL?user=$user&path=$path").readText()
    val json = Json {prettyPrint = true}
    return json.decodeFromString<DirectoryResponse>(data)
}

fun putDirectory(user: String, path: String, name: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path&name=$name"))
        .PUT(HttpRequest.BodyPublishers.ofString(""))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun postDirectory(user: String, path: String, oldName: String, newName: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path&oldName=$oldName&newName=$newName"))
        .POST(HttpRequest.BodyPublishers.ofString(""))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun deleteDirectory(user: String, path: String): String  {
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?user=$user&path=$path"))
        .DELETE()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}
