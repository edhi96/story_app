package tia.sarwoedhi.storyapp.core.data.repositories.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import tia.sarwoedhi.storyapp.core.data.Resource
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import tia.sarwoedhi.storyapp.core.data.local.data_store_local.DataStoreInterface
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth.RemoteAuthDataSource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteAuthDataSource: RemoteAuthDataSource,
    private val dataStoreInterface: DataStoreInterface
) :
    AuthRepository {
    override suspend fun register(request: BodyRegister): Flow<Resource<BaseResponse>> {
        return remoteAuthDataSource.register(request)
    }

    override suspend fun login(request: BodyLogin): Flow<Resource<LoginResponse>> = flow {
        remoteAuthDataSource.login(request).onStart {
            emit(Resource.Loading)
        }.collect { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.loginResult?.token?.let {
                        dataStoreInterface.saveTokenKey(it)
                    }

                    dataStoreInterface.getTokenKey().onStart {
                    }
                    emit(Resource.Success(response.data))
                }
                is Resource.Error -> {
                    emit(Resource.Error(error = response.error))
                }
                else -> {

                }
            }
        }
    }

    override suspend fun saveTokenKey(token: String) {
        dataStoreInterface.saveTokenKey(token)
    }

    override suspend fun logout() {
        return dataStoreInterface.deleteTokenKey()
    }

    override suspend fun getTokenKey(): Flow<String> {
        return dataStoreInterface.getTokenKey()
    }
}