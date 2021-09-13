package com.kimger.focustime.net.api

/**
 * Created by Administrator on 2017/9/6.
 */

interface NetAddress {
    companion object {

        fun url(url: String) = BASE_URL_TEST + url

        //测试环境
        const val BASE_URL_TEST = "http://192.168.199.245:8080/api/"
//        const val BASE_URL_TEST = "http://192.168.199.75:8080/api/"

        //正式环境
        const val BASE_URL = "http://api.movevi.com/"

        const val WEB_URL = "http://wap.movevi.com"

        const val BASE_LOGIN = "http://192.168.199.245:8081/"

        const val ONE_TIP = "https://v1.hitokoto.cn/"

    }


}
