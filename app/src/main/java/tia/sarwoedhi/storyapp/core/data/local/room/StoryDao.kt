package tia.sarwoedhi.storyapp.core.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(story: StoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<StoryEntity>)

    @Query("SELECT * FROM stories")
    fun selectAll(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}