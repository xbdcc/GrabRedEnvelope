package com.carlos.grabredenvelope.services

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.cutils.util.LogUtils

/**
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


    init {
        onAccessibilityEvent()
    }

    fun onAccessibilityEvent() {
        LogUtils.d("event:$event")
        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                LogUtils.d("通知改变" + event.text)
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d("界面改变")
                openRedEnvelope()
                quitEnvelope()
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.d("内容改变")
                grabRedEnvelope()

            }
        }
    }

    private fun grabRedEnvelope() {
        val envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_ID)
        if (envelopes.size < 1) return

        /* 发现红包点击进入领取红包页面 */
        for (envelope in envelopes.reversed()) {
            if (envelope.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_BEEN_GRAB_ID).size < 1) {
                LogUtils.d("发现红包：$envelope")
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                break
            }
        }
    }

    private fun openRedEnvelope() {
        if (event.className != WECHAT_LUCKYMONEY_ACTIVITY) return
        var envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_OPEN_ID)
        if (envelopes.size < 1) {
            envelopes = nodeRoot.findAccessibilityNodeInfosByViewId(RED_ENVELOPE_CLOSE_ID)
            /* 进入红包页面点击退出按钮 */
            for (envelope in envelopes.reversed()) {
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        } else {
            /* 进入红包页面点击开按钮 */
            for (envelope in envelopes.reversed()) {
                envelope.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }

    }

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