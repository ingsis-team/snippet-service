package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.SnippetTest
import snippet.services.TestCaseService

@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/test-case")
class TestCaseController(
    @Autowired val testCaseService: TestCaseService,
) {
    @PostMapping
    fun createTestCase(
        @RequestBody testCaseCreateDto: TestCreateDTO,
    ): SnippetTest {
        return testCaseService.createTestCase(testCaseCreateDto)
    }

    @DeleteMapping
    fun deleteTestCase(
        @RequestParam testCaseId: Long,
    ) {
        return testCaseService.deleteTestCase(testCaseId)
    }

    @GetMapping
    fun getTestCases(
        @RequestParam snippetId: Long,
    ): List<TestCaseReturnDto> {
        return testCaseService.getTestCase(snippetId)
    }

    @PostMapping("/run")
    fun runTestCase(
        @RequestParam testCaseId: Long,
        @RequestParam envVars: String,
    ): String {
        println("1$testCaseId")
        return testCaseService.runTestCase(testCaseId, envVars)
    }
}