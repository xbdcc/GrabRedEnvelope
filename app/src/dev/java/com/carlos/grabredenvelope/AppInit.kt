package com.carlos.grabredenvelope

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

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
 * Created by Carlos on 2019/2/20.
 */
class AppInit {

    private var context: Context = MyApplication.instance.applicationContext

    init {

        initJpush()

        initUment()

        initBugly()

    }

    private fun initJpush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(context)
    }

    private fun initUment() {

        // 打开统计SDK调试模式
        UMConfigure.setLogEnabled(true)
//        /**
//         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
//         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
//         * UMConfigure.init调用中appkey和channel参数请置为null）。
//         */
        UMConfigure.init(context, "5c6a2a41b465f547ff00034a", "test", UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)


//        UMConfigure.setLogEnabled(true)
//        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null)
//        // 选用AUTO页面采集模式
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

    private fun initBugly() {
        CrashReport.initCrashReport(context, BuildConfig.BUGLY_KEY_DEV, false)//DEV环境
    }

}