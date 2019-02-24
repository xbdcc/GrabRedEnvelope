package com.carlos.grabredenvelope

import android.content.Context
import cn.bmob.v3.Bmob
import cn.jpush.android.api.JPushInterface
import com.carlos.grabredenvelope.local.LocalConstants
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * Created by Carlos on 2019/2/20.
 */
class AppInit {

    private var context: Context = MyApplication.instance.applicationContext

    init {

        initJpush()

        initUment()

        initBugly()

        initBmob()
    }

    private fun initJpush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(context)
    }

    private fun initUment() {

        UMConfigure.setLogEnabled(true)
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }
    private fun initBugly() {
        CrashReport.initCrashReport(context, LocalConstants.BUGLY_DEV_KEY, false)//DEV环境
    }

    private fun initBmob() {
        Bmob.initialize(context, LocalConstants.BMOB_DEV_KEY)
    }
}