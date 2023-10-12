package tia.sarwoedhi.storyapp.utils

import android.content.Context
import android.location.Geocoder
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.util.*

object Utils {

    @Suppress("DEPRECATION")
    fun getAddressName(context: Context, lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    fun Response<out Any>.getErrorMessage(): String {
        val errorBody = JSONObject(this.errorBody()?.charStream()?.readText() ?: "")
        return errorBody.get("message") as String
    }
}