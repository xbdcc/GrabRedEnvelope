package com.carlos.grabredenvelope.services

import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.EditText

/**
 * Created by Carlos on 2021/2/9.
 */
class EmojiService : BaseAccessibilityService() {

    private var windowClassName = ""
    private val CHAT_ACTIVITY = "com.tencent.mm.ui.LauncherUI" //微信红包弹框
    override fun monitorContentChanged(event: AccessibilityEvent) {
    }

    override fun monitorWindowChanged(event: AccessibilityEvent) {
        windowClassName = event.className.toString()
        sendMessage()
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


}