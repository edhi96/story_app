package tia.sarwoedhi.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.domain.use_case.MapUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val useCase: MapUseCase) : ViewModel() {

    private val _mapStories: MutableLiveData<UiState<List<StoryEntity>>> = MutableLiveData()
    val mapStories: LiveData<UiState<List<StoryEntity>>> get() = _mapStories

    fun getAllStoriesWithLocation() {
        viewModelScope.launch {
            useCase.invoke().stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Loading
            ).collect {
                _mapStories.value = it
            }
        }
    }

}