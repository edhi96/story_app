package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(
        email: String,
        password: String,
        name: String
    ): Flow<UiState<BaseResponse>> = flow {
        authRepository.register(BodyRegister(email = email, password = password,name = name)).onStart {
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