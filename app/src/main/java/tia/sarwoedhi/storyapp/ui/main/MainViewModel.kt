package tia.sarwoedhi.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import tia.sarwoedhi.storyapp.domain.entities.Story
import tia.sarwoedhi.storyapp.domain.use_case.LogOutUseCase
import tia.sarwoedhi.storyapp.domain.use_case.StoryListUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val storyListUseCase: StoryListUseCase, private val logOutUseCase: LogOutUseCase) : ViewModel() {
    fun getListStory(): LiveData<UiState<List<Story>>> {
        return storyListUseCase.invoke().asLiveData()
    }

    fun logOut():LiveData<UiState<Unit>>{
        return logOutUseCase.invoke().asLiveData()
    }
}