package tia.sarwoedhi.storyapp.domain.use_case

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepository
import tia.sarwoedhi.storyapp.utils.UiState
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<UiState<String>> = flow {
        authRepository.getTokenKey().onStart {
            UiState.Loading
        }.collect { response ->
            if(response.isNotEmpty()){
                emit(UiState.Success(response))
            }else{
                emit(UiState.Error(error = "Empty"))
            }
        }
    }.flowOn(Dispatchers.IO)
}