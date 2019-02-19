package com.carlos.grabredenvelope.util

import android.app.Application
import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.carlos.cutils.util.LogUtils
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.commonsdk.utils.UMUtils

/**
 * Created by 小不点 on 2016/2/6.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.appContext = applicationContext
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        UMConfigure.setLogEnabled(true)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        val appKey = UMUtils.getAppkey(this)
        LogUtils.d("appkey:$appKey")

        val appChannel = UMUtils.getChannel(this)
        LogUtils.d("appChannel:$appChannel")

    }

    companion object {
        var appContext: Context? = null
            private set
    }
}