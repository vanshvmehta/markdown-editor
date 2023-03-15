package net.codebot.backend.controller

import net.codebot.backend.service.FileContentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class FileContentController(val service: FileContentService) {
    @GetMapping("/file/content/{user}/{id}")
    fun getFiles(@PathVariable id: String, @PathVariable user: String): MutableMap<String, String> {
        return service.getFileContent(user, id)
    }

    @PutMapping("/file/content/{user}/{id}")
    fun putFile(@PathVariable user: String, @PathVariable id: String, @RequestBody content: String): MutableMap<String, String> {
        return service.putFileContent(user, id, content)
    }

    @PostMapping("/file/content/{user}/{id}")
    fun postFile(@PathVariable user: String, @PathVariable id: String, @RequestBody content: String): MutableMap<String, String> {
        return service.postFileContent(user, id, content)
    }

    @DeleteMapping("/file/content/{user}/{id}")
    fun deleteFile(@PathVariable id: String, @PathVariable user: String): MutableMap<String, String> {
        return service.deleteFileContent(user, id)
    }
}
