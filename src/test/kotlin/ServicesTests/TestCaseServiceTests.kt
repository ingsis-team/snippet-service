

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import snippet.model.dtos.testCase.TestCaseReturnDto
import snippet.model.dtos.testCase.TestCreateDTO
import snippet.model.entities.SnippetTest
import snippet.repositories.TestCaseRepository
import snippet.services.AssetService
import snippet.services.PrintscriptService
import snippet.services.TestCaseService
import java.util.Optional

class TestCaseServiceTests {
    private lateinit var testCaseService: TestCaseService
    private lateinit var testCaseRepositoryMock: TestCaseRepository
    private lateinit var printscriptServiceMock: PrintscriptService
    private lateinit var assetServiceMock: AssetService

    @BeforeEach
    fun setUp() {
        testCaseRepositoryMock = mock(TestCaseRepository::class.java)
        printscriptServiceMock = mock(PrintscriptService::class.java)
        assetServiceMock = mock(AssetService::class.java)

        testCaseService = TestCaseService(testCaseRepositoryMock, printscriptServiceMock, assetServiceMock)
    }

    @Test
    fun `deleteTestCase should not throw exception`() {
        assertDoesNotThrow {
            testCaseService.deleteTestCase(1L)
        }
    }

    @Test
    fun `runTestCase should return test result`() {
        val snippetTest = SnippetTest()
        val snippetContent = "snippet content"
        val testResult = "test result"

        `when`(testCaseRepositoryMock.findById(anyLong())).thenReturn(Optional.of(snippetTest))
        `when`(assetServiceMock.getSnippet(anyString())).thenReturn(snippetContent)
        `when`(printscriptServiceMock.runTest(anyString(), anyString(), anyList(), anyString())).thenReturn(testResult)

        val result = testCaseService.runTestCase(1L, "envVars")

        assertEquals(testResult, result)
    }

    @Test
    fun `createTestCase should return saved test case`() {
        val testCreateDTO =
            TestCreateDTO(
                name = "Test Case 1",
                input = mutableListOf("input1", "input2"),
                output = mutableListOf("output1", "output2"),
                id = 1L,
                envVars = "envVar1",
            )
        val snippetTest = SnippetTest.from(testCreateDTO)

        `when`(testCaseRepositoryMock.save(any(SnippetTest::class.java))).thenReturn(snippetTest)

        val result = testCaseService.createTestCase(testCreateDTO)

        assertEquals(snippetTest.id, result.id)
        assertEquals(snippetTest.name, result.name)
        assertEquals(snippetTest.input, result.input)
        assertEquals(snippetTest.output, result.output)
        assertEquals(snippetTest.snippetId, result.snippetId)
        assertEquals(snippetTest.envVars, result.envVars)
    }

    @Test
    fun `getTestCase should return list of TestCaseReturnDto`() {
        val snippetTests = mutableListOf(SnippetTest())
        val testCaseReturnDtos = snippetTests.map { TestCaseReturnDto.from(it) }

        `when`(testCaseRepositoryMock.findBySnippetId(anyLong())).thenReturn(snippetTests)

        val result = testCaseService.getTestCase(1L)

        assertEquals(testCaseReturnDtos.size, result.size)
        assertEquals(testCaseReturnDtos[0].id, result[0].id)
        assertEquals(testCaseReturnDtos[0].name, result[0].name)
        assertEquals(testCaseReturnDtos[0].input, result[0].input)
        assertEquals(testCaseReturnDtos[0].output, result[0].output)
        assertEquals(testCaseReturnDtos[0].envVars, result[0].envVars)
    }
}
