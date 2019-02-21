package com.carlos.grabredenvelope

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test

/**
 * Created by Carlos on 2019/2/21.
 */
class CoroutinesTest {

    @Test
    fun testCoroutines() {
        println("Start")

        GlobalScope.launch {
            println(System.currentTimeMillis())
            delay(3000)
            println(System.currentTimeMillis())
        }

        Thread.sleep(2000) // 等待 2 秒钟
        println("Stop")
    }

}