package net.codebot.backend

import net.codebot.backend.service.DatabaseService
import net.codebot.backend.service.DirectoryService
import net.codebot.backend.service.FileService
import net.codebot.backend.service.UserService
import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.round


class FileTests: Tests() {
    val fileSvc = FileService()
    @Test
    fun createFile() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        val parsedResponse = JSONObject(fileSvc.putFile(user, path, name, "Test Content"))
        val success: Boolean = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun getFile() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        var parsedResponse = JSONObject(fileSvc.putFile(user, path, name, "Test Content"))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(fileSvc.getFile(user, name))
        success = parsedResponse.optBoolean("success")
        val content = parsedResponse.optString("body")
        assert(success)
        assert(content == "Test Content")
    }

    @Test
    fun renameFile() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"
        val newName = "newRoot"

        var parsedResponse = JSONObject(fileSvc.putFile(user, path, name, "Test Content"))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(fileSvc.renameFile(user, path, name, newName))
        success = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun deleteFile() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        var parsedResponse = JSONObject(fileSvc.putFile(user, path, name, "Test Content"))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(fileSvc.deleteFile(user, name))
        success = parsedResponse.optBoolean("success")
        assert(success)
    }

    @Test
    fun updateFile() {
        val user: String = UUID.randomUUID().toString()
        val path = ""
        val name = "root"

        var parsedResponse = JSONObject(fileSvc.putFile(user, path, name, "Test Content"))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(fileSvc.postFile(user, name, "Test Content New"))
        success = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(fileSvc.getFile(user, name))
        success = parsedResponse.optBoolean("success")
        val content = parsedResponse.optString("body")
        assert(success)
        assert(content == "Test Content New")
    }
}