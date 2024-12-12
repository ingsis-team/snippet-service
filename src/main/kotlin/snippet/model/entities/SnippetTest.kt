package snippet.model.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import org.jetbrains.annotations.NotNull
import snippet.model.dtos.testCase.TestDataReceive

@Entity
class SnippetTest {
    @Id()
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    @Column
    var snippetId: Long = 0

    @Column
    var name: String = ""

    @ElementCollection
    @Column
    var input: List<String> = mutableListOf()

    @ElementCollection
    @CollectionTable(name = "test_case_outputs", joinColumns = [JoinColumn(name = "test_case_id")])
    @Column(name = "output")
    var output: List<String> = mutableListOf()

    @Column
    var creator: String=""


    companion object {
        fun from(testCaseDto: TestDataReceive, userId:String): SnippetTest {
            val testCase = SnippetTest()
            testCase.input = testCaseDto.input
            testCase.output = testCaseDto.output
            testCase.name = testCaseDto.name
            testCase.snippetId = testCaseDto.snippetId.toLong()
            testCase.creator = userId
            return testCase
        }
    }
}
