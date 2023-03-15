package net.codebot.backend.controller

import net.codebot.backend.service.DirectoryService
import net.codebot.backend.service.FileContentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class DirectoryController(val service: DirectoryService) {
    @GetMapping("/file/content")
    fun getFiles(@RequestParam path: String, @RequestParam user: String): MutableMap<String, Any?> {
        return service.getDirectory(user, path)
    }

    @PutMapping("/file/content")
    fun putFile(@RequestParam user: String, @RequestParam path: String, @RequestParam name: String): MutableMap<String, String> {
        return service.createDirectory(user, path, name)
    }

    @PostMapping("/file/content")
    fun postFile(@RequestParam user: String, @RequestParam path: String, @RequestParam oldName: String, @RequestParam newName: String): MutableMap<String, String> {
        return service.renameDirectory(user, path, oldName, newName)
    }

    @DeleteMapping("/file/content")
    fun deleteFile(@RequestParam path: String, @RequestParam user: String): MutableMap<String, String> {
        return service.deleteDirectory(user, path)
    }
}
