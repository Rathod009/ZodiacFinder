package xyz.aboutdhruv.zodiacfinder.Api
import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor

class BasicAuthInterceptor(username: String, password: String): Interceptor {
    private var credentials: String = Credentials.basic(username, password)
    private val TAG:String = "BASIC_AUTH"
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", credentials).build()
        Log.i(TAG, credentials)
        return chain.proceed(request)
    }
}