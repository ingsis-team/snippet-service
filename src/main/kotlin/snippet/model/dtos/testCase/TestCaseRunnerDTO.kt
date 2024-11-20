package snippet.model.dtos.testCase

import snippet.model.entities.SnippetTest

class TestCaseReturnDto {
    var id: String = ""
    var name: String = ""
    var input: List<String> = mutableListOf()
    var output: List<String> = mutableListOf()
    var envVars: String = ""

    companion object {
        fun from(testCase: SnippetTest): TestCaseReturnDto {
            val testCase1 = TestCaseReturnDto()
            testCase1.id = testCase.id.toString()
            testCase1.input = testCase.input
            testCase1.output = testCase.output
            testCase1.name = testCase.name
            testCase1.envVars = testCase.envVars
            return testCase1
        }
    }
}