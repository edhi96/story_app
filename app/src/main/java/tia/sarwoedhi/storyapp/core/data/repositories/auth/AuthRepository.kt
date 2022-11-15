package tia.sarwoedhi.storyapp.core.data.repositories.auth

import kotlinx.coroutines.flow.Flow
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse

interface AuthRepository {
    suspend fun register(request: BodyRegister): Flow<Resource<BaseResponse>>
    suspend fun login(request: BodyLogin): Flow<Resource<LoginResponse>>
    suspend fun saveTokenKey(token: String)
    suspend fun logout()
    suspend fun getTokenKey(): Flow<String>
}
