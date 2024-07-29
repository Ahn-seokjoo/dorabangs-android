package com.mashup.dorabangs.feature.home

sealed class HomeSideEffect {
    data class ShowSnackBar(val copiedText: String) : HomeSideEffect()
    object HideSnackBar : HomeSideEffect()
    object NavigateToCreateFolder : HomeSideEffect()
    object NavigateToHome : HomeSideEffect()
    data class SaveLink(val folderId: String, val urlLink: String) : HomeSideEffect()
    object NavigateHomeAfterSaveLink : HomeSideEffect()
    data class NavigateSelectLinkFromService(val urlLink: String) : HomeSideEffect()
    object ShowFolderRemoveToastSnackBar : HomeSideEffect()
}
