package com.carlos.grabredenvelope.util

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.AppInit
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * Created by 小不点 on 2016/2/6.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        AppInit()

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        initLocal()

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