package tia.sarwoedhi.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.domain.use_case.LogOutUseCase
import tia.sarwoedhi.storyapp.domain.use_case.StoryListUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val storyListUseCase: StoryListUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {
    private val _stories = MutableStateFlow(PagingData.empty<StoryEntity>())
    val stories: StateFlow<PagingData<StoryEntity>> get() = _stories

    private val _loginState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(UiState.Loading)
    val loginState: StateFlow<UiState<Unit>> get() = _loginState

    fun getListStory() {
        viewModelScope.launch {
            storyListUseCase.invoke()
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = PagingData.empty()
                ).cachedIn(viewModelScope).collect { result ->
                    _stories.value = result
                }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase.invoke().stateIn(
                scope = viewModelScope,
                started = WhileSubscribed(5_000),
                initialValue = UiState.Loading
            ).collect { result ->
                _loginState.value = result
            }
        }

    }
}