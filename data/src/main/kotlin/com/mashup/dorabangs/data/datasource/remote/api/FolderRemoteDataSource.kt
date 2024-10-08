package com.mashup.dorabangs.data.datasource.remote.api

import com.mashup.dorabangs.data.model.CreateFolderResponseModel
import com.mashup.dorabangs.data.model.FolderListResponseModel
import com.mashup.dorabangs.data.model.FolderResponseModel
import com.mashup.dorabangs.data.model.LinksFromFolderResponseModel
import com.mashup.dorabangs.domain.model.NewFolderName
import com.mashup.dorabangs.domain.model.NewFolderNameList

interface FolderRemoteDataSource {

    suspend fun getFolders(): FolderListResponseModel

    suspend fun getFolderById(folderId: String): FolderResponseModel

    suspend fun createFolder(folderList: NewFolderNameList): CreateFolderResponseModel

    suspend fun getLinksFromFolder(
        folderId: String?,
        page: Int,
        limit: Int,
        order: String,
        isRead: Boolean?,
    ): LinksFromFolderResponseModel

    suspend fun editFolderName(folderName: NewFolderName, folderId: String)

    suspend fun deleteFolder(folderId: String)
}
