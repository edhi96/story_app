package tia.sarwoedhi.storyapp.domain.use_case

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepository
import javax.inject.Inject

class StoryListUseCase @Inject constructor(private val storyRepository: StoryRepository) {
    suspend operator fun invoke(): Flow<PagingData<StoryEntity>> = storyRepository.getAllStories()
}