package net.codebot.api

import java.net.URL

class DirectoryAPI {
    val baseURL = "http://localhost:8080/directory/content"

    fun getData(user: String, path: String): String  {
        val data = URL("$baseURL?user=$user&path=$path").readText()
        println(data)
        return ""
    }
}