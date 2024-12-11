package snippet.model.dtos.printscript

data class Rule(
    val id: String,
    val name: String,
    val isActive: Boolean,
    val value: Any?,
)
