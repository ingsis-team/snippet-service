package snippet.exceptions



class InvalidSnippetException(
    val rule: String,
    val line: Int,
    val column: Int
) : RuntimeException("Invalid snippet: $rule at line $line, column $column")