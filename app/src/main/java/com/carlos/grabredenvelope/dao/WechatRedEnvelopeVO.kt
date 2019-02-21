package com.carlos.grabredenvelope.dao

import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Id

/**
 * Created by Carlos on 2019/2/21.
 */
@Entity
data class WechatRedEnvelopeVO(
    @Id
    val id: Int
)