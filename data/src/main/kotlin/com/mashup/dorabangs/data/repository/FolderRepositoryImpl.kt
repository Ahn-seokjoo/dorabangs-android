package com.mashup.dorabangs.data.repository

import com.mashup.dorabangs.data.datasource.remote.api.FolderRemoteDataSource
import com.mashup.dorabangs.data.model.toDomain
import com.mashup.dorabangs.domain.model.CreateCompleteFolderInfo
import com.mashup.dorabangs.domain.model.CreateFolder
import com.mashup.dorabangs.domain.model.EditCompleteFolderInfo
import com.mashup.dorabangs.domain.model.EditFolder
import com.mashup.dorabangs.domain.model.Folder
import com.mashup.dorabangs.domain.model.FolderList
import com.mashup.dorabangs.domain.repository.FolderRepository
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val remoteDataSource: FolderRemoteDataSource,
) : FolderRepository {

    override suspend fun getFolders(): FolderList =
        remoteDataSource.getFolders()
            .toDomain()

    override suspend fun getFolderById(folderId: String): Folder =
        remoteDataSource.getFolderById(folderId).toDomain()

    override suspend fun createFolder(createFolder: CreateFolder) =
        remoteDataSource.createFolder(folderList = createFolder)

    override suspend fun editFolderName(
        editFolder: EditFolder,
        folderId: String
    ): EditCompleteFolderInfo =
        remoteDataSource.editFolderName(folderName = editFolder, folderId = folderId).toDomain()
}
