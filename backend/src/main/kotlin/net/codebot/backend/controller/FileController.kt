package net.codebot.backend.controller

import net.codebot.backend.dto.FileDTO
import net.codebot.backend.service.FileService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/file")
class FileController(val service: FileService) {
    @GetMapping
    fun getFiles(): String {
        service.getFiles()
        return "Hello"
    }

    @PostMapping
    fun postFile(@RequestBody request: FileDTO): FileDTO {
        return service.postFiles(request)
    }
}
