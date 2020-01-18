package com.carlos.grabredenvelope.demo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.carlos.cutils.util.AccessibilityServiceUtils
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_FLAG_ID
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_ID
import com.carlos.grabredenvelope.demo.WechatConstants.RED_ENVELOPE_OPEN_ID

/**
 * Created by Carlos on 2019/2/25.
 * Adapt Wechat 7.0.3,7.0.4,7.0.5.
 */
class WechatService : AccessibilityService() {

    private val WECHAT_PACKAGE = "com.tencent.mm"
    private val WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
    private val WECHAT_LAUNCHER_UI = "com.tencent.mm.ui.LauncherUI"
    private var currentClassName = WECHAT_LAUNCHER_UI

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.className.toString().startsWith("com.tencent.mm")) {
            currentClassName = event.className.toString()
        }
        WechatConstants.setVersion(getAppVersionName(baseContext, WECHAT_PACKAGE))

        when (event.eventType) {
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                LogUtils.d("通知改变:$event")
            }
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                LogUtils.d("界面改变:$event")
                openRedEnvelope(event)
                openRedEnvelopeNew(event)
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                LogUtils.d("内容改变:$event")
                if (rootInActiveWindow == null) return
                clickRedEnvelope()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun clickRedEnvelope() {

        windows.forEach {
            it.root
        }
        //如果没找到红包就不继续往下执行
        if (!AccessibilityServiceUtils.isExistElementById(
                RED_ENVELOPE_FLAG_ID,
                rootInActiveWindow
            )
        ) return
        //点击红包
        AccessibilityServiceUtils.findAndClickFirstOneById(RED_ENVELOPE_ID, rootInActiveWindow)
    }


    private fun openRedEnvelope(event: AccessibilityEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) return
        //如果当前页面不是微信红包弹出框则不继续往下执行
        if (WECHAT_LUCKYMONEY_ACTIVITY != currentClassName) return
        AccessibilityServiceUtils.findAndClickFirstOneById(RED_ENVELOPE_OPEN_ID, rootInActiveWindow)
    }

    override fun onInterrupt() {
    }


    fun getAppVersionName(context: Context, packageName: String = context.packageName) = try {
        context.packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }

    private fun openRedEnvelopeNew(event: AccessibilityEvent) {

        if (WECHAT_LUCKYMONEY_ACTIVITY != currentClassName) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val metrics = resources.displayMetrics
            val dpi = metrics.densityDpi
            val path = Path()

            when (dpi) {
                640 -> //1440
                    path.moveTo(720f, 1575f)
                320 -> //720p
                    path.moveTo(360f, 780f)
                480 -> //1080p
                    path.moveTo(540f, 1465f) //oppo r15,android 9, 小米8 android 9
//                  path.moveTo(540f, 1210f) //小米mix5
//                  path.moveTo(540f, 1309f) //小米mix5
                440 -> //1080*2160
                    path.moveTo(450f, 1250f)
                420 -> //420一加5T
                    path.moveTo(550f, 1400f)
//                    path.moveTo(540f, 1213f)
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
}