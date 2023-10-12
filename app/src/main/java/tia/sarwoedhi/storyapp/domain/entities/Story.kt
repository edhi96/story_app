package tia.sarwoedhi.storyapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id:String? = "",
    val title: String? = "null",
    val description: String? = "",
    val image: String? = "",
    val date: String? = "",
) : Parcelable