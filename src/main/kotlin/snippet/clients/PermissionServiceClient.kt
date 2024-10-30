package snippet.clients

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import snippet.model.dtos.AddResource
import snippet.model.dtos.ResourceUserPermission
import snippet.model.dtos.ShareResource

// Cliente HTTP que maneja conexión con servicio de permisos
@Service
class PermissionServiceClient(private val restTemplate: RestTemplate) {

    private val baseUrl = "http://localhost:8081/resource"

    // Registrar un nuevo recurso (ownerId asociado con snippetId)
    fun addResource(resource: AddResource): ResourceUserPermission {
        val url = "$baseUrl/create-resource"
        val response: ResponseEntity<ResourceUserPermission> =
            restTemplate.postForEntity(url, resource, ResourceUserPermission::class.java)
        if (response.statusCode == HttpStatus.CREATED) {
            return response.body!!
        } else {
            throw Exception("Failed to add resource")
        }
    }

    // Compartir un recurso existente con otro usuario (dándo permisos específicos de lectura o escritura)
    fun shareResource(shareRequest: ShareResource): AddResource {
        val url = "$baseUrl/share-resource"
        val response: ResponseEntity<AddResource> =
            restTemplate.postForEntity(url, shareRequest, AddResource::class.java)
        if (response.statusCode == HttpStatus.CREATED) {
            return response.body!!
        } else {
            throw Exception("Failed to share resource")
        }
    }

    // Obtener todos los recursos a los que el usuario tiene acceso y sus niveles de permiso para cada uno
    fun getPermissionsForUser(userId: String): List<ResourceUserPermission> {
        val uri = UriComponentsBuilder.fromHttpUrl("$baseUrl/all-by-userId")
            .queryParam("id", userId)
            .toUriString()
        val response: ResponseEntity<Array<ResourceUserPermission>> =
            restTemplate.getForEntity(uri, Array<ResourceUserPermission>::class.java)
        return response.body?.toList() ?: emptyList()
    }

    fun deleteResource(resourceId: String, userId: String): String{
        // construir url de solicitud incluyendo id de recurso en path
        val uri = UriComponentsBuilder.fromHttpUrl("$baseUrl/$resourceId")
            .queryParam("userId", userId)
            .toUriString()

        // realizar solicitud delete
        val response: ResponseEntity<String> = restTemplate.exchange(uri, HttpMethod.DELETE, null, String::class.java)

        // verificar si la eliminación fue exitosa y retornar mensaje de respuesta
        return response.body ?: throw Exception("Failed to delete resource")
    }

    fun getSpecificPermission(resourceId: String, userId: String): ResourceUserPermission{
        // construir url con ambos parámetros
        val uri = UriComponentsBuilder.fromHttpUrl("$baseUrl/user-resource")
            .queryParam("userId", userId)
            .queryParam("resourceId", resourceId)
            .toUriString()

        // realizar solicitud GET y obtener respuesta
        val response: ResponseEntity<ResourceUserPermission> =
            restTemplate.getForEntity(uri, ResourceUserPermission::class.java)

        // verificar que la respuesta sea exitosa y devolver el cuerpo
        return response.body ?: throw Exception("No permission found for specified user and resource")
    }
}