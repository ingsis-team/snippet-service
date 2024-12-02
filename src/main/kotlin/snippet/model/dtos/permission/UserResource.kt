package snippet.model.dtos.permission

class UserResource(
    val userId: String,
    val resourceId: List<Permission>
)