package com.mashup.dorabangs.feature.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.dorabangs.core.coroutine.doraLaunch
import com.mashup.dorabangs.domain.model.NewFolderNameList
import com.mashup.dorabangs.domain.usecase.folder.CreateFolderUseCase
import com.mashup.dorabangs.domain.usecase.onboarding.GetOnBoardingKeywordsUseCase
import com.mashup.dorabangs.domain.usecase.user.SetIsFirstEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val getOnBoardingKeywordsUseCase: GetOnBoardingKeywordsUseCase,
    private val setIsFirstEntryUseCase: SetIsFirstEntryUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<OnBoardingState, OnBoardingSideEffect> {
    override val container = container<OnBoardingState, OnBoardingSideEffect>(OnBoardingState())

    init {
        fetchOnBoardingKeywords()
    }

    fun onClickKeyword(index: Int) = intent {
        reduce {
            if (state.selectedIndex.contains(index)) {
                state.copy(selectedIndex = state.selectedIndex.minus(index))
            } else {
                state.copy(selectedIndex = state.selectedIndex.plus(index))
            }
        }
    }

    fun onClickOkButton() = intent {
        postSideEffect(OnBoardingSideEffect.NavigateToHome)
        setIsFirstEntryUseCase(false)
        createFolderUseCase(
            NewFolderNameList(
                state.keywords.filterIndexed { index, _ -> index in state.selectedIndex },
            ),
        )
    }

    private fun fetchOnBoardingKeywords(limit: Int? = null) = viewModelScope.doraLaunch {
        val onBoardingKeywords = getOnBoardingKeywordsUseCase.invoke(limit)
        intent {
            reduce {
                state.copy(
                    keywords = onBoardingKeywords,
                )
            }
        }
    }
}
