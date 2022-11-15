package tia.sarwoedhi.storyapp.core.data.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AcceptInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    fun addHeader(oriRequest: Request): Request {
        return oriRequest.newBuilder().addHeader("Accept", "application/json").build()
    }

}