package com.mashup.dorabangs.data.datasource.remote.impl

import com.mashup.dorabangs.data.datasource.remote.api.FolderRemoteDataSource
import com.mashup.dorabangs.data.model.CreateFolderResponseModel
import com.mashup.dorabangs.data.model.FolderListResponseModel
import com.mashup.dorabangs.data.model.FolderResponseModel
import com.mashup.dorabangs.data.model.LinksFromFolderResponseModel
import com.mashup.dorabangs.data.model.toData
import com.mashup.dorabangs.data.network.service.FolderService
import com.mashup.dorabangs.domain.model.NewFolderName
import com.mashup.dorabangs.domain.model.NewFolderNameList
import javax.inject.Inject

class FolderRemoteDataSourceImpl @Inject constructor(
    private val folderService: FolderService,
) : FolderRemoteDataSource {

    override suspend fun getFolders(): FolderListResponseModel =
        folderService.getFolders()

    override suspend fun getFolderById(folderId: String): FolderResponseModel =
        folderService.getFolderById(folderId)

    override suspend fun createFolder(folderList: NewFolderNameList): CreateFolderResponseModel {
        return folderService.createFolder(folderList.toData())
    }

    override suspend fun editFolderName(folderName: NewFolderName, folderId: String) {
        folderService.editFolderName(folderId, folderName.toData())
    }

    override suspend fun getLinksFromFolder(
        folderId: String?,
        page: Int,
        limit: Int,
        order: String,
        isRead: Boolean?,
    ): LinksFromFolderResponseModel =
        folderService.getLinkFolderList(
            folderId = folderId,
            page = page,
            limit = limit,
            order = order,
            isRead = isRead,
        )

    override suspend fun deleteFolder(folderId: String) {
        folderService.deleteFolder(folderId = folderId)
    }
}
