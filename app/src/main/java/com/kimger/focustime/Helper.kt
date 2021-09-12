package com.kimger.focustime

import kotlin.random.Random

/**
 * Created with Android Studio.
 * @author Kimger
 * @date 2021-09-10 15:11
 * @email kimger@cloocle.com
 * @description
 */
class Helper {

    companion object {

        @JvmStatic
        fun get(): Helper {
            return Helper()
        }

        @JvmStatic
        public fun isNotNull(any: Any?) = !isNull(any)

        @JvmStatic
        public fun isNull(any: Any?) = any == null


    }

    fun time2ms(longS: Long): String {
        val sb = StringBuffer()
        return if (longS > 0) {
            val time = longS.toLong()
            val m = time / 60
            if (m < 10) {
                sb.append("0").append(m.toString()).append(":")
            } else {
                sb.append(m.toString()).append(":")
            }
            val s = time % 60
            if (s < 10) {
                sb.append("0").append(s.toString())
            } else {
                sb.append(s.toString())
            }
            sb.toString()
        } else {
            "00:00"
        }
    }

    fun time2m(longS: Long): String {
        val sb = StringBuffer()
        return if (longS > 0) {
            val time = longS.toLong()
            val m = time / 60
            sb.append(m.toString())
            sb.toString()
        } else {
            "0"
        }
    }

    private val cardBgList = arrayOf(
        R.drawable.shape_card_bg_1,
        R.drawable.shape_card_bg_2,
        R.drawable.shape_card_bg_3,
        R.drawable.shape_card_bg_4,
        R.drawable.shape_card_bg_5,
        R.drawable.shape_card_bg_6,
        R.drawable.shape_card_bg_7,
    )

    fun getRandomBackground(): Int {
        val randomInt = Random.nextInt(0, 6)
        return cardBgList[randomInt]
    }
}