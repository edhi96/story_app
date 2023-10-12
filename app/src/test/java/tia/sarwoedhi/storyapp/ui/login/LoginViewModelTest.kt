package tia.sarwoedhi.storyapp.ui.login

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
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.domain.use_case.LoginUseCase
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule
import tia.sarwoedhi.storyapp.utils.UiState
import tia.sarwoedhi.storyapp.utils.getOrAwaitValue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: LoginUseCase

    private lateinit var viewModel: LoginViewModel

    private val dummySuccessResponse = DataDummy.generateDummyLogin()

    private val dummyEmail = "edhitia@mail.com"
    private val dummyPassword = "Password"

    @Before
    fun setUp() {
        viewModel = LoginViewModel(useCase)
    }


    @Test
    fun `when login failed`() {
        val expectFailed: Flow<UiState<LoginResponse>> = flowOf(UiState.Error("failed"))

        Mockito.`when`(
            useCase.invoke(
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectFailed)

       viewModel.login(
            email = dummyEmail,
            password = dummyPassword
        )

        val result = viewModel.loginState.asLiveData().getOrAwaitValue()

        Mockito.verify(useCase).invoke(
            email = dummyEmail,
            password = dummyPassword
        )

        Assert.assertNotNull(result)
        Assert.assertEquals(expectFailed.asLiveData().getOrAwaitValue(), result)

    }

    @Test
    fun `when login successfully`() {
        val expectedSuccess: Flow<UiState<LoginResponse>> =
            flowOf(UiState.Success(dummySuccessResponse))

        Mockito.`when`(
            useCase.invoke(
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectedSuccess)

        viewModel.login(
            email = dummyEmail,
            password = dummyPassword
        )

        val result = viewModel.loginState.asLiveData()

        Mockito.verify(useCase).invoke(
            email = dummyEmail,
            password = dummyPassword
        )

        Assert.assertNotNull(result)
        Assert.assertEquals(expectedSuccess.asLiveData().getOrAwaitValue(), result.getOrAwaitValue())
    }
}