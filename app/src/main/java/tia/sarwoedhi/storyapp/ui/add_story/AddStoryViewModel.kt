package tia.sarwoedhi.storyapp.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.AddStoryUseCase
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(val addStoryUseCase: AddStoryUseCase) : ViewModel() {
    fun addStory(
        imageMultipart: MultipartBody.Part, description: RequestBody
    ): LiveData<UiState<BaseResponse>> {
        return addStoryUseCase(imageMultipart, description).asLiveData()
    }

}