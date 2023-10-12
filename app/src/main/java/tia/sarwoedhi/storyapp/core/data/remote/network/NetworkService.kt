package tia.sarwoedhi.storyapp.core.data.remote.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tia.sarwoedhi.storyapp.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkService {

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl("https://story-api.dicoding.dev/v1/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(AcceptInterceptor())
            addInterceptor(ContentTypeInterceptor())
            addInterceptor(FormInterceptor())
//            addInterceptor(HttpLoggingInterceptor().apply {
//                level = if (BuildConfig.DEBUG)
//                    HttpLoggingInterceptor.Level.BODY
//                else
//                    HttpLoggingInterceptor.Level.NONE
//            })
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

}