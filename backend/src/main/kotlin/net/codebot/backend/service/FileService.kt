package net.codebot.backend.service

import net.codebot.backend.dto.FileDTO
import org.springframework.stereotype.Service

@Service
class FileService {
    fun getFiles(): List<FileDTO>? {
        println("Getting Files")
        return null
    }

    fun postFiles(file: FileDTO): FileDTO {
        return file
    }
}