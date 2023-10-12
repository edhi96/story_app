package tia.sarwoedhi.storyapp.core.data.repositories.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.RemoteKeysEntity
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.local.room.AppDatabase
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSource
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator constructor(
    private val database: AppDatabase,
    private val storyRemoteDataSource: StoryRemoteDataSource,
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>,
    ): MediatorResult {
        var result : MediatorResult = MediatorResult.Error(Exception())
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            val listDataStory = mutableListOf<StoryEntity>()
            val endOfPaginationReached: Boolean

            when (val response = storyRemoteDataSource.getAllStories(page, state.config.pageSize).firstOrNull()) {
                is Resource.Success -> {
                    response.data?.listStory?.forEach { story ->
                        listDataStory.add(
                            StoryEntity(
                                id = story.id,
                                name = story.name ?: "-",
                                description = story.description ?: "-",
                                photoUrl = story.photoUrl ?: "",
                                createdAt = story.createdAt ?: "",
                                lat = story.lat ?: 0.0,
                                lon = story.lon ?: 0.0
                            )
                        )
                    }
                    endOfPaginationReached = listDataStory.isEmpty()
                    if (loadType == LoadType.REFRESH) {
                        database.remoteKeysDao().deleteRemoteKeys()
                        database.storyDao().deleteAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = listDataStory.map {
                        RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }

                    database.remoteKeysDao().insertAll(keys.toList())
                    database.storyDao().insertAll(listDataStory.toList())
                    result = MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                is Resource.Error ->{
                    result = MediatorResult.Error(Exception(response.error))
                }
                else -> {}
            }
        } catch (exception: HttpException) {
            result =  MediatorResult.Error(exception)
        } catch (exception: IOException) {
            result = MediatorResult.Error(exception)
        }catch (exception: Exception) {
            result = MediatorResult.Error(exception)
        }
        return result
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}