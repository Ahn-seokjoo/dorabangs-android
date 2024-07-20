package com.mashup.dorabangs.domain.usecase.folder

import com.mashup.dorabangs.domain.model.DoraSampleResponse
import com.mashup.dorabangs.domain.model.NewFolderNameList
import com.mashup.dorabangs.domain.repository.FolderRepository
import javax.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository,
) {
    suspend operator fun invoke(folderList: NewFolderNameList): DoraSampleResponse {
        return folderRepository.createFolder(newFolderNameList = folderList)
    }
}
