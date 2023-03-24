package net.codebot.api

import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class DirectoryAPI {
    val baseURL = "http://localhost:8080/directory/content"

    fun getDirectory(user: String, path: String): String  {
        val data = URL("$baseURL?user=$user&path=$path").readText()
        return ""
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

    fun postFile(user: String, path: String, oldName: String, newName: String): String  {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$baseURL?user=$user&path=$path&oldName=$oldName&newName=$newName"))
            .POST(HttpRequest.BodyPublishers.ofString(""))
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
}