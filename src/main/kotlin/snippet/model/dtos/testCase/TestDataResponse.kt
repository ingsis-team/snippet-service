package snippet.model.dtos.testCase

import snippet.model.entities.SnippetTest

class TestDataResponse(var name:String, var input:MutableList<String>, var output:MutableList<String>,var snippetId:String,var creator:String,var id:String) {

    companion object{
        fun from(snippetTest: SnippetTest): TestDataResponse {
            return TestDataResponse(
        name = snippetTest.name,
        input = snippetTest.input.toMutableList(),
        output = snippetTest.output.toMutableList(),
        snippetId = snippetTest.snippetId.toString(),
        creator = snippetTest.creator,
        id = snippetTest.id.toString()
    )
}








    }








}