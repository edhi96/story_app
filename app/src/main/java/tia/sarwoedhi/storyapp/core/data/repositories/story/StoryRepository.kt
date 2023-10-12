package tia.sarwoedhi.storyapp.core.data.repositories.story

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse

interface StoryRepository {
    suspend fun getAllStories(): Flow<PagingData<StoryEntity>>
    suspend fun addStory(imageMultipart: MultipartBody.Part, description: RequestBody,latitude : RequestBody, longitude:RequestBody): Flow<Resource<BaseResponse>>
    suspend fun getAllStoriesWithLocation(): Flow<Resource<StoriesResponse>>
}