package tia.sarwoedhi.storyapp.core.data.remote.data_sources.story

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse

interface StoryRemoteDataSource {
    suspend fun getAllStories(
        page: Int? = null,
        pageSize: Int? = null
    ): Flow<Resource<StoriesResponse>>

    suspend fun getAllStoriesWithLocationResponse(): Flow<Resource<StoriesResponse>>

    suspend fun addStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody
    ): Flow<Resource<BaseResponse>>

}