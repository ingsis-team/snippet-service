package snippet.model.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Snippet(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: String? = null,
    var name: String,
    var description: String,
    var language: String,
    var version: String,
    var content: String,
)