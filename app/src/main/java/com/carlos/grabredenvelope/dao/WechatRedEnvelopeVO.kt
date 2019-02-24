package com.carlos.grabredenvelope.dao

import cn.bmob.v3.BmobObject

/**
 * Created by Carlos on 2019/2/21.
 */
data class WechatRedEnvelopeVO(
    var send_info: String,
    var wish_word: String,
    var count: String,
    var time: String,
    var wechatId: String,
    var imei: String
) : BmobObject()

data class WechatIdVO(
    var wechatId: String
) : BmobObject()