package tia.sarwoedhi.storyapp.core.data.repositories.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import tia.sarwoedhi.storyapp.DataDummy
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.local.data_store_local.DataStoreInterface
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth.RemoteAuthDataSource
import tia.sarwoedhi.storyapp.utils.CoroutinesTestRule

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTesRule = CoroutinesTestRule()

    @Mock
    private lateinit var remoteAuth: RemoteAuthDataSource

    @Mock
    private lateinit var dataStoreInterface: DataStoreInterface

    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var bodyLogin: BodyLogin

    @Mock
    private lateinit var bodyRegister: BodyRegister

    @Before
    fun setup() {
        authRepository = AuthRepositoryImpl(remoteAuth , dataStoreInterface)
    }


    @Test
    fun `when login successfully`() = runTest {

        val expectedSuccessResponse = DataDummy.generateDummyLogin()

        Mockito.`when`(remoteAuth.login(bodyLogin)).thenReturn( flowOf(Resource.Success(expectedSuccessResponse)) )

        authRepository.login(bodyLogin).collect {
            when(it) {
                is Resource.Success -> {
                    assertTrue(it.data?.error == false)
                    assertNotNull(it.data)
                    assertEquals(expectedSuccessResponse, it.data)
                }
                else -> {}
            }
        }
    }

    @Test
    fun `when register successfully`() = runTest {
        val expectedSuccessResponse = DataDummy.generateDummyBaseResponseSuccess()

        Mockito.`when`(remoteAuth.register(bodyRegister)).thenReturn( flowOf(Resource.Success(DataDummy.generateDummyBaseResponseSuccess())) )

        authRepository.register(bodyRegister).collect {
            when(it) {
                is Resource.Success -> {
                    assertTrue(it.data?.error == false)
                    assertNotNull(it.data)
                    assertEquals(expectedSuccessResponse, it.data)
                }
                else -> {}
            }
        }
    }
}