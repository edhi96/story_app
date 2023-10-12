package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject


class MapUseCase @Inject constructor(private val storyRepository: StoryRepository) {

    operator fun invoke(): Flow<UiState<List<StoryEntity>>> = flow {
        storyRepository.getAllStoriesWithLocation().onStart {
            emit(UiState.Loading)
        }.collect { response ->
            when (response) {
                is Resource.Success -> {
                    val domain = response.data?.listStory?.map {
                        it
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