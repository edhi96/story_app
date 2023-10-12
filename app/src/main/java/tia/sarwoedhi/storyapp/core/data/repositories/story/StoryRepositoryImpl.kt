package tia.sarwoedhi.storyapp.core.data.repositories.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse
import tia.sarwoedhi.storyapp.core.data.local.room.AppDatabase
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSource
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(private val storyRemoteDataSource: StoryRemoteDataSource,
                                              private val appDatabase: AppDatabase,) : StoryRepository {

    override suspend fun getAllStories(): Flow<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = StoryRemoteMediator(database = appDatabase , storyRemoteDataSource = storyRemoteDataSource ),
            pagingSourceFactory = {
                appDatabase.storyDao().selectAll()
            }
        ).flow
    }

    override suspend fun addStory(imageMultipart: MultipartBody.Part,
        description: RequestBody, latitude : RequestBody, longitude:RequestBody
    ): Flow<Resource<BaseResponse>> {
        return storyRemoteDataSource.addStory(imageMultipart,description,latitude= latitude, longitude= longitude)
    }

    override suspend fun getAllStoriesWithLocation(): Flow<Resource<StoriesResponse>> {
        return storyRemoteDataSource.getAllStoriesWithLocationResponse()
    }
}