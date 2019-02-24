package com.carlos.grabredenvelope

import android.app.Application
import cn.bmob.v3.Bmob
import com.carlos.cutils.util.LogUtils

/**
 * Created by 小不点 on 2016/2/6.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppInit()

        initLocal()

//        Bugtags.start("251033fc150b1a586e738abd782988e6", this, Bugtags.BTGInvocationEventBubble);

        //第一：默认初始化
        Bmob.initialize(this, "2b7a153910938a44794d3b03cc81da24");
    }

    private fun initLocal() {
        try {
            val myInitClass = Class.forName("com.carlos.grabredenvelope.local.LocalInit")
            val initBuglyMethod = myInitClass.getMethod("init")
            val myInitObject = myInitClass.newInstance()
            initBuglyMethod.invoke(myInitObject)
        } catch (e: Exception) {
            LogUtils.e("local init error:", e)
        }
    }


    companion object {
        lateinit var instance: MyApplication
            private set
    }
}