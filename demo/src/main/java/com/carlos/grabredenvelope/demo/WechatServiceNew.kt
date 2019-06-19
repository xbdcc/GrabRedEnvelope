package com.carlos.grabredenvelope.demo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.cutils.util.AccessibilityServiceUtils
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_FLAG_ID
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_ID
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_OPEN_ID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Carlos on 2019/2/25.
 * 基于Android版微信7.0.3测试.
 * 一加5T尺寸1080*2160，dpi420，点击拆红包（540，1330）
 *
 * 走界面改变没走内容改变
 */
class WechatServiceNew : AccessibilityService() {

    private val WECHAT_PACKAGE = "com.tencent.mm"
    private val WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框

    private var isHasReceived: Boolean = false//true已经通知或聊天列表页面收到红包
    private var isHasClicked: Boolean = false//true点击了聊天页面红包


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return
        if (rootInActiveWindow == null) return

        LogUtils.d("envent:" + event)
        LogUtils.d("envent:" + event.eventType)

        openRedEnvelope(event)

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

                openRedEnvelopeNew(event)

            }
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                LogUtils.d("TYPE_WINDOW_STATE_CHANGED:$event")
//                openRedEnvelope(event)
//            }
        }

    }

    private fun clickRedEnvelope() {
        if(rootInActiveWindow == null) return
        //如果没找到红包就不继续往下执行
        if (!AccessibilityServiceUtils.isExistElementById(RED_ENVELOPE_FLAG_ID, rootInActiveWindow)) return
        //点击红包
        AccessibilityServiceUtils.findAndClickOneById(RED_ENVELOPE_ID, rootInActiveWindow)
        isHasClicked = false
    }

    private fun openRedEnvelope(event: AccessibilityEvent) {
        //如果当前页面不是微信红包弹出框则不继续往下执行
        if (WECHAT_LUCKYMONEY_ACTIVITY != event.className) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return
        if(rootInActiveWindow == null) return
        if (isHasClicked)
            AccessibilityServiceUtils.findAndClickOneById(RED_ENVELOPE_OPEN_ID, rootInActiveWindow)

        isHasClicked = true

    }

    private fun openRedEnvelopeNew(event: AccessibilityEvent) {
        LogUtils.d("isHasClicked:" + isHasClicked)
        LogUtils.d("Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT)
        if (isHasClicked) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        GlobalScope.launch {
            val delayTime = 8000L
            delay(delayTime)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LogUtils.d("sdk:" + Build.VERSION.SDK_INT)
                val metrics = resources.displayMetrics
                val dpi = metrics.densityDpi
                val path = Path()
                LogUtils.d("dpi:" + dpi)

                LogUtils.d("envent:" + event)
                if (640 == dpi) { //1440
                    path.moveTo(720f, 1575f)
                } else if (320 == dpi) {//720p
                    path.moveTo(360f, 780f)
                } else if (480 == dpi) {//1080p
                    path.moveTo(540f, 1309f) //小米mix5
                } else if (440 == dpi) {//1080*2160
                    path.moveTo(450f, 1250f)
                } else if (420 == dpi) {//420一加5T
                    path.moveTo(540f, 1330f)
                }
                val build = GestureDescription.Builder()
                val gestureDescription =
                    build.addStroke(GestureDescription.StrokeDescription(path, 500, 100)).build()

                dispatchGesture(gestureDescription, object : GestureResultCallback() {

                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        LogUtils.d("onCompleted")
                    }

                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        LogUtils.d("onCancelled")
                    }

                }, null)
            }

        }
        isHasClicked = true
    }

    override fun onInterrupt() {
        LogUtils.e("onInterrupt")
    }

    fun getNodes(accessibilityNodeInfo: AccessibilityNodeInfo) {
        for (index in 0 until accessibilityNodeInfo.childCount) {
            val nodeInfo = accessibilityNodeInfo.getChild(index)
            LogUtils.d("nodeinfo:$nodeInfo")
            if (nodeInfo != null)
                getNodes(nodeInfo)
        }
    }

    private fun openRedEnvelope() {

    }

}