package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

class AddStoryUseCase @Inject constructor(private val storyRepository: StoryRepository) {
    operator fun invoke(
        imageMultipart: MultipartBody.Part, description: RequestBody, latitude : RequestBody, longitude : RequestBody):
            Flow<UiState<BaseResponse>> = flow {
        storyRepository.addStory(imageMultipart, description, latitude=latitude, longitude=longitude).onStart {
            emit(UiState.Loading)
        }.collect { response ->
            when (response) {
                is Resource.Success -> {
                    emit(UiState.Success(response.data))
                }
                is Resource.Error -> {
                    emit(UiState.Error(error = response.error))
                }
                else -> {}
            }
        }
    }
}