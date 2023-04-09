package net.codebot.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Serializable
data class VerifyResponse(
    var success: Boolean,
    var message: String
)

fun verifyUser(user: String, pwd: String): Boolean {
    val baseURL = "http://ec2-18-118-140-38.us-east-2.compute.amazonaws.com:8080/user/verify"
    val client = HttpClient.newBuilder().build();
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$baseURL?name=$user&pwd=$pwd"))
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    val json = Json {prettyPrint = true}
    val parsedResponse = json.decodeFromString<VerifyResponse>(response.body())

    return parsedResponse.success
}