package snippet.model.dtos.permission

class ShareResource(
    val selfId: String,
    val otherId: String,
    val resourceId: String,
    val permissions: List<Permissons> = listOf(Permissons.READ, Permissons.WRITE),
)