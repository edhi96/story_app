package tia.sarwoedhi.storyapp

import tia.sarwoedhi.storyapp.core.data.entities.LoginResult
import tia.sarwoedhi.storyapp.core.data.entities.StoryEntity
import tia.sarwoedhi.storyapp.core.data.entities.response.BaseResponse
import tia.sarwoedhi.storyapp.core.data.entities.response.LoginResponse
import java.util.*

object DataDummy {
    fun generateDummyStoriesEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val story = StoryEntity(
                id = "ID ${random()}",
                name = "Name ${random()}",
                description = "Description ${random()}",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "2022-11-16T22:22:22Z",
                lat = Random().nextDouble(),
                lon = Random().nextDouble()
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyStory(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val story = StoryEntity(
                id = "ID ${random()}",
                name = "Name ${random()}",
                description = "Description ${random()}",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                createdAt = "2022-11-16T22:22:22Z",
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyBaseResponseSuccess() = BaseResponse(
        error = false,
        message = "Success"
    )

    fun generateDummyLogin() = LoginResponse(
        error = false,
        message = "Success",
        loginResult = LoginResult(
            userId = "User ID ${random()}",
            name = "Name ${random()}",
            token = "Token-${random()}-Secure"
        )
    )

    private fun random() = (0..99999).random().toString()
}