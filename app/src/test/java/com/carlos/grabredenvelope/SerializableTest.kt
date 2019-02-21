package com.carlos.grabredenvelope

import com.carlos.grabredenvelope.dao.WechatControlVO
import kotlinx.serialization.json.JSON
import org.junit.Test

/**
 * Created by Carlos on 2019/2/21.
 */
class SerializableTest {

    @Test
    fun testSerializable() {

        val wechatControlVO = WechatControlVO()

        val json = JSON.stringify(WechatControlVO.serializer(), wechatControlVO)
        println("json:$json")


        val data = JSON.parse(WechatControlVO.serializer(), json)
        println("isMonitor:${data.isMonitor}")


        var json2 = JSON.parse(WechatControlVO.serializer(), "")
        println("json2:$json2")

    }

}