package com.mashup.dorabangs.data.network.service

import com.mashup.dorabangs.data.model.CreateFolderRequestModel
import com.mashup.dorabangs.data.model.EditFolderNameRequestModel
import com.mashup.dorabangs.data.model.FolderListResponseModel
import com.mashup.dorabangs.data.model.FolderResponseModel
import com.mashup.dorabangs.data.model.LinksFromFolderResponseModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FolderService {

    @GET("folders")
    suspend fun getFolders(): FolderListResponseModel

    @GET("folders/{id}")
    suspend fun getFolderById(@Path("id") folderId: String): FolderResponseModel

    @POST("folders")
    suspend fun createFolder(
        @Body createFolderRequest: CreateFolderRequestModel,
    )

    @PATCH("folders/{folderId}")
    suspend fun editFolderName(
        @Path("folderId") folderId: String,
        @Body editFolderNameRequest: EditFolderNameRequestModel,
    )

    @GET("folders/{folderId}/posts")
    suspend fun getLinkFolderList(
        @Path("folderId") folderId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10,
        @Query("order") order: String,
        @Query("unread") unread: Boolean,
    ): LinksFromFolderResponseModel

    @DELETE("folders/{folderId}")
    suspend fun deleteFolder(
        @Path("folderId") folderId: String,
    )
}
