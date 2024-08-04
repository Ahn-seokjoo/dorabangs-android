package com.mashup.dorabangs.feature.storage.storagedetail.model

import com.mashup.dorabangs.core.designsystem.component.bottomsheet.SelectableBottomSheetItemUIModel
import com.mashup.dorabangs.core.designsystem.component.chips.FeedUiModel
import com.mashup.dorabangs.domain.model.Folder
import com.mashup.dorabangs.domain.model.Post
import com.mashup.dorabangs.domain.model.SavedLinkDetailInfo
import com.mashup.dorabangs.core.designsystem.R as coreR

fun SavedLinkDetailInfo.toUiModel(): FeedUiModel.FeedCardUiModel {
    return FeedUiModel.FeedCardUiModel(
        postId = this.id.orEmpty(),
        title = this.title,
        content = this.description,
        createdAt = this.createdAt,
        keywordList = this.keywords?.map { it.name },
        isFavorite = isFavorite ?: false,
        thumbnail = this.thumbnailImgUrl,
        folderId = this.folderId.orEmpty(),
        url = this.url.orEmpty(),
    )
}

fun Post.toUiModel(): FeedUiModel.FeedCardUiModel {
    return FeedUiModel.FeedCardUiModel(
        postId = this.id,
        title = this.title,
        content = this.description,
        createdAt = this.createdAt,
        keywordList = listOf(),
        isFavorite = isFavorite,
        thumbnail = this.thumbnailImgUrl,
        folderId = this.folderId,
        url = this.url,
    )
}

fun List<Folder>.toSelectBottomSheetModel(folderId: String): List<SelectableBottomSheetItemUIModel> {
    return this.map { item ->
        SelectableBottomSheetItemUIModel(
            id = item.id.orEmpty(),
            icon = coreR.drawable.ic_3d_folder_big,
            itemName = item.name,
            isSelected = item.id == folderId,
        )
    }
}
