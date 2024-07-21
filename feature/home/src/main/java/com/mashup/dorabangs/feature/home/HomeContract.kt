package com.mashup.dorabangs.feature.home

import androidx.paging.PagingData
import com.mashup.dorabangs.core.designsystem.component.card.FeedCardUiModel
import com.mashup.dorabangs.core.designsystem.component.chips.DoraChipUiModel
import com.mashup.dorabangs.domain.utils.isValidUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeState(
    val clipBoardState: ClipBoardState = ClipBoardState(),
    val tapElements: List<DoraChipUiModel> = emptyList(),
    val feedCards: Flow<PagingData<FeedCardUiModel>> = emptyFlow(),
    val selectedIndex: Int = 0,
    val isShowMoreButtonSheet: Boolean = false,
    val isShowDialog: Boolean = false,
    val isShowMovingFolderSheet: Boolean = false,
    val homeCreateFolder: HomeCreateFolder = HomeCreateFolder(),
    val aiClassificationCount: Int = 0,
)

data class ClipBoardState(
    val copiedText: String = "",
) {
    val isValidUrl = copiedText.isNotBlank() && copiedText.isValidUrl()
}

data class HomeCreateFolder(
    val folderName: String = "",
    val helperEnable: Boolean = false,
    val helperMessage: String = "",
    val urlLink: String = "",
)
