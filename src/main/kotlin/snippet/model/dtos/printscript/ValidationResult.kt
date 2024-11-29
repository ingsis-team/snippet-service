package snippet.model.dtos.printscript

data class ValidationResult(val isValid: Boolean,
    val rule: String, val line:Int, val column:Int)
