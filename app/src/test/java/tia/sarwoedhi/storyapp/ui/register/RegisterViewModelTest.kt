package tia.sarwoedhi.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import tia.sarwoedhi.storyapp.DataDummy
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.domain.use_case.RegisterUseCase
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: RegisterUseCase

    private lateinit var viewModel: RegisterViewModel

    private val dummySuccessResponse = DataDummy.generateDummyBaseResponseSuccess()

    private val dummyName = "Edhitia"
    private val dummyEmail = "edhitia@mail.com"
    private val dummyPassword = "Password"

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(useCase)
    }

    @Test
    fun `when register failed`() {
        val expectedFailed: Flow<UiState<BaseResponse>> = flowOf(UiState.Error("failed"))

        Mockito.`when`(
            useCase.invoke(
                name = dummyName,
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectedFailed)

        viewModel.register(
            name = dummyName,
            email = dummyEmail,
            password = dummyPassword
        )
        val result = viewModel.registerState.asLiveData()

        Mockito.verify(useCase).invoke(
            name = dummyName,
            email = dummyEmail,
            password = dummyPassword
        )

        Assert.assertNotNull(result)
        Assert.assertEquals(expectedFailed.asLiveData().getOrAwaitValue(), result.getOrAwaitValue())
    }

    @Test
    fun `when register successfully`() {
        val expectedSuccessResponse: Flow<UiState<BaseResponse>> =
            flowOf(UiState.Success(dummySuccessResponse))

        Mockito.`when`(
            useCase.invoke(
                name = dummyName,
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectedSuccessResponse)

        viewModel.register(
            name = dummyName,
            email = dummyEmail,
            password = dummyPassword
        )
        val result = viewModel.registerState.asLiveData()

        Mockito.verify(useCase).invoke(
            name = dummyName,
            email = dummyEmail,
            password = dummyPassword
        )


        Assert.assertNotNull(result)
        Assert.assertEquals(expectedSuccessResponse.asLiveData().getOrAwaitValue(), result.getOrAwaitValue())
    }
}