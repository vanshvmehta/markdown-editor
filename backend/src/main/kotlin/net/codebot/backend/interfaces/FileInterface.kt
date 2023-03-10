//package net.codebot.backend.interfaces
//
//import jakarta.persistence.Table
//import net.codebot.backend.dto.FileDTO
//import org.springframework.data.jpa.repository.Query
//import org.springframework.data.repository.CrudRepository
//import org.springframework.data.annotation.Id
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//
//interface FileInterface: CrudRepository<String, String> {
//    @Query("select * from FILES")
//    fun findFiles(): String
//}
//
//@Table(name="FILES")
//data class Table(@Id val id: String?, val name: String, val path: String)
