package com.carlos.grabredenvelope.data

import com.carlos.cutils.base.CBasePreferences
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.dao.WechatControlVO
import kotlinx.serialization.json.JSON

/**
 * Created by Carlos on 2019/2/21.
 */
object RedEnvelopePreferences :
    CBasePreferences("redenvelope_preferences", MyApplication.instance.applicationContext) {

    private val USE_STATUS = "use_status"
    private val WECHAT_CONTROL = "wechat_control"

    var useStatus: Boolean
        get() = getBoolean(USE_STATUS, true)
        set(value) = setBoolean(USE_STATUS, value)

    var wechatControl: WechatControlVO
        get() {
            val data = getString(WECHAT_CONTROL, "")
            if (data.isNullOrEmpty()) return WechatControlVO()
            return try {
                JSON.parse(WechatControlVO.serializer(), data)
            }catch (e: Exception) {
                LogUtils.e("error:", e)
                setString(WECHAT_CONTROL, JSON.stringify(WechatControlVO.serializer(), WechatControlVO()))
                WechatControlVO()
            }
        }
        set(value) {
            setString(WECHAT_CONTROL, JSON.stringify(WechatControlVO.serializer(), value))
        }


}


