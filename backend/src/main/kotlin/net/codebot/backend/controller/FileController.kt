package net.codebot.backend.controller

import net.codebot.backend.service.FileService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class FileController(val service: FileService) {
    @GetMapping("/file/content")
    fun getFile(@RequestParam path: String, @RequestParam user: String): MutableMap<String, String> {
        return service.getFile(user, path)
    }

    @PutMapping("/file/content")
    fun putFile(@RequestParam user: String, @RequestParam path: String, @RequestParam name: String, @RequestBody content: String): MutableMap<String, String> {
        return service.putFile(user, path, name, content)
    }

    @PostMapping("/file/content")
    fun postFile(@RequestParam user: String, @RequestParam path: String, @RequestBody content: String): MutableMap<String, String> {
        return service.postFile(user, path, content)
    }

    @DeleteMapping("/file/content")
    fun deleteFile(@RequestParam path: String, @RequestParam user: String): MutableMap<String, String> {
        return service.deleteFile(user, path)
    }

    @PostMapping("/file/rename")
    fun renameFile(@RequestParam path: String, @RequestParam user: String, @RequestParam oldName: String, @RequestParam newName: String): MutableMap<String, String> {
        return service.renameFile(user, path, oldName, newName)
    }
}
