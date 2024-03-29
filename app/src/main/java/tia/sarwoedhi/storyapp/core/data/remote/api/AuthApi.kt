package tia.sarwoedhi.storyapp.core.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyLogin
import tia.sarwoedhi.storyapp.core.data.entities.request.BodyRegister
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse

interface AuthApi {

    @POST("login")
    suspend fun login(
        @Body requestBody: BodyLogin,
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body requestBody: BodyRegister,
    ): Response<BaseResponse>
}