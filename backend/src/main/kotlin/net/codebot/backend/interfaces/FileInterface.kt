//package net.codebot.backend.interfaces
//
//import jakarta.persistence.Table
//import net.codebot.backend.dto.FileDTO
//import org.springframework.data.jpa.repository.Query
//import org.springframework.data.repository.CrudRepository
//import org.springframework.data.annotation.Id
//
//interface FileInterface: CrudRepository<FileDTO, String> {
//    @Query("select * from FILES")
//    fun findFiles(): List<FileDTO>
//}
//
//@Table(name="FILES")
//data class Table(@Id val id: String?, val name: String, val path: String)
