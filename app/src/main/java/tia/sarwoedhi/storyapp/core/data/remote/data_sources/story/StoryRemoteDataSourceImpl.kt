package tia.sarwoedhi.storyapp.core.data.remote.data_sources.story

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse
import tia.sarwoedhi.storyapp.core.data.local.data_store_local.DataStoreInterface
import tia.sarwoedhi.storyapp.core.data.remote.api.StoryApi
import javax.inject.Inject

class StoryRemoteDataSourceImpl @Inject constructor(private val storyApi: StoryApi, private val dataStoreInterface: DataStoreInterface) :
    StoryRemoteDataSource {

    override suspend fun getAllStories(): Flow<Resource<StoriesResponse>> = flow {
        try {
            dataStoreInterface.getTokenKey().collect {
                val result = storyApi.getListStory("Bearer $it")
                if (result.isSuccessful) {
                    emit(Resource.Success(result.body()))
                } else {
                    emit(Resource.Error(result.message()))
                }
            }

        } catch (e: Exception) {
            emit(Resource.Error((e.message ?: "").toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addStory(
        imageMultipart: MultipartBody.Part,
        description: RequestBody
    ): Flow<Resource<BaseResponse>> = flow {
        try {
            dataStoreInterface.getTokenKey().collect {
                val result = storyApi.addStory("Bearer $it",imageMultipart, description)
                if (result.isSuccessful) {
                    emit(Resource.Success(result.body()))
                } else {
                    emit(Resource.Error(result.message()))
                }
            }

        } catch (e: Exception) {
            emit(Resource.Error((e.message ?: "").toString()))
        }
    }.flowOn(Dispatchers.IO)
}