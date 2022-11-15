package tia.sarwoedhi.storyapp.core.di.binds

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSource
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.story.StoryRemoteDataSourceImpl
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepository
import tia.sarwoedhi.storyapp.core.data.repositories.story.StoryRepositoryImpl

@InstallIn(ViewModelComponent::class)
@Module
abstract class StoryBindModule {

    @Binds
    abstract fun bindStoryRemoteDataSource(impl: StoryRemoteDataSourceImpl): StoryRemoteDataSource

    @Binds
    abstract fun bindStoryRepositoryImpl(impl: StoryRepositoryImpl): StoryRepository

}