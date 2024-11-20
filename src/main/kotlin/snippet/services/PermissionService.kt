package snippet.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.reactive.function.client.WebClient
import snippet.exceptions.PermissionDeniedException
import snippet.model.dtos.permission.PermissionResponse
import snippet.model.dtos.permission.ResourcePermissionCreateDTO
import snippet.model.dtos.permission.UserResourcePermission


@Service
class PermissionService(@Value("\${permission.url}") permissionUrl :String) {

    private val permissionApi = WebClient.builder().baseUrl("http://$permissionUrl").build()

    fun createResourcePermission(resourceData: ResourcePermissionCreateDTO, correlationId:String):Boolean{
        return try{
            permissionApi
                .post()
                .uri("/resource/create-resource")
                .bodyValue(resourceData)
                .retrieve()
                .bodyToMono(PermissionResponse::class.java)
                .block()
        true
        }catch (e: Exception){
            println(e.message)
            false
        }
    }
//Tiene como proposito obtener todos los recursos asociados a un usuario en específico, segun su userId
    fun getAlluserResources(userId:String):List<PermissionResponse>{
        val response =
            permissionApi
                .get()
                .uri("/resource/all-by-userId?id=$userId")
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<List<PermissionResponse>>() {})
                .block()?:throw RuntimeException("Unable to fetch the resources")

        return response

    }
//Recibe la respuesta del servidor, que indica si el usuario tiene permiso para escribir en el recurso
    fun userCanWrite(userId:String,resourceId:String):PermissionResponse{
        return permissionApi
            .get()
            .uri("/resource/can-write")
            .cookie("userId",userId)
            .cookie("resourceId",resourceId)
            .retrieve()
            .bodyToMono(PermissionResponse::class.java)
            .block()?: throw RuntimeException("Unable to fetch permissions")
    }

    fun deleteResourcePermissions(userId: String, resourceId: String) {
        try {
            permissionApi
                .delete()
                .uri("/resource/{resourceId}", resourceId)
                .cookie("userId", userId)
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: throw AuthenticationCredentialsNotFoundException("Failed to delete resource permissions")
        } catch (e: Unauthorized) {
            throw PermissionDeniedException("User is not authorized to delete resource permissions")
        }
    }


    fun shareResource(userId:String, resourceId:String,otherId:String):UserResourcePermission{
        val shareDto = shareResource(userId,otherId,resourceId)
        return permissionApi
            .post()
            .uri("/resource/share-resource")
            .bodyValue(shareDto)
            .retrieve()
            .bodyToMono(UserResourcePermission::class.java)
            .block()?: throw AuthenticationCredentialsNotFoundException("User cannot share this resource as he is not the owner")

    }



    fun getUsers():List<String> =
        permissionApi
            .get()
            .uri("/user")
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<List<String>>(){})
            .block()?: throw NotFoundException()











}