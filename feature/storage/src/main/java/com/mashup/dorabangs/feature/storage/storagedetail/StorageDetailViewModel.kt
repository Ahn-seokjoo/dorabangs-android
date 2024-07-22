package com.mashup.dorabangs.feature.storage.storagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.mashup.dorabangs.core.coroutine.doraLaunch
import com.mashup.dorabangs.core.designsystem.R
import com.mashup.dorabangs.domain.model.Folder
import com.mashup.dorabangs.domain.model.PostInfo
import com.mashup.dorabangs.domain.usecase.folder.DeleteFolderUseCase
import com.mashup.dorabangs.domain.usecase.folder.GetFolderListUseCase
import com.mashup.dorabangs.domain.usecase.folder.GetSavedLinksFromFolderUseCase
import com.mashup.dorabangs.domain.usecase.posts.ChangePostFolder
import com.mashup.dorabangs.domain.usecase.posts.DeletePost
import com.mashup.dorabangs.domain.usecase.posts.GetPosts
import com.mashup.dorabangs.domain.usecase.posts.PatchPostInfoUseCase
import com.mashup.dorabangs.feature.storage.storagedetail.model.EditActionType
import com.mashup.dorabangs.feature.storage.storagedetail.model.FolderType
import com.mashup.dorabangs.feature.storage.storagedetail.model.StorageDetailSideEffect
import com.mashup.dorabangs.feature.storage.storagedetail.model.StorageDetailSort
import com.mashup.dorabangs.feature.storage.storagedetail.model.StorageDetailState
import com.mashup.dorabangs.feature.storage.storagedetail.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class StorageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val savedLinksFromFolderUseCase: GetSavedLinksFromFolderUseCase,
    private val patchPostInfoUseCase: PatchPostInfoUseCase,
    private val getPostsUseCase: GetPosts,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deletePostUseCase: DeletePost,
    private val getFolderListUseCase: GetFolderListUseCase,
    private val changePostFolderUseCase: ChangePostFolder
) : ViewModel(), ContainerHost<StorageDetailState, StorageDetailSideEffect> {
    override val container = container<StorageDetailState, StorageDetailSideEffect>(StorageDetailState())

    fun setFolderInfo(folderItem: Folder) = intent {
        reduce {
            state.copy(
                folderInfo = state.folderInfo.copy(
                    folderId = folderItem.id,
                    title = folderItem.name,
                    postCount = folderItem.postCount,
                    folderType = folderItem.type,
                ),
            )
        }
        fetchSavedLinkFromType(
            type = folderItem.type,
            folderId = folderItem.id,
        )
    }

    /**
     * 폴더 타입별 API 호출 분기 처리
     */
    private fun fetchSavedLinkFromType(
        type: String = "",
        folderId: String? = "",
        order: String = StorageDetailSort.ASC.name,
        isRead: Boolean? = null,
    ) {
        when (type) {
            FolderType.All.type -> getSavedLinkFromDefaultFolder(order = order, favorite = false, isRead = isRead)
            FolderType.Favorite.type -> getSavedLinkFromDefaultFolder(order = order, favorite = true, isRead = isRead)
            else -> getSavedLinkFromCustomFolder(folderId = folderId, order = order, isRead = isRead)
        }
    }

    /**
     * 사용자 지정 folder links
     */
    private fun getSavedLinkFromCustomFolder(
        folderId: String?,
        order: String = StorageDetailSort.ASC.name,
        isRead: Boolean? = null,
    ) = viewModelScope.doraLaunch {
        val pagingData = savedLinksFromFolderUseCase.invoke(folderId = folderId, order = order, isRead = isRead)
            .cachedIn(viewModelScope).map { pagedData ->
                pagedData.map { savedLinkInfo -> savedLinkInfo.toUiModel() }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = PagingData.empty(),
            )
        intent {
            reduce {
                state.copy(pagingList = pagingData)
            }
        }
    }

    /**
     * default folder links
     */
    private fun getSavedLinkFromDefaultFolder(
        order: String = StorageDetailSort.ASC.name,
        favorite: Boolean = false,
        isRead: Boolean? = null,
    ) = viewModelScope.doraLaunch {
        val pagingData =
            getPostsUseCase.invoke(order = order, favorite = favorite, isRead = isRead)
                .cachedIn(viewModelScope).map { pagedData ->
                    pagedData.map { savedLinkInfo -> savedLinkInfo.toUiModel() }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = PagingData.empty(),
                )
        intent {
            reduce {
                state.copy(pagingList = pagingData)
            }
        }
    }

    /**
     * 탭 변환
     */
    fun changeSelectedTabIdx(selectedIdx: Int) = viewModelScope.launch {
        intent {
            val isRead = if (selectedIdx == 0) null else false
            fetchSavedLinkFromType(
                type = state.folderInfo.folderType,
                folderId = state.folderInfo.folderId,
                order = state.isLatestSort.name,
                isRead = isRead,
            )
            reduce {
                state.copy(tabInfo = state.tabInfo.copy(selectedTabIdx = selectedIdx))
            }
        }
    }

    /**
     * 정렬 변환
     */
    fun clickFeedSort(item: StorageDetailSort) = viewModelScope.launch {
        intent {
            val isRead = if (state.tabInfo.selectedTabIdx == 0) null else false
            fetchSavedLinkFromType(
                type = state.folderInfo.folderType,
                folderId = state.folderInfo.folderId,
                order = item.name,
                isRead = isRead,
            )
            reduce {
                state.copy(
                    isLatestSort = item,
                )
            }
        }
    }

    /**
     * 즐겨찾기 추가
     * 낙관적 Update추가, 스크롤 위치가 왜 변할까?
     */
    fun addFavoriteItem(postId: String, isFavorite: Boolean) = viewModelScope.doraLaunch {
        intent {
            val postInfo = PostInfo(isFavorite = !isFavorite)
            patchPostInfoUseCase(
                postId = postId,
                postInfo = postInfo,
            )
//            reduce {
////                val updatedCardList = state.pagingList.map { pagingData ->
////                    pagingData.map { currentItem ->
////                        if (currentItem.id == postId) {
////                            currentItem.copy(isFavorite = !isFavorite)
////                        } else {
////                            currentItem
////                        }
////                    }
////                }
////                state.copy(pagingList = updatedCardList)
//            }
            intent { postSideEffect(StorageDetailSideEffect.RefreshPagingList) }
        }
    }

    /**
     * 폴더 삭제
     */
    fun deleteFolder(folderId: String?) = viewModelScope.doraLaunch {
        folderId?.let { id ->
            val isSuccessDelete = deleteFolderUseCase(folderId = id)
            if (isSuccessDelete.isSuccess) {
                setVisibleDialog(false)
                intent { postSideEffect(StorageDetailSideEffect.NavigateToHome) }
            } else {
                // TODO - 에러처리
            }
        }
    }

    /**
     * 링크 삭제
     */
    fun deletePost(postId: String) = viewModelScope.doraLaunch {
        deletePostUseCase(postId)
        setVisibleDialog(false)
        // fetchSavedLinkFromType() TODO - 새로 데이터 불러오거느 update
    }

    /**
     * 현재 폴더 리스트 가져오기
     */
    fun getFolderList() = viewModelScope.doraLaunch {
        val customFolderList = getFolderListUseCase().customFolders
        intent {
            reduce {
                state.copy(folderList = customFolderList)
            }
            setVisibleMovingFolderBottomSheet(true)
        }
    }

    /**
     * 링크 폴더 이동
     */
    fun moveFolder(postId: String, folderId: String) = viewModelScope.doraLaunch {
        changePostFolderUseCase(postId = postId, folderId = folderId)
        setVisibleMovingFolderBottomSheet(false)
        //TODO - 실패 성공 여부 리스트 업데이트
        intent { postSideEffect(StorageDetailSideEffect.RefreshPagingList) }
    }


    fun setVisibleMoreButtonBottomSheet(visible: Boolean) = intent {
        val bottomSheet = when (state.editActionType) {
            EditActionType.FolderEdit -> {
                state.moreBottomSheetState.copy(
                    isShowMoreButtonSheet = visible,
                    firstItem = R.string.remove_dialog_folder_title,
                    secondItem = R.string.rename_folder_bottom_sheet_title,
                )
            }
            EditActionType.LinkEdit -> {
                state.moreBottomSheetState.copy(
                    isShowMoreButtonSheet = visible,
                    firstItem = R.string.remove_dialog_title,
                    secondItem = R.string.moving_folder_dialog_title,
                )
            }
        }
        reduce {
            state.copy(moreBottomSheetState = bottomSheet)
        }
    }

    fun setVisibleDialog(visible: Boolean) = intent {
        val dialogState = when (state.editActionType) {
            EditActionType.FolderEdit -> {
                state.editDialogState.copy(
                    isShowDialog = visible,
                    dialogTitle = R.string.remove_dialog_folder_title,
                    dialogCont = R.string.remove_dialog_folder_cont,
                )
            }
            EditActionType.LinkEdit -> {
                state.editDialogState.copy(
                    isShowDialog = visible,
                    dialogTitle = R.string.remove_dialog_title,
                    dialogCont = R.string.remove_dialog_content,
                )
            }
        }
        reduce {
            state.copy(editDialogState = dialogState)
        }
    }

    fun setActionType(type: EditActionType, postId: String = "") = intent {
        reduce { state.copy(editActionType = type, currentClickPostId = postId) }
    }

    fun moveToEditFolderName(folderId: String?) = intent {
        postSideEffect(StorageDetailSideEffect.NavigateToEditFolder(folderId = folderId.orEmpty()))
    }

    fun setVisibleMovingFolderBottomSheet(visible: Boolean, isNavigate: Boolean = false) = intent {
        reduce {
            state.copy(isShowMovingFolderSheet = visible)
        }
        if (isNavigate) postSideEffect(StorageDetailSideEffect.NavigateToCreateFolder)
    }
}
