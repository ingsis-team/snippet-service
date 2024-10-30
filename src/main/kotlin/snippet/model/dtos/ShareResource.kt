package snippet.model.dtos

data class ShareResource(
    val selfId: String,
    val otherId: String,
    val resourceId: String,
    val permissions: List<String>
)