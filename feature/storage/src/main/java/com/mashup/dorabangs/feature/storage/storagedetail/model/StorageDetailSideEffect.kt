package com.mashup.dorabangs.feature.storage.storagedetail.model

sealed class StorageDetailSideEffect {
    object NavigateToHome : StorageDetailSideEffect()
    data class NavigateToEditFolder(val folderId: String) : StorageDetailSideEffect()
    object NavigateToCreateFolder : StorageDetailSideEffect()
    object RefreshPagingList : StorageDetailSideEffect()
}
