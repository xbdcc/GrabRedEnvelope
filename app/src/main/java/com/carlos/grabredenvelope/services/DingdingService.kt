package com.carlos.grabredenvelope.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.activity.MainActivity
import com.carlos.grabredenvelope.data.RedEnvelopePreferences
import com.carlos.grabredenvelope.old2016.PreferencesUtils
import com.carlos.grabredenvelope.util.ControlUse

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
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2019/2/14.
 */
class DingdingService : AccessibilityService() {

    var isStopUse: Boolean = false

    private lateinit var nodeRoot: AccessibilityNodeInfo

    private val DINGDING_PACKAGE = "com.alibaba.android.rimet"

    private val DINGDING_RED_ENVELOPE_ACTIVITY =
        "com.alibaba.android.dingtalk.redpackets.activities.FestivalRedPacketsPickActivity"

    private val DINGDING_CHAT_ITEM_ID =
        "com.alibaba.android.rimet:id/chatting_content_view_stub" //钉钉聊天页面可点击列表
    private val DINGDING_RED_ENVELOPE_TYPE_ID =
        "com.alibaba.android.rimet:id/tv_redpackets_type" //钉钉聊天页面红包类型

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("service oncreate.")
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
                    R.mipmap.ic_launcher
                )
            ) // set the large icon in the drop down list.
            .setContentTitle("RedEnvelope") // set the caption in the drop down list.
            .setSmallIcon(R.mipmap.ic_launcher) // set the small icon in state.
            .setContentText("RedEnvelope") // set context content.
            .setWhen(System.currentTimeMillis()) // set the time for the notification to occur.

        val notification = builder.build()
        notification.defaults = Notification.DEFAULT_SOUND// set default sound.

        startForeground(110, notification)
        flags = Service.START_FLAG_REDELIVERY
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val accessibilityServiceInfo = AccessibilityServiceInfo()
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        accessibilityServiceInfo.packageNames = arrayOf(DINGDING_PACKAGE)
        accessibilityServiceInfo.notificationTimeout = 100
        accessibilityServiceInfo.flags =
            serviceInfo.flags or AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
        serviceInfo = accessibilityServiceInfo
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("service ondestroy.")
    }

    override fun onInterrupt() {
        LogUtils.e("出错")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        try {
            val controlUse = ControlUse(applicationContext)
            if (controlUse.stopUse()) {
                LogUtils.d("time---停止使用")
                isStopUse = true
                //            return;
            }
            LogUtils.d("state:" + PreferencesUtils.usestatus)
            if (!PreferencesUtils.usestatus) {
                LogUtils.d("use---停止使用")
                isStopUse = true
                //            return;
            }

            if (isStopUse) return

            if (rootInActiveWindow == null)
                return
            nodeRoot = rootInActiveWindow


            LogUtils.d("data:" + RedEnvelopePreferences.wechatControl)

            if (DINGDING_PACKAGE != event.packageName) return
            LogUtils.d("" + event.className + "-" + event.eventType)

            when (event.eventType) {
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    LogUtils.d("通知改变" + event.text)
//                    monitorNotification(event)
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    LogUtils.d("界面改变")
                    openRedEnvelope(event)
//                    quitEnvelope(event)
                }
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                    LogUtils.d("内容改变")
                    grabRedEnvelope()
//                    monitorChat()
                }
            }
        } catch (e: Exception) {
            LogUtils.e("error:", e)
        } finally {

        }

    }

    /**
     * 聊天页面监控点击红包
     */
    private fun grabRedEnvelope() {
        LogUtils.d("grabRedEnvelope")
        val envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(DINGDING_CHAT_ITEM_ID)
        if (envelopes.isEmpty()) return

        /* 发现红包点击进入领取红包页面 */
        for (envelope in envelopes.reversed()) {
            if (envelope.findAccessibilityNodeInfosByViewId(DINGDING_RED_ENVELOPE_TYPE_ID).isNotEmpty()) {
                LogUtils.d("发现红包：$envelope")
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                break
            }
        }
//        isHasReceived = false
    }


    /**
     * 拆开红包
     */
    private fun openRedEnvelope(event: AccessibilityEvent) {
        if (event.className != DINGDING_RED_ENVELOPE_ACTIVITY) return

        getNodes(nodeRoot)

        findWebViewNode(nodeRoot)

    }

    /**
     * 递归遍历出WebView节点
     */
    private var accessibilityNodeInfoWebView: AccessibilityNodeInfo? = null

    private fun findWebViewNode(rootNode: AccessibilityNodeInfo) {
        for (i in 0 until rootNode.childCount) {
            val child = rootNode.getChild(i)
            if (child == null) continue
            if ("android.webkit.WebView" == child.className) {
                accessibilityNodeInfoWebView = child
                LogUtils.d("findWebViewNode--找到webView" + accessibilityNodeInfoWebView)
                return
            }
            if (child.childCount > 0) {
                findWebViewNode(child)
            }
        }
    }


    fun getNodes(accessibilityNodeInfo: AccessibilityNodeInfo) {
        for (index in 0 until accessibilityNodeInfo.childCount) {
            val nodeInfo = accessibilityNodeInfo.getChild(index)
            LogUtils.d("nodeinfo:$nodeInfo")
            if (nodeInfo != null)
                getNodes(nodeInfo)
        }
    }

}