package tia.sarwoedhi.storyapp.core.data.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class FormInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    private fun addHeader(oriRequest: Request): Request {
        return oriRequest.newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded").build()
    }
}