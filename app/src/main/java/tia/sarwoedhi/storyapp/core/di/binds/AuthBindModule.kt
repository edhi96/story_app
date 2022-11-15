package tia.sarwoedhi.storyapp.core.di.binds

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth.RemoteAuthDataSource
import tia.sarwoedhi.storyapp.core.data.remote.data_sources.auth.RemoteAuthDataSourceImpl
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepository
import tia.sarwoedhi.storyapp.core.data.repositories.auth.AuthRepositoryImpl

@InstallIn(ViewModelComponent::class)
@Module
abstract class AuthBindModule {

    @Binds
    abstract fun bindAuthDataSource(impl: RemoteAuthDataSourceImpl): RemoteAuthDataSource

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}