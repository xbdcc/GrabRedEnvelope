package com.carlos.grabredenvelope

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.tencent.bugly.Bugly
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

        initUmeng()

        initBugly()

    }

    private fun initJpush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(context)
    }

    private fun initUmeng() {
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        UMConfigure.init(context, BuildConfig.UMENG_APPKEY_DEV, BuildConfig.VERSION_NAME, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

    private fun initBugly() {
        Bugly.init(context, BuildConfig.BUGLY_KEY_DEV, BuildConfig.DEBUG)
    }

}