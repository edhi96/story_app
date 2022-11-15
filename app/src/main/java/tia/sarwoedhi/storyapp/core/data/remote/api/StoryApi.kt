package tia.sarwoedhi.storyapp.core.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.StoriesResponse

interface StoryApi {

    @GET("stories")
   suspend fun getListStory(@Header("Authorization") auth:String): Response<StoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") auth:String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<BaseResponse>
}