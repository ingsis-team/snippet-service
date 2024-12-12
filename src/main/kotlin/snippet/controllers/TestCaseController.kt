package snippet.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.dtos.testCase.TestDataReceive
import snippet.model.dtos.testCase.TestDataResponse
import snippet.model.entities.SnippetTest
import snippet.services.TestCaseService

@RestController
@RequestMapping("/test-case")
class TestCaseController(
    @Autowired val testCaseService: TestCaseService,
) {
    @PostMapping
    fun createTestCase(
        @RequestBody testCaseCreateDto: TestDataReceive,
        @AuthenticationPrincipal jwt: Jwt
    ): TestDataResponse {
        return testCaseService.createTestCase(testCaseCreateDto, jwt.subject)
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
    ): List<TestDataResponse> {
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
