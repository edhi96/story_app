package tia.sarwoedhi.storyapp.core.di.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import tia.sarwoedhi.storyapp.core.data.remote.api.StoryApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoryApiModule {

    @Provides
    @Singleton
    fun provideStoryApi(retrofit: Retrofit): StoryApi {
        return retrofit.create(StoryApi::class.java)
    }



}