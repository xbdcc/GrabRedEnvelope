package com.carlos.grabredenvelope

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.multidex.MultiDex
import com.carlos.cutils.util.LogUtils
import com.tencent.bugly.beta.Beta
import com.tencent.tinker.entry.DefaultApplicationLike

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

/**
 * Created by 小不点 on 2016/2/6.
 */
class MyApplication (
    application: Application, tinkerFlags: Int,
    tinkerLoadVerifyFlag: Boolean, applicationStartElapsedTime: Long,
    applicationStartMillisTime: Long, tinkerResultIntent: Intent
) : DefaultApplicationLike(
    application,
    tinkerFlags,
    tinkerLoadVerifyFlag,
    applicationStartElapsedTime,
    applicationStartMillisTime,
    tinkerResultIntent
) {

    override fun onCreate() {
        super.onCreate()

        instance = this
        applicationContext = application.applicationContext

        LogUtils.isShowLog = BuildConfig.DEBUG

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
//            LogUtils.e("local init error:", e)
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onBaseContextAttached(base: Context?) {
        super.onBaseContextAttached(base)
        MultiDex.install(base)
        Beta.installTinker(this)
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun registerActivityLifecycleCallback(callbacks: Application.ActivityLifecycleCallbacks) {
        application.registerActivityLifecycleCallbacks(callbacks)
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    lateinit var applicationContext : Context

}