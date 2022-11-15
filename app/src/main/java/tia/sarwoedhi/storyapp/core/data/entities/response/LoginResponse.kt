package tia.sarwoedhi.storyapp.core.data.entities.response

import com.google.gson.annotations.SerializedName
import tia.sarwoedhi.storyapp.core.data.entities.LoginResult

data class LoginResponse(
    @SerializedName("loginResult")
    val loginResult: LoginResult? = null,
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
)

