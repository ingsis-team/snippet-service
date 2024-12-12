import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import snippet.controllers.TestCaseController
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.SnippetTest
import snippet.services.TestCaseService

class TestCaseControllerTests {
    private lateinit var testCaseController: TestCaseController
    private lateinit var testCaseServiceMock: TestCaseService

    @BeforeEach
    fun setUp() {
        testCaseServiceMock = mock(TestCaseService::class.java)
        testCaseController = TestCaseController(testCaseServiceMock)
    }

    @Test
    fun `createTestCase should call service and return result`() {
        val testCreateDTO =
            TestCreateDTO(
                name = "testName",
                input = mutableListOf("input1", "input2"),
                output = mutableListOf("output1", "output2"),
                id = 1L,
                envVars = "envVars",
            )
        val snippetTest =
            SnippetTest().apply {
                this.name = "testName"
                this.input = listOf("input1", "input2")
                this.output = listOf("output1", "output2")
                this.snippetId = 1L
                this.envVars = "envVars"
            }
        `when`(testCaseServiceMock.createTestCase(testCreateDTO)).thenReturn(snippetTest)

        val result = testCaseController.createTestCase(testCreateDTO)

        assertEquals(snippetTest, result)
        verify(testCaseServiceMock).createTestCase(testCreateDTO)
    }

    @Test
    fun `deleteTestCase should call service`() {
        val testCaseId = 1L

        testCaseController.deleteTestCase(testCaseId)

        verify(testCaseServiceMock).deleteTestCase(testCaseId)
    }

    @Test
    fun `getTestCases should call service and return result`() {
        val snippetId = 1L
        val testCaseReturnDtoList = listOf(TestCaseReturnDto())
        `when`(testCaseServiceMock.getTestCase(snippetId)).thenReturn(testCaseReturnDtoList)

        val result = testCaseController.getTestCases(snippetId)

        assertEquals(testCaseReturnDtoList, result)
        verify(testCaseServiceMock).getTestCase(snippetId)
    }

    @Test
    fun `runTestCase should call service and return result`() {
        val testCaseId = 1L
        val envVars = "envVars"
        val expectedResult = "result"
        `when`(testCaseServiceMock.runTestCase(testCaseId, envVars)).thenReturn(expectedResult)

        val result = testCaseController.runTestCase(testCaseId, envVars)

        assertEquals(expectedResult, result)
        verify(testCaseServiceMock).runTestCase(testCaseId, envVars)
    }
}
