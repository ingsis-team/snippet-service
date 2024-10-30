package snippet.model.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Snippet (
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: String,
    val name: String,
    val description: String,
    val language: String,
    val version: String,
    val content: String,
)