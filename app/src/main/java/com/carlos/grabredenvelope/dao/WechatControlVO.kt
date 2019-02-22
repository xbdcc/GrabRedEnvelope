package com.carlos.grabredenvelope.dao

import kotlinx.serialization.Serializable

/**
 * Created by Carlos on 2019/2/21.
 */
@Serializable
data class WechatControlVO(
    var isMonitorNotification: Boolean = true, //是否监控通知
    var isMonitorChat: Boolean = true, //是否监控聊天列表页面
    var delayOpenTime: Int = 1,
    var delayCloseTime: Int = 1,
    var wechatId: String = "",
    var imei: String = ""
)