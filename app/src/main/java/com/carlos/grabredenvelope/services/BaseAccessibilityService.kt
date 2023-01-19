package com.carlos.grabredenvelope.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.accessibility.AccessibilityEvent
import com.carlos.cutils.base.CBaseAccessibilityService
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.activity.MainActivity
import com.carlos.grabredenvelope.util.ControlUse
import io.sentry.Sentry

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
 * Created by Carlos on 2020/3/4.
 */
abstract class BaseAccessibilityService : CBaseAccessibilityService() {

    private lateinit var controlUse: ControlUse
    open var notificationTitle: String = ""

    /* 状态切换，流程更直观，避免人为点击的误操作，等待红包——点击红包关键字——点击红包——拆红包——等待红包循环 */
    var status: Int = WAIT_NEW
    companion object {
        const val WAIT_NEW = 0 //等待新的红包
        const val HAS_RECEIVED = 1 //通知或聊天列表页面收到红包
        const val HAS_CLICKED = 2 //已点击红包弹出了红包框
        const val HAS_OPENED = 3 //点击了拆开红包按钮
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("Service onCreate.")
        controlUse = ControlUse(applicationContext)
        if (controlUse.stopUse()) isMonitor = false
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("Service onDestroy.")
    }

    override fun onInterrupt() {
        LogUtils.e("Service onInterrupt.")
        Sentry.captureMessage("${this.javaClass.name} onInterrupt")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        var flags = flags
        LogUtils.d("service onstartcommand.")
        val builder = Notification.Builder(MyApplication.instance.applicationContext)
        val notificationIntent = Intent(this, MainActivity::class.java)

        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.mipmap.xiaobudian
                )
            ) // set the large icon in the drop down list.
            .setContentTitle(getString(R.string.app_name)) // set the caption in the drop down list.
            .setSmallIcon(R.mipmap.xiaobudian) // set the small icon in state.
            .setContentText(getString(R.string.app_content)) // set context content.
            .setWhen(System.currentTimeMillis()) // set the time for the notification to occur.

        val notification = builder.build()
        notification.defaults = Notification.DEFAULT_SOUND// set default sound.

        startForeground(110, notification)
        flags = Service.START_FLAG_REDELIVERY
        return super.onStartCommand(intent, flags, startId)
    }


    override fun monitorNotificationChanged(event: AccessibilityEvent) {
        val text = event.text.toString()
        if (status!= WAIT_NEW) return
        if (text.isEmpty() or notificationTitle.isNullOrEmpty()) {
            return
        }
        if (text.contains(notificationTitle).not()) return
        LogUtils.d("Notification contains redenvelope.")
        try {
            val notification = event.parcelableData as Notification
            val pendingIntent = notification.contentIntent
            pendingIntent.send()
            status = HAS_RECEIVED
            LogUtils.d("click redenvelope notification.")
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }
    }

}