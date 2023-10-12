package tia.sarwoedhi.storyapp.core.data.entities.response

import com.google.gson.annotations.SerializedName
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("listStory") var listStory: MutableList<StoryEntity> = mutableListOf()
)