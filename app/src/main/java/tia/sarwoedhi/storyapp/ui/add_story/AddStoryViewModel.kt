package tia.sarwoedhi.storyapp.ui.add_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.AddStoryUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(val addStoryUseCase: AddStoryUseCase) : ViewModel() {
    private val _addState: MutableStateFlow<UiState<BaseResponse>> = MutableStateFlow(UiState.Loading)
    val addState: StateFlow<UiState<BaseResponse>> get() = _addState

    fun addStory(imageMultipart: MultipartBody.Part, description: RequestBody,
                      latitude : RequestBody, longitude:RequestBody) {
        viewModelScope.launch {
            addStoryUseCase(imageMultipart, description,latitude=latitude, longitude=longitude).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Loading
            ).collect {
                _addState.value = it
            }
        }
    }

}