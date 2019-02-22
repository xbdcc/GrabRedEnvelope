package com.carlos.grabredenvelope

import android.app.Application
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