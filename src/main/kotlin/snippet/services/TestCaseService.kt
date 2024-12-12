package snippet.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.dtos.testCase.TestDataReceive
import snippet.model.dtos.testCase.TestDataResponse
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
        private val logger = LoggerFactory.getLogger(TestCaseService::class.java)



        fun createTestCase(testCaseCreateDto: TestDataReceive, userId:String): TestDataResponse {
            val testCase = SnippetTest.from(testCaseCreateDto, userId)
            this.testCaseRepository.save(testCase)
            val testResponse = TestDataResponse.from(testCase)


            return testResponse
        }

        fun deleteTestCase(testId: Long) {
            this.testCaseRepository.deleteById(testId)
        }

        fun getTestCase(snippetId: Long): List<TestDataResponse> {
            var testCasedtos: MutableList<SnippetTest> = this.testCaseRepository.findBySnippetId(snippetId)
            return testCasedtos.map { TestDataResponse.from(it) }
        }

        fun runTestCase(
            testCaseId: Long,
            envVars: String,
        ): String {
            logger.info("1$testCaseId")
            val testCase: SnippetTest = this.testCaseRepository.findById(testCaseId).get()
            logger.info("2,$testCase")
            val snippetId: Long = testCase.snippetId
            logger.info("3,$snippetId")
            val snippetContent: String = assetService.getSnippet(snippetId.toString())
            logger.info(snippetContent)
            logger.info("TestCase input: ${testCase.input}")
            logger.info("TestCas output: ${testCase.output}")
            return printscriptService.runTest(snippetContent, testCase.input.toString(), testCase.output, envVars)
        }
    }
