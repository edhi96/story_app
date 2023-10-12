package tia.sarwoedhi.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import tia.sarwoedhi.storyapp.DataDummy
import tia.sarwoedhi.storyapp.domain.use_case.MapUseCase
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: MapUseCase

    private lateinit var viewModel: MapsViewModel

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesEntity()

    @Before
    fun setUp() {
        viewModel = MapsViewModel(useCase)
    }

    @Test
    fun `when get List Story with location successfully`(): Unit = runTest {
        val expectedStories = flowOf(UiState.Success(dummyStoriesResponse))

        `when`(useCase.invoke()).thenReturn(expectedStories)

        viewModel.getAllStoriesWithLocation()
        val actualStories = viewModel.mapStories.asFlow().asLiveData()
        verify(useCase).invoke()
        Assert.assertSame(expectedStories.asLiveData().getOrAwaitValue(), actualStories.getOrAwaitValue())
        assertNotNull(actualStories.getOrAwaitValue())
    }

}