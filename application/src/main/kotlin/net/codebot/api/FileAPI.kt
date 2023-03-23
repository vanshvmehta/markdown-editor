package net.codebot.api

import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class FileAPI {
    val baseURL = "http://localhost:8080/file/content"

    fun getFile(user: String, path: String): String  {
        val data = URL("$baseURL?user=$user&path=$path").readText()
        println(data)
        return ""
    }

    fun putFile(user: String, path: String, name: String, content: String): String  {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://httpbin.org/post"))
            .POST(HttpRequest.BodyPublishers.ofString(content))
            .build()
//        val data = URL("$baseURL?user=$user&path=$path&name=$name")
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        println(response.body())
        return ""
    }
}