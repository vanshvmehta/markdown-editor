package net.codebot.backend

import net.codebot.backend.service.DatabaseService
import net.codebot.backend.service.UserService
import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.util.*


class UserTests: Tests() {
    private val baseURL = "$hostURL/user/verify"
    val userSvc = UserService(DatabaseService())
    @Test
    fun testNewUser() {
        val username: String = UUID.randomUUID().toString()
        val pwd: String = UUID.randomUUID().toString()

        val parsedResponse = JSONObject(userSvc.getUser(username, pwd))
        val success: Boolean = parsedResponse.optBoolean("success")
        println("New User Tested")
        assert(success)
    }

    @Test
    fun testIncorrectPwd() {
        val username: String = UUID.randomUUID().toString()
        val pwd: String = UUID.randomUUID().toString()
        val incorrectPwd: String = UUID.randomUUID().toString()

        var parsedResponse = JSONObject(userSvc.getUser(username, pwd))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(userSvc.getUser(username, incorrectPwd))
        success = parsedResponse.optBoolean("success")
        println("Incorrect User Tested")
        assert(!success)
    }

    @Test
    fun testCorrectPwd() {
        val username: String = UUID.randomUUID().toString()
        val pwd: String = UUID.randomUUID().toString()

        var parsedResponse = JSONObject(userSvc.getUser(username, pwd))
        var success: Boolean = parsedResponse.optBoolean("success")
        assert(success)

        parsedResponse = JSONObject(userSvc.getUser(username, pwd))
        success = parsedResponse.optBoolean("success")
        println("Correct User Tested")
        assert(success)
    }
}