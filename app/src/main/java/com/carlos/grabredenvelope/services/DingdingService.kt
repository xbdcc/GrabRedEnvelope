package com.carlos.grabredenvelope.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import com.carlos.cutils.util.AccessibilityServiceUtils
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.activity.MainActivity
import com.carlos.grabredenvelope.db.DingDingRedEnvelopeDb
import com.carlos.grabredenvelope.db.DingDingRedEnvelope
import com.carlos.grabredenvelope.util.ControlUse
import io.sentry.Sentry
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private lateinit var controlUse: ControlUse
    var isStopUse: Boolean = false

    private val DINGDING_PACKAGE = "com.alibaba.android.rimet"

    private val DINGDING_RED_ENVELOPE_ACTIVITY =
        "com.alibaba.android.dingtalk.redpackets.activities.FestivalRedPacketsPickActivity" //钉钉红包弹框

    private val DINGDING_CHAT_ITEM_ID =
        "com.alibaba.android.rimet:id/chatting_content_view_stub" //钉钉聊天页面可点击列表
    private val DINGDING_RED_ENVELOPE_TYPE_ID =
        "com.alibaba.android.rimet:id/tv_redpackets_type" //钉钉聊天页面红包类型
    private val DINGDING_REDENVELOPE_COVER =
        "com.alibaba.android.rimet:id/iv_cover" //钉钉聊天页面红包已领取

    private val DINGDING_PICK_BOTTOM = "com.alibaba.android.rimet:id/iv_pick_bottom" //钉钉拆红包底部弹框
    private val DINGDING_REDENVELOPE_DETAIL_ACTIVITY =
        "com.alibaba.android.dingtalk.redpackets.activities.RedPacketsDetailActivity" //钉钉拆红包底部弹框
    private val DINGDING_REDENVELOPE_MONEY =
        "com.alibaba.android.rimet:id/redpackets_money" //钉钉红包金额

    private var isHasClicked: Boolean = false//true点击红包弹出红包框
    private var isHasOpened: Boolean = false//true点击了拆开红包按钮

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("service oncreate.")
        controlUse = ControlUse(applicationContext)
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

            if (controlUse.stopUse()) return

            if (DINGDING_PACKAGE != event.packageName) return
            if (event.className.toString().startsWith(DINGDING_PACKAGE)) {
//                currentClassName = event.className.toString()
            }

            if (rootInActiveWindow == null)
                return

//            LogUtils.d("data:" + RedEnvelopePreferences.wechatControl)
//            LogUtils.d("" + event.className + "-" + event.eventType)

            when (event.eventType) {
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    LogUtils.d("通知改变" + event.text)
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    LogUtils.d("界面改变" + event.className)
                    openRedEnvelope(event)
                    quitEnvelope(event)
                }
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                    LogUtils.d("内容改变")
                    grabRedEnvelope()
//                    monitorChat()
                }
            }
        } catch (e: Exception) {
            LogUtils.e("error:", e)
            Sentry.capture(e)
        } finally {

        }

    }

    /**
     * 聊天页面监控点击红包
     */
    private fun grabRedEnvelope() {
        LogUtils.d("grabRedEnvelope")

        val envelopes =
            AccessibilityServiceUtils.getElementsById(DINGDING_CHAT_ITEM_ID, rootInActiveWindow)
                ?: return

        /* 发现红包点击进入领取红包页面 */
        for (envelope in envelopes.reversed()) {
            //已领取
            if (AccessibilityServiceUtils.isExistElementById(
                    DINGDING_REDENVELOPE_COVER,
                    envelope
                )
            ) {
                continue
            }
            //不是红包
            if (!AccessibilityServiceUtils.isExistElementById(
                    DINGDING_RED_ENVELOPE_TYPE_ID,
                    envelope
                )
            ) {
                continue
            }
            LogUtils.d("发现红包：$envelope")
            envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            isHasClicked = true
        }

    }


    /**
     * 拆开红包
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun openRedEnvelope(event: AccessibilityEvent) {
        if (event.className != DINGDING_RED_ENVELOPE_ACTIVITY) return
        if (!isHasClicked) return
        val elememnts =
            AccessibilityServiceUtils.getElementsById(DINGDING_PICK_BOTTOM, rootInActiveWindow)
        if (elememnts.isNullOrEmpty()) return
        val rect = Rect()
        elememnts.reversed().first().getBoundsInScreen(rect)
        val path = Path()
        path.moveTo(rect.centerX().toFloat(), rect.centerY().toFloat())
        LogUtils.d("" + rect.centerX().toFloat() + "-" + rect.centerY().toFloat())
        val gestureDescription = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 500, 300)).build()
        dispatchGesture(gestureDescription, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                isHasOpened = true
                isHasClicked = false
                LogUtils.d("onCompleted")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                LogUtils.d("onCancelled")
            }
        }, null)

    }

    /**
     * 退出红包详情页
     */
    private fun quitEnvelope(event: AccessibilityEvent) {
        LogUtils.d("quitEnvelope")
        if (event.className != DINGDING_REDENVELOPE_DETAIL_ACTIVITY) return
        if (!isHasOpened) return
        GlobalScope.launch {
            saveData()
            performGlobalAction(GLOBAL_ACTION_BACK)
        }
        isHasOpened = false
    }


    private fun saveData() {
        val count = AccessibilityServiceUtils.getElementsById(DINGDING_REDENVELOPE_MONEY, rootInActiveWindow)
        count?.get(0)?.let {
            val dingDingRedEnvelope = DingDingRedEnvelope()
            dingDingRedEnvelope.count = it.text.toString()
            DingDingRedEnvelopeDb.insertData(dingDingRedEnvelope)
        }
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