package snippet.services

import org.springframework.stereotype.Service
import snippet.clients.PermissionServiceClient
import snippet.exceptions.PermissionDeniedException
import snippet.exceptions.SnippetNotFoundException
import snippet.model.dtos.AddResource
import snippet.model.dtos.ShareResource
import snippet.model.entities.Snippet
import snippet.repositories.SnippetRepository

@Service
class SnippetService(
    private val snippetRepository: SnippetRepository,
    private val permissionServiceClient: PermissionServiceClient
){

    fun createSnippet(snippet: Snippet, userId: String): Unit{
        val savedSnippet = snippetRepository.save(snippet)
        val addResource = AddResource(resourceId = savedSnippet.id, ownerId = userId)
        permissionServiceClient.addResource(addResource)
    }

    fun deleteSnippetById(snippetId: String, userId: String): String{
        val permissions = permissionServiceClient.getSpecificPermission(snippetId, userId).permissions
        val isOwner = permissions.any { it == "OWNER" }
        if (!isOwner) throw PermissionDeniedException("El usuario $userId no tiene permiso para eliminar el snippet $snippetId.")

        // eliminar el recurso en el sistema de permisos y el snippet de la base de datos
        permissionServiceClient.deleteResource(snippetId, userId)
        snippetRepository.deleteById(snippetId)

        return "Snippet eliminado correctamente."
    }

    fun updateSnippet(snippetId: String, snippetData: Snippet, userId: String){
        // chequea si existe snippet
        val snippet = snippetRepository.findById(snippetId)
            .orElseThrow{SnippetNotFoundException("Snippet con id $snippetId no encontrado.")}

        // chequea si tiene permisos de escritura en ese snippet
        val permissions = permissionServiceClient.getPermissionsForUser(userId)
        val hasWritePermission = permissions.any{it.resourceId==snippetId && it.permissions.any { it == "WRITE" }}
        if (!hasWritePermission) throw PermissionDeniedException("Usuario $userId no tiene permisos de escritura en snippet $snippetId.")

        // actualiza snippet
        snippetRepository.save(snippetData)
    }

    fun getSnippetsByUser(userId: String): Iterable<Snippet>{
        val permission = permissionServiceClient.getPermissionsForUser(userId)
        val snippetIds = permission.map { it.resourceId }
        return snippetRepository.findAllById(snippetIds)
    }

    fun getSnippetById(snippetId: String, userId: String): Snippet{
        // chequea si usuario tiene acceso a snippet
        val snippets = getSnippetsByUser(userId)
        val snippet = snippets.firstOrNull { it -> it.id == snippetId }
            ?: throw SnippetNotFoundException("Usuario $userId sin acceso a snippet $snippetId.")
        return snippet
    }

    fun shareSnippet(snippetId: String, selfId:String, otherId: String){
        val shareResource = ShareResource(selfId = selfId, otherId = otherId, resourceId = snippetId, permissions = listOf("READ", "WRITE"))
        permissionServiceClient.shareResource(shareResource)
    }
}