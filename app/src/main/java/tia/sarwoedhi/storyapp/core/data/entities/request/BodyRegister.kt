package tia.sarwoedhi.storyapp.core.data.entities.request

import com.google.gson.annotations.SerializedName

data class BodyRegister(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)