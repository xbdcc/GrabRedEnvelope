package com.carlos.grabredenvelope

import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Id

/**
 * Created by Carlos on 2019/2/22.
 */
@Entity
data class WechatRedEnvelopeEntity(
    @Id(autoincrement = true)
    val id: Long,
    var send_info: String,
    var wish_word: String,
    var count: String,
    var time: String
)