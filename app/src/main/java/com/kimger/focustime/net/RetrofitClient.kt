package com.kimger.focustime.net

import android.util.Log
import com.google.gson.GsonBuilder
import com.kimger.focustime.BuildConfig
import com.kimger.focustime.isNotNull
import com.kimger.focustime.net.api.UserServiceApi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author Kimger
 * @email kimger@cloocle.com
 * @date 2019/3/28 15:26
 * @description
 */
class RetrofitClient {
    companion object {

        //        var BASE_URL = App.instance().getBaseUrl()
        private const val TAG = "RetrofitClient"
        private var retrofitClient = getRetrofit()
        val userServiceApi: UserServiceApi by lazy {
            retrofitClient.create(UserServiceApi::class.java)
        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://kimger.com")
                .client(okHttpClient())
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().serializeNulls().create()
                    )
                )
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
        }

        private fun okHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
//                .addInterceptor(headerInterceptor())
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(logInterceptor())
            }
            return builder.build()
        }

        private fun logInterceptor(): Interceptor {
            return Interceptor { chain ->
                val request = chain.request()
                val startTime = System.currentTimeMillis()
                val response = chain.proceed(chain.request())
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                val mediaType = response.body()!!.contentType()
                val content = response.body()!!.string()
                Log.d(TAG, "\n")
                Log.d(TAG, "----------Start----------------")
                Log.d(TAG, "| $request")
                val method = request.method()
                if ("POST" == method) {
                    val sb = StringBuilder()
                    if (request.body() is FormBody) {
                        val body = request.body() as FormBody
                        for (i in 0 until body.size()) {
                            sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                        }
                        sb.delete(sb.length - 1, sb.length)
                        Log.d(TAG, "| RequestParams:{$sb}")
                    }
                } else if ("GET" == method) {
                    val url = request.url().url()
                    if (url.query.isNotNull()) {
                        Log.d(TAG, "| RequestParams:{${url.query.replace("&", ",")}}")
                    }
                }
                Log.d(TAG, "| RequestHeader: ${request.headers()}")
                Log.d(TAG, "| Response:$content")
                Log.d(TAG, "----------End:" + duration + "毫秒----------")
                return@Interceptor response.newBuilder()

                    .body(ResponseBody.create(mediaType, content))
                    .build()
            }
        }

//        private fun headerInterceptor(): Interceptor {
//            return Interceptor { chain: Interceptor.Chain ->
//                val request = chain.request()
//                val token: String = UserCache.getToken()
//                if (TextUtils.isEmpty(token)) {
//                    return@Interceptor chain.proceed(request)
//                }
//                val authorised =
//                    request.newBuilder().header("Authorization", "Bearer$token").build()
//                return@Interceptor chain.proceed(authorised)
//            }
//        }


    }
}