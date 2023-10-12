package tia.sarwoedhi.storyapp.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import tia.sarwoedhi.storyapp.core.data.entities.RemoteKeysEntity
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity

@Database(
    entities = [
        StoryEntity::class, RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}