package net.codebot.backend.controller

import net.codebot.backend.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class UserController(val service: UserService) {
    @GetMapping("/user/verify")
    fun getUser(@RequestParam pwd: String, @RequestParam name: String): MutableMap<String, Any> {
        return service.getUser(name, pwd)
    }
}