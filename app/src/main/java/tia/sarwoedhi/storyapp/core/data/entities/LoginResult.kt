package tia.sarwoedhi.storyapp.core.data.entities

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("userId")
    val userId: String? = null,

    @SerializedName("token")
    val token: String? = null
)
