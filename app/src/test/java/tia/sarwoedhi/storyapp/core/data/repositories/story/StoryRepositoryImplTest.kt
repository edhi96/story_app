package tia.sarwoedhi.storyapp.core.data.repositories.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import tia.sarwoedhi.storyapp.DataDummy
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse
import tia.sarwoedhi.storyapp.core.data.local.room.AppDatabase
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSource
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class StoryRepositoryImplTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTesRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRemoteDataSource: StoryRemoteDataSource

    @Mock
    private lateinit var appDatabase: AppDatabase

    private lateinit var storyRepository: StoryRepository

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var photo: MultipartBody.Part

    @Mock
    private lateinit var description: RequestBody

    @Mock
    private lateinit var lat: RequestBody

    @Mock
    private lateinit var lon: RequestBody

    @Before
    fun setup() {
        storyRepository = StoryRepositoryImpl(storyRemoteDataSource, appDatabase)
    }


    @Test
    fun `when get List Story with location successfully`() = runTest {
        val response = StoriesResponse(
            error = false,
            message = "success",
            DataDummy.generateDummyStoriesEntity().toMutableList()
        )
        val expectedStories = flowOf(Resource.Success(response))

        Mockito.`when`(storyRemoteDataSource.getAllStoriesWithLocationResponse())
            .thenReturn(expectedStories)

        storyRepository.getAllStoriesWithLocation().collect { result ->
            Mockito.verify(storyRemoteDataSource).getAllStoriesWithLocationResponse()
            assertTrue(result is Resource.Success<StoriesResponse>)
            if (result is Resource.Success) assertSame(response, result.data)
        }
    }

    @Test
    fun `when get List Story with location failed`() = runTest {
        val expectedStories2 = flowOf(Resource.Error("Failed"))

        Mockito.`when`(storyRemoteDataSource.getAllStoriesWithLocationResponse())
            .thenReturn(expectedStories2)

        storyRepository.getAllStoriesWithLocation().collect { result ->
            Mockito.verify(storyRemoteDataSource).getAllStoriesWithLocationResponse()
            assertTrue(result is Resource.Error)
        }
    }


    @Test
    fun `add new story successfully`() = runTest {
        val expectedResponse = DataDummy.generateDummyBaseResponseSuccess()
        val flow = flowOf(Resource.Success(expectedResponse))

        Mockito.`when`(
            storyRemoteDataSource.addStory(
                imageMultipart = photo,
                description = description,
                latitude = lat,
                longitude = lon
            )
        ).thenReturn(flow)

        storyRepository.addStory(
            imageMultipart = photo,
            description = description,
            latitude = lat,
            longitude = lon
        ).collect { result ->
            Mockito.verify(storyRemoteDataSource).addStory(
                imageMultipart = photo,
                description = description,
                latitude = lat,
                longitude = lon
            )
            assertTrue(result is Resource.Success)
            if (result is Resource.Success) assertSame(expectedResponse, result.data)
        }
    }

    @Test
    fun `add new story failed`() = runTest {
        val expected = flowOf(Resource.Error("Failed"))

        Mockito.`when`(
            storyRemoteDataSource.addStory(
                imageMultipart = photo,
                description = description,
                latitude = lat,
                longitude = lon
            )
        ).thenReturn(expected)

        storyRepository.addStory(
            imageMultipart = photo,
            description = description,
            latitude = lat,
            longitude = lon
        ).collect { result ->
            Mockito.verify(storyRemoteDataSource).addStory(
                imageMultipart = photo,
                description = description,
                latitude = lat,
                longitude = lon
            )
            assertTrue(result is Resource.Error)
        }
    }
}

