//package net.codebot.backend.controller
//
//import net.codebot.backend.dto.ContentDTO
//import net.codebot.backend.service.FileService
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/file")
//class FileController(val service: FileService) {
//    @GetMapping("/file/{user}")
//    fun getFiles(@PathVariable user: String): List<ContentDTO>? {
//        return service.getFiles(user)
//    }
//
//    @PutMapping("/file/{user}")
//    fun putFile(@RequestBody request: ContentDTO, @PathVariable user: String): MutableMap<String, String> {
//        return service.createFile(request, user)
//    }
//
//    @PostMapping("/file/{user}")
//    fun postFile(@RequestBody request: ContentDTO, @PathVariable user: String): MutableMap<String, String> {
//        return service.postFile(request, user)
//    }
//
//    @DeleteMapping("/file/{user}")
//    fun deleteFile(@RequestBody id: String, @PathVariable user: String): MutableMap<String, String> {
//        return service.deleteFile(id, user)
//    }
//}
