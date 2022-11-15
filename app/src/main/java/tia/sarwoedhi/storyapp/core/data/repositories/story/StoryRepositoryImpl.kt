package tia.sarwoedhi.storyapp.core.data.repositories.story

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSource
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(private val storyRemoteDataSource: StoryRemoteDataSource) :
    StoryRepository {
    override suspend fun getAllStories(): Flow<Resource<StoriesResponse>> {
        return storyRemoteDataSource.getAllStories()
    }

    override suspend fun addStory(imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): Flow<Resource<BaseResponse>> {
        return storyRemoteDataSource.addStory(imageMultipart,description)
    }
}