package com.kimger.focustime.net.api

import androidx.lifecycle.LiveData
import com.kimger.focustime.OneTipBean
import com.kimger.focustime.net.ApiResponse
import retrofit2.http.GET

/**
 * @author Kimger
 * @email kimgerxue@gmail.com
 * @date 2019/3/28 15:26
 * @description
 */
interface UserServiceApi {

    @GET(NetAddress.ONE_TIP)
    fun getOneTip(): LiveData<ApiResponse<OneTipBean>>

}