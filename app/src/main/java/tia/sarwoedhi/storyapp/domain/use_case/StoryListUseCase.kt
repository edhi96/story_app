package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepository
import tia.sarwoedhi.storyapp.domain.entities.Story
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

class StoryListUseCase @Inject constructor(private val storyRepository: StoryRepository) {
    operator fun invoke(): Flow<UiState<List<Story>>> = flow {
        storyRepository.getAllStories().onStart {
            emit(UiState.Loading)
        }.collect { response ->
            when (response) {
                is Resource.Success -> {
                    val domain = response.data?.listStory?.map {
                        Story(it.name, it.description, it.photoUrl,it.createdAt)
                    }?.toList()
                    emit(UiState.Success(domain))
                }
                is Resource.Error -> {
                    emit(UiState.Error(error = response.error))
                }
                else -> {
                }
            }
        }
    }
}