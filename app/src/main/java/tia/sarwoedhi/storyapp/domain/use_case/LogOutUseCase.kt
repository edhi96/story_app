package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject


class LogOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<UiState<Unit>> = flow<UiState<Unit>> {
        authRepository.logout()
        emit(UiState.Success(Unit))
    }.flowOn(Dispatchers.IO)
}