package snippet.model.dtos

data class ResourceUserPermission(
    val resourceId: String,
    val permissions: Set<String>,
)
