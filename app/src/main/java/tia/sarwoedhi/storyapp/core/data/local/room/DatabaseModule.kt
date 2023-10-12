package tia.sarwoedhi.storyapp.core.data.local.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "story_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideStoryDao(appDatabase: AppDatabase): StoryDao {
        return appDatabase.storyDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }
}