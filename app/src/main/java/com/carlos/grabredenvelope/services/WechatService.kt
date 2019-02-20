package com.carlos.grabredenvelope.services

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.util.WakeupTools

/**
 * Test in 7.0.3
 * Created by Carlos on 2019/2/14.
 */
class WechatService(
    private val context: Context,
    private val event: AccessibilityEvent,
    private val nodeRoot: AccessibilityNodeInfo
) {

    private val WECHAT_PACKAGE = "com.tencent.mm"
    private val WECHAT_LAUNCHERUI_ACTIVITY = "$WECHAT_PACKAGE.ui.LauncherUI" //聊天页面
    private val WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
    private val WECHAT_LUCKYMONEYDETAILUI_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyDetailUI" //微信红包详情页


    private val RED_ENVELOPE_ID = "com.tencent.mm:id/aou" //聊天页面红包点击框控件id
    private val RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aq6" //聊天页面检测红包已被领控件id
    private val RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf" //抢红包页面点开控件id
    private val RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cv0" //抢红包页面退出控件id

    private val RED_ENVELOPE_DETAIL_CLOSE_ID = "com.tencent.mm:id/ka" //红包详情页面退出控件id
    private val RED_ENVELOPE_TITLE = "[微信红包]" //红包文字
    private val RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b5q" //红包id
    private val RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b5m" //红包RECT id


    private var isHasReceived: Boolean = false//true已经通知或聊天列表页面收到红包
    private var isHasClicked: Boolean = false//true点击了聊天页面红包
    private var isHasInput: Boolean = false//true输入了红包口令
    private var isHasOpened: Boolean = false//true发送红包口令
    private var isHasReceivedList: Boolean = false//从聊天页面收到后点击红包

    init {
        onAccessibilityEvent()
    }

    fun onAccessibilityEvent() {
        if (WECHAT_PACKAGE != event.packageName) return
        LogUtils.d(""+event.className +"-"+ event.eventType)
        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                LogUtils.d("通知改变" + event.text)
                monitorNotification()
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d("界面改变")
                openRedEnvelope()
                quitEnvelope()
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.d("内容改变")
                grabRedEnvelope()
                monitorChat()
            }
        }
    }

    /**
     * 监控通知是否有红包
     */
    private fun monitorNotification() {
        val texts = event.text
        LogUtils.d("检测到微信通知，文本为------------>$texts")
        if (texts.isEmpty())
            return
        if (texts.toString().contains(RED_ENVELOPE_TITLE)) {
            WakeupTools.wakeUpAndUnlock(context)
            isHasReceived = true
            //以下是精华，将QQ的通知栏消息打开
            val notification = event.parcelableData as Notification
            val pendingIntent = notification.contentIntent
            try {
                LogUtils.d("准备打开通知栏")
                pendingIntent.send()
            } catch (e: PendingIntent.CanceledException) {
                LogUtils.e("error:$e")
            }
        }

    }

    /**
     * 监控微信聊天列表页面是否有红包
     */
    private fun monitorChat() {
        LogUtils.d("monitorChat")
        val lists = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_RECT_TITLE_ID)
        for (envelope in lists) {
            val redEnvelope = envelope.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_TITLE_ID)
            if (redEnvelope.isNotEmpty()) {
                if (redEnvelope[0].text.contains(RED_ENVELOPE_TITLE)) {
                    envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    isHasReceived = true
                }
            }
        }

    }

    /**
     * 聊天页面监控点击红包
     */
    private fun grabRedEnvelope() {
        LogUtils.d("grabRedEnvelope")
        val envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_ID)
        if (envelopes.size < 1) return
        if (isHasClicked) return

        /* 发现红包点击进入领取红包页面 */
        for (envelope in envelopes.reversed()) {
            if (envelope.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_BEEN_GRAB_ID).size < 1) {
                LogUtils.d("发现红包：$envelope")
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                isHasClicked = true
//                break
            }
        }
    }

    /**
     * 拆开红包
     */
    private fun openRedEnvelope() {
        if (event.className != WECHAT_LUCKYMONEY_ACTIVITY) return

        var envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_OPEN_ID)
        if (envelopes.size < 1) {
            envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_CLOSE_ID)
            /* 进入红包页面点击退出按钮 */
            for (envelope in envelopes.reversed()) {
                if (isHasClicked)
                    envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else {
            /* 进入红包页面点击开按钮 */
            for (envelope in envelopes.reversed()) {
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
        isHasReceived = false
        isHasClicked = false
    }

    /**
     * 退出红包详情页
     */
    private fun quitEnvelope() {
        if (event.className != WECHAT_LUCKYMONEYDETAILUI_ACTIVITY) return
        val envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_DETAIL_CLOSE_ID)
        if (envelopes.size < 1) return

        /* 发现红包点击进入领取红包页面 */
        for (envelope in envelopes.reversed()) {
            envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }

}