package snippet.services

import org.intellij.lang.annotations.Language
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import snippet.exceptions.InvalidSnippetException
import snippet.exceptions.PermissionDeniedException
import snippet.model.dtos.permission.ResourcePermissionCreateDTO
import snippet.model.dtos.permission.UserResourcePermission
import snippet.model.dtos.printscript.FormatFileDto
import snippet.model.dtos.printscript.PrintscriptDataDTO
import snippet.model.dtos.printscript.Rule
import snippet.model.dtos.snippet.GetSnippetDto
import snippet.model.dtos.snippet.SnippetCreateDto
import snippet.model.dtos.snippet.UpdateSnippetDto
import snippet.model.entities.Snippet
import snippet.repositories.SnippetRepository
import java.util.*

@Service
class SnippetService
@Autowired
constructor(
    val snippetRepository: SnippetRepository,
    val assetService: AssetService,
    val permissionService: PermissionService,
    val printscriptService: PrintscriptService
){

    fun createSnippet(snippetDto: SnippetCreateDto, correlationId: String, authorId: String): Snippet{
        try{
//        validateSnippet(snippetDto.content)
        val snippet = Snippet.from(snippetDto,authorId)
        val savedSnippet = this.snippetRepository.save(snippet)
        createResourcePermissions(snippetDto, savedSnippet, correlationId, authorId)
        saveSnippetOnAssetService(savedSnippet.id.toString(), snippetDto.content, correlationId)
        return savedSnippet}
        catch(e: InvalidSnippetException){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        }
    }


    fun createResourcePermissions(snippetDto: SnippetCreateDto,savedSnippet: Snippet, correlationId: String,authorId: String){
        val permissions = listOf("READ","WRITE")
        val dto = ResourcePermissionCreateDTO(authorId, savedSnippet.id.toString(),permissions)
        permissionService.createResourcePermission(dto, correlationId)

    }


    fun saveSnippetOnAssetService(id:String, content: String, correlationId: String){
        println("saving on asset service..")
        assetService.saveSnippet(id,content,correlationId)
        println("asset saved!")
    }

    private fun validateSnippet(content:String){
        val validationResult = printscriptService.validate(content)
        if(!validationResult.isValid){
            throw InvalidSnippetException(validationResult.rule,validationResult.line,validationResult.column)
        }

    }









   fun getSnippets(
        userId: String,
        page: Int,
        size: Int,
    ): Page<GetSnippetDto> {
        val resources = permissionService.getAlluserResources(userId)
        val context = snippetRepository.findAllById(resources.map { it.resourceId.toLong() })
        val snippets =
            context.map {
                val content = assetService.getSnippet(it.id.toString())
                GetSnippetDto.from(it, content)
            }
        return toPageable(snippets, page, size)
    }

    private fun toPageable(
        snippets: List<GetSnippetDto>,
        page: Int,
        size: Int,
    ): Page<GetSnippetDto> {
        val total = snippets.size
        val start = (page * size).coerceAtMost(total)
        val end = (start + size).coerceAtMost(total)
        val subList = snippets.subList(start, end)
        return PageImpl(subList, PageRequest.of(page, size), total.toLong())
    }

    fun getSnippetById(
        userId: String,
        snippetId: Long,
    ): GetSnippetDto {
        val context = snippetRepository.findById(snippetId).orElse(null) ?: throw ChangeSetPersister.NotFoundException()
        val permission = permissionService.getAlluserResources(userId).filter { it.resourceId == snippetId.toString() }
        if (permission.isEmpty()) throw PermissionDeniedException("User cannot acces this snippet")
        val content = assetService.getSnippet(snippetId.toString())
        return GetSnippetDto.from(context, content)
    }


    fun getAllSnippetsByUser(userId: String): List<GetSnippetDto> {
        val resources = permissionService.getAlluserResources(userId)
        val context = snippetRepository.findAllById(resources.map { it.resourceId.toLong() })
        val snippets =
            context.map {
                val content = assetService.getSnippet(it.id.toString())
                GetSnippetDto.from(it, content)
            }
        return snippets
    }


     fun updateSnippet(
         userId: String,
         updateSnippetDto: UpdateSnippetDto,
         correlationId: String,
    ): GetSnippetDto {
        val snippet = checkSnippetExists(updateSnippetDto.id.toLong())
        checkUserCanModify(userId, updateSnippetDto.id)
        assetService.deleteSnippet(updateSnippetDto.id)
        saveSnippetOnAssetService(updateSnippetDto.id, updateSnippetDto.content, correlationId)
        return GetSnippetDto.from(snippet, updateSnippetDto.content)
    }

    fun deleteSnippet(
        userId: String,
        snippetId: Long,
    ) {
        println("id: $snippetId")
        permissionService.deleteResourcePermissions(userId, snippetId.toString())
        println("deleted from resources")
        assetService.deleteSnippet(snippetId.toString())
        println("deleted from asset service")
        val snippet = snippetRepository.findById(snippetId).orElse(null)
        if (snippet == null) {
            println("snippet not found")
            throw ChangeSetPersister.NotFoundException()
        }
        snippetRepository.delete(snippet)
    }

     fun getSnippet(id: String): String = assetService.getSnippet(id)

        fun shareSnippet(
            authorId: String,
            friendId: String,
            snippetId: Long,
        ): UserResourcePermission = permissionService.shareResource(authorId, snippetId.toString(), friendId)

   fun getUsers(
        pageNumber: Int,
        pageSize: Int,
    ): Page<String> {
        val snippets = permissionService.getUsers()
        val total = snippets.size
        val start = (pageNumber * pageSize).coerceAtMost(total)
        val end = (start + pageSize).coerceAtMost(total)
        val subList = snippets.subList(start, end)
        return PageImpl(subList, PageRequest.of(pageNumber, pageSize), total.toLong())
    }

     fun updateFormattedLintedSnippet(
        snippetId: Long,
        content: String,
        correlationId: String,
    ) {
        assetService.saveSnippet(snippetId.toString(), content, correlationId)
    }


    private fun checkUserCanModify(
        userId: String,
        snippetId: String,
    ) {
        val permissions = permissionService.userCanWrite(userId, snippetId)
        if (!permissions.permissions.contains("WRITE")) {
            throw PermissionDeniedException("User does not have write permission")
        }
    }

    private fun checkSnippetExists(id: Long): Snippet = snippetRepository.findById(id).orElseThrow { throw ChangeSetPersister.NotFoundException() }


    fun formatSnippet(userId:String,snippetId:String,language: String,correlationId: UUID):String{
        val permissions = permissionService.userCanWrite(userId, snippetId)
        if(!permissions.permissions.contains("WRITE")) throw PermissionDeniedException("User cannot format this snippet")
        val content = assetService.getSnippet(snippetId)
        val data = FormatFileDto(correlationId,snippetId,language,"1.1",content,userId)
        val response= printscriptService.formatSnippet(data)
        return response.snippet


    }

    fun getFormatRules(
        userId: String,
        correlationId: UUID,
    ): List<Rule> {
        return printscriptService.getFormatRules(userId, correlationId)
    }

     fun getLintRules(
        userId: String,
        correlationId: UUID,
    ): List<Rule> {
        return printscriptService.getLintRules(userId, correlationId)
    }

     fun changeFormatRules(
        userId: String,
        rules: List<Rule>,
        correlationId: UUID,
    ) {
        val snippets =
            getAllSnippetsByUser(
                userId,
            ).map {
                PrintscriptDataDTO(correlationId, it.id, it.language, "1.1", it.content)
            }
        rules.map {
            println("rules: ${it.name} ${it.value} ${it.id} ${it.name}")
        }
        snippets.map {
            println(it.input)
        }
        printscriptService.changeFormatRules(userId, rules, snippets, correlationId)
    }

     fun changeLintRules(
        userId: String,
        rules: List<Rule>,
        correlationId: UUID,
    ) {
        val snippets =
            getAllSnippetsByUser(
                userId,
            ).map {
                PrintscriptDataDTO(correlationId, it.id, it.language, "1.1", it.content)
            }
        printscriptService.changeFormatRules(userId, rules, snippets, correlationId)
    }












}

