package snippet.model.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.jetbrains.annotations.NotNull

@Entity
@Table(name="users")
class User {
    @Id
    @NotNull
    @Column(unique = true, nullable = false)
    var nickname: String = ""

    @NotNull
    @Column(unique = true, nullable = false)
    var id: String = ""
}