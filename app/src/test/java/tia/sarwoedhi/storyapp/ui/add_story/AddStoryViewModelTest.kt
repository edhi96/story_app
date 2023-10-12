package tia.sarwoedhi.storyapp.ui.add_story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import tia.sarwoedhi.storyapp.DataDummy
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.AddStoryUseCase
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.getOrAwaitValue
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var viewModel: AddStoryViewModel

    @Mock
    private lateinit var useCase: AddStoryUseCase

    @Mock
    private lateinit var photo: MultipartBody.Part

    @Mock
    private lateinit var description: RequestBody

    @Mock
    private lateinit var lat: RequestBody

    @Mock
    private lateinit var lon: RequestBody

    @Mock
    private lateinit var addStoryResponse: UiState<BaseResponse>

    private val dummySuccessResponse = DataDummy.generateDummyBaseResponseSuccess()

    @Before
    fun setUp() {
        description = "".toRequestBody("text/plain".toMediaType())
        lat = 0.toString().toRequestBody("text/plain".toMediaType())
        lon = 0.toString().toRequestBody("text/plain".toMediaType())
        val requestImageFile = File("test").asRequestBody("image/jpeg".toMediaTypeOrNull())
        photo = MultipartBody.Part.createFormData(
            "photo",
            "file",
            requestImageFile
        )

        viewModel = AddStoryViewModel(useCase)
    }

    @Test
    fun `add new story successfully`() = runTest {
        val response: Flow<UiState<BaseResponse>> = flowOf(UiState.Success(dummySuccessResponse))
        `when`(useCase.invoke(photo, description, lat, lon)).thenReturn(response)
        viewModel.addStory(photo, description, lat, lon)
        val result = viewModel.addState.asLiveData().getOrAwaitValue()
        advanceUntilIdle()
        Mockito.verify(useCase).invoke(photo, description, lat, lon)
        Assert.assertEquals(response.asLiveData().getOrAwaitValue(), result)
    }

    @Test
    fun `add new story failed`() = runTest {
        val response: Flow<UiState<BaseResponse>> = flowOf(UiState.Error("Failed"))
        `when`(useCase.invoke(photo, description, lat, lon)).thenReturn(response)
        viewModel.addStory(photo, description, lat, lon)
        val actualResult = viewModel.addState.asLiveData().getOrAwaitValue()
        advanceUntilIdle()
        Assert.assertEquals(response.asLiveData().getOrAwaitValue(), actualResult)
    }

}