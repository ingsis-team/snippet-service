package snippet.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.SnippetTest
import snippet.repositories.TestCaseRepository

@Service
class TestCaseService
    @Autowired
    constructor(
        private val testCaseRepository: TestCaseRepository,
        private val printscriptService: PrintscriptService,
        private val assetService: AssetService,
    ) {
        fun createTestCase(testCaseCreateDto: TestCreateDTO): SnippetTest {
            val testCase = SnippetTest.from(testCaseCreateDto)
            this.testCaseRepository.save(testCase)
            return testCase
        }

        fun deleteTestCase(testId: Long) {
            this.testCaseRepository.deleteById(testId)
        }

        fun getTestCase(snippetId: Long): List<TestCaseReturnDto> {
            var testCasedtos: MutableList<SnippetTest> = this.testCaseRepository.findBySnippetId(snippetId)
            return testCasedtos.map { TestCaseReturnDto.from(it) }
        }

        fun runTestCase(
            testCaseId: Long,
            envVars: String,
        ): String {
            println("1$testCaseId")
            val testCase: SnippetTest = this.testCaseRepository.findById(testCaseId).get()
            println("2,$testCase")
            val snippetId: Long = testCase.snippetId
            println("3,$snippetId")
            val snippetContent: String = assetService.getSnippet(snippetId.toString())
            print(snippetContent)
            print(testCase.input)
            print(testCase.output)
            return printscriptService.runTest(snippetContent, testCase.input.toString(), testCase.output, envVars)
        }
    }
