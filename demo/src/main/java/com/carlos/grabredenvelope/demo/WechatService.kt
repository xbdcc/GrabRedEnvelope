package com.carlos.grabredenvelope.demo

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.carlos.cutils.util.AccessibilityServiceUtils
import com.carlos.cutils.util.LogUtils

/**
 * Created by Carlos on 2019/2/25.
 * 基于Android版微信7.0.3测试.
 */
class WechatService : AccessibilityService() {

    private val WECHAT_PACKAGE = "com.tencent.mm"
    private val WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框

    private val RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aq7" //聊天页面区分红包id
    private val RED_ENVELOPE_ID = "com.tencent.mm:id/aou" //聊天页面红包点击框控件id
    private val RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf" //抢红包页面点开控件id


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (rootInActiveWindow == null) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                LogUtils.d("通知改变:$event")
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d("界面改变:$event")
                openRedEnvelope(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.d("内容改变:$event")
                clickRedEnvelope()
            }
        }
    }

    private fun clickRedEnvelope() {
        //如果没找到红包就不继续往下执行
        if (!AccessibilityServiceUtils.isExistElementById(
                RED_ENVELOPE_FLAG_ID,
                rootInActiveWindow
            )
        ) return
        //点击红包
        AccessibilityServiceUtils.findAndClickOneById(RED_ENVELOPE_ID, rootInActiveWindow)
    }

    private fun openRedEnvelope(event: AccessibilityEvent) {
        //如果当前页面不是微信红包弹出框则不继续往下执行
        if (WECHAT_LUCKYMONEY_ACTIVITY != event.className) return
        AccessibilityServiceUtils.findAndClickOneById(RED_ENVELOPE_OPEN_ID, rootInActiveWindow)
    }

    override fun onInterrupt() {
    }
}