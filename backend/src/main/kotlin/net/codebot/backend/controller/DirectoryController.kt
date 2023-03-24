package net.codebot.backend.controller

import net.codebot.backend.service.DirectoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class DirectoryController(val service: DirectoryService) {
    @GetMapping("/directory/content")
    fun getDirectory(@RequestParam path: String, @RequestParam user: String): MutableMap<String, Any?> {
        return service.getDirectory(user, path)
    }

    @PutMapping("/directory/content")
    fun putDirectory(@RequestParam user: String, @RequestParam path: String, @RequestParam name: String): MutableMap<String, String> {
        return service.createDirectory(user, path, name)
    }

    @PostMapping("/directory/content")
    fun postDirectory(@RequestParam user: String, @RequestParam path: String, @RequestParam oldName: String, @RequestParam newName: String): MutableMap<String, String> {
        return service.renameDirectory(user, path, oldName, newName)
    }

    @DeleteMapping("/directory/content")
    fun deleteDirectory(@RequestParam path: String, @RequestParam user: String): MutableMap<String, String> {
        return service.deleteDirectory(user, path)
    }
}
