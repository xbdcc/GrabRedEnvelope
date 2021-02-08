package com.carlos.grabredenvelope.demo

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.EditText
import com.carlos.cutils.util.LogUtils

/**
 * Created by Carlos on 2021/2/8.
 * 自动发送表情脚本, 基于微信8.0.0
 */
class SendEmojiService : AccessibilityService() {

    private var windowClassName = ""
    private val CHAT_ACTIVITY = "com.tencent.mm.ui.LauncherUI" //微信红包弹框

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("onCreate")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                LogUtils.d("通知改变:$event")
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d("界面改变:$event")
                windowClassName = event.className.toString()
                sendMessage()

            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.d("内容改变:$event")
            }
        }
    }

    /**
     * 找到文本框输入表情，找到发送按钮点击循环执行
     */
    private fun sendMessage() {
        if (windowClassName != CHAT_ACTIVITY) return
        val accessibilityNodeInfo =
            rootInActiveWindow?.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/auj")
                ?: return

        for (editText in accessibilityNodeInfo) {
            if (editText.className == EditText::class.java.name) {
                val arguments = Bundle()
                arguments.putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    "[烟花]"
                )
                editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)

                send()
                break

            }

        }

        sendMessage()

    }

    private fun send() {
        val accessibilityNodeInfos =
            rootInActiveWindow?.findAccessibilityNodeInfosByText("发送") ?: return
        for (accessibilityNodeInfo in accessibilityNodeInfos) {
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
    }


    override fun onInterrupt() {
        LogUtils.d("onInterrupt")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("onDestroy")
    }
}