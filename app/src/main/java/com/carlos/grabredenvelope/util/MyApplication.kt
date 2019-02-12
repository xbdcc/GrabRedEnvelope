package com.carlos.grabredenvelope.util

import android.app.Application
import android.content.Context
import cn.jpush.android.api.JPushInterface

/**
 * Created by 小不点 on 2016/2/6.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.appContext = applicationContext
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

//        val registrationId = JPushInterface.getRegistrationID(this)
//        Log.e("1099", "run:--------->registrationId： $registrationId")
//        LogUtil.d("1099"+"run:--------->registrationId： $registrationId")
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}