package snippet.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import snippet.model.entities.User
import snippet.repositories.UserRepository

@Service
class UserService
    @Autowired
    constructor(
        private val userRepository: UserRepository,
    ) {
        fun addUser(
            nickname: String,
            id: String,
        ): User {
            val user = User()
            user.nickname = nickname
            user.id = id
            return userRepository.save(user)
        }

        fun nicknameExists(nickname: String): Boolean {
            return userRepository.existsById(nickname)
        }

        fun findIdByNickname(nickname: String): String {
            return userRepository.findIdByNickname(nickname).get()
        }
    }
