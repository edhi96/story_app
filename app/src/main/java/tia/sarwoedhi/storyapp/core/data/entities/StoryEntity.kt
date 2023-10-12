package tia.sarwoedhi.storyapp.core.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "stories", indices = [Index(value = ["id"], unique = true)])
data class StoryEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("photoUrl")
    val photoUrl: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
): Parcelable