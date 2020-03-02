package com.carlos.grabredenvelope.db

import com.carlos.grabredenvelope.data.GreenDaoManager

/**
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2020/3/2.
 */
object DingDingRedEnvelopeDb {

    private val dingDingRedEnvelopeDao = GreenDaoManager.instance.session.dingDingRedEnvelopeDao

    val allData: List<DingDingRedEnvelope>
        @Synchronized get() = dingDingRedEnvelopeDao.loadAll()

    @Synchronized
    fun insertData(dingDingRedEnvelope: DingDingRedEnvelope) {
        dingDingRedEnvelopeDao.insert(dingDingRedEnvelope)
    }
}