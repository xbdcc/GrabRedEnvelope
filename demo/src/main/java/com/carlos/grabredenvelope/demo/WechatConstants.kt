package com.carlos.grabredenvelope.demo

import com.carlos.cutils.util.LogUtils

/**
 * Created by Carlos on 2019-05-29.
 */
object WechatConstants {

    var RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aql" //聊天页面区分红包id
    var RED_ENVELOPE_ID = "com.tencent.mm:id/ap9" //聊天页面红包点击框控件id
    var RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d02" //抢红包页面点开控件id

    fun setVersion(version: String) {
        LogUtils.d("version:$version")
        when(version) {
            "7.0.3" -> {
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aq7"
                RED_ENVELOPE_ID = "com.tencent.mm:id/aou"
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf"
            }
            "7.0.4" -> {
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aql"
                RED_ENVELOPE_ID = "com.tencent.mm:id/ap9"
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d02"
            }
        }
    }

}