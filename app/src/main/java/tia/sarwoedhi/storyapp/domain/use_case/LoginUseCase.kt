package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {

    operator fun invoke(email: String, password: String): Flow<UiState<LoginResponse>> = flow {
        authRepository.login(BodyLogin(email = email, password = password)).onStart {
            emit(UiState.Loading)
        }.collect { response ->
            when (response) {
                is Resource.Success -> {
                    emit(UiState.Success(response.data))
                }
                is Resource.Error -> {
                    emit(UiState.Error(error = response.error))
                }
                else -> {}
            }
        }
    }

}
