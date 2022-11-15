package tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.core.data.remote.api.AuthApi
import javax.inject.Inject

class RemoteAuthDataSourceImpl @Inject constructor(private val authApi: AuthApi) : RemoteAuthDataSource {
    override suspend fun login(requestBody: BodyLogin): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = authApi.login(requestBody)
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(response.message()))
            }
        }catch (e: Exception) {
            emit(Resource.Error((e.message ?: "").toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun register(requestBody: BodyRegister): Flow<Resource<BaseResponse>> = flow {
        emit(Resource.Loading)
        try {
            val response = authApi.register(requestBody)
            if (response.isSuccessful) {
                emit(Resource.Success(null))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: Exception) {
            emit(Resource.Error((e.message ?: "").toString()))
        }
    }.flowOn(Dispatchers.IO)



}