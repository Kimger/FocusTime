package com.kimger.focustime.net

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/28 15:01
 * @description
 */
class LiveDataCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        when (getRawType(observableType)) {
            ApiResponse::class.java -> {
                require(observableType is ParameterizedType) { "resource must be parameterized" }
                val bodyType = getParameterUpperBound(0, observableType)
                return LiveDataCallAdapter<Any>(bodyType)
            }
            else -> throw IllegalArgumentException("返回值需为参数化类型")
        }

    }

}