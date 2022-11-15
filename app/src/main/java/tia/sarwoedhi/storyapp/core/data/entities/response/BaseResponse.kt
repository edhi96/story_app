package tia.sarwoedhi.storyapp.core.data.entities.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(

    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)
