package tia.sarwoedhi.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
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
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.domain.use_case.LogOutUseCase
import tia.sarwoedhi.storyapp.domain.use_case.StoryListUseCase
import tia.sarwoedhi.storyapp.ui.main.MainViewModel
import tia.sarwoedhi.storyapp.ui.main.adapter.StoryAdapter
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule
import tia.sarwoedhi.storyapp.utils.PagedTestDataSource
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyListUseCase: StoryListUseCase

    @Mock
    private lateinit var logOutUseCase: LogOutUseCase

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var fakeList: Flow<PagingData<StoryEntity>>

    @Mock
    private lateinit var storiesObserver: Observer<PagingData<StoryEntity>>

    private val dummyStoriesResponse = DataDummy.generateDummyStory()

    @Before
    fun setUp() {
        viewModel = MainViewModel(storyListUseCase, logOutUseCase)
    }

    @Test
    fun `when get List Story successfully`(): Unit = runTest {
        val flowData: Flow<PagingData<StoryEntity>> = flow { fakeList }

        `when`(storyListUseCase.invoke()).thenReturn(flowData)
        viewModel.getListStory()
        advanceUntilIdle()
        viewModel.stories.asLiveData().getOrAwaitValue()

        Mockito.verify(storyListUseCase).invoke()
        Assert.assertNotNull(viewModel.stories.value)
    }

    @Test
    fun `when get List Story successfully and first item same`(): Unit = runTest {
        val data = PagedTestDataSource.snapshot(dummyStoriesResponse)
        val expectedStories = flowOf(data)

        `when`(storyListUseCase.invoke()).thenReturn(expectedStories)

        viewModel.getListStory()
        advanceUntilIdle()
        val actualStories = viewModel.stories.asLiveData().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )

        actualStories.let {
            differ.submitData(it)
        }

        Mockito.verify(storyListUseCase).invoke()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStoriesResponse[0], differ.snapshot()[0])
        Assert.assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
    }

    @Test
    fun `when get List Story is empty`(): Unit = runTest {
        val flowData: Flow<PagingData<StoryEntity>> = flow { PagingData.empty<StoryEntity>() }

        `when`(storyListUseCase.invoke()).thenReturn(flowData)
        viewModel.getListStory()
        advanceUntilIdle()
        val actualStories = viewModel.stories.asLiveData().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )

        actualStories.let {
            differ.submitData(it)
        }

        Mockito.verify(storyListUseCase).invoke()
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(0, differ.snapshot().size)
    }

    @Test
    fun `when logOut is called should call invoke() in LogOutUseCase`() = runTest {
        val expected = flowOf(UiState.Success(Unit))
        `when`(logOutUseCase.invoke()).thenReturn(expected)
        viewModel.logOut()
        Mockito.verify(logOutUseCase).invoke()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}