package tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth

import kotlinx.coroutines.flow.Flow
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse

interface RemoteAuthDataSource {
    suspend fun login(requestBody: BodyLogin): Flow<Resource<LoginResponse>>
    suspend fun register(requestBody: BodyRegister): Flow<Resource<BaseResponse>>
}