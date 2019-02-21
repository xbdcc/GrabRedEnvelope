package com.carlos.grabredenvelope.dao

import kotlinx.serialization.Serializable
import org.greenrobot.greendao.annotation.Entity

/**
 * Created by Carlos on 2019/2/21.
 */
@Entity
@Serializable
data class WechatControlVO(
    var isMonitor: Boolean = true, //是否打开开关
    var isMonitorNotification: Boolean = true, //是否监控通知
    var isMonitorChat: Boolean = true, //是否监控聊天列表页面
    var delayOpenTime: Int = 1,
    var delayCloseTime: Int = 1
)