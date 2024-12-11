package snippet.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import snippet.model.entities.User
import java.util.Optional

interface UserRepository : JpaRepository<User, String> {
    @Query("SELECT u.id FROM User u WHERE u.nickname = :nickname")
    fun findIdByNickname(
        @Param("nickname") nickname: String,
    ): Optional<String>
}
