package net.codebot.backend

import net.codebot.backend.service.DatabaseService
import net.codebot.backend.service.DirectoryService
import net.codebot.backend.service.UserService
import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.round


class DirectoryTests {
    val directorySvc = DirectoryService()
    @Test
    fun createDirectory() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        val parsedResponse = JSONObject(directorySvc.createDirectory(user, path, name))
        val success: Boolean = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun getDirectory() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        var parsedResponse = JSONObject(directorySvc.createDirectory(user, path, name))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(directorySvc.getDirectory(user, path))
        success = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun renameDirectory() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"
        val newName = "newRoot"

        var parsedResponse = JSONObject(directorySvc.createDirectory(user, path, name))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(directorySvc.renameDirectory(user, path, name, newName))
        success = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun deleteDirectory() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        var parsedResponse = JSONObject(directorySvc.createDirectory(user, path, name))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(directorySvc.deleteDirectory(user, name))
        success = parsedResponse.optBoolean("success")
        assert(success)
    }
}