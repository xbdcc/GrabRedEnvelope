package com.carlos.grabredenvelope.old2016

import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.*

/**
 * Created by 小不点 on 2016/2/20.
 */
class XiuYiXiuService(
    private val event: AccessibilityEvent,
    private val nodeRoot: AccessibilityNodeInfo
) {
    private var node: AccessibilityNodeInfo? = null
    //是否能够不停点击咻咻的开关
    private var isCanCyclingClick = false
    private var timer: Timer? = null//定时器

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == MSG_NODE_CLICK) {
                val btnNode = msg.obj as AccessibilityNodeInfo
                btnNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }


    init {

        method()
    }

    fun method() {

        if (event.packageName == PACKAGE_ALIPAY) {
            //            Log.d(TAG,"全局"+event.getEventType()+","+event.getClassName());
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                //进入咻一咻界面
                if (event.className == WINDOW_XIUXIU) {
                    Log.i(TAG, "进入咻咻界面")

                    Log.i(TAG, "------$nodeRoot")
                    node = nodeRoot

                    Log.i(TAG, "----------" + node!!.childCount)

                    for (i in 0 until node!!.childCount) {
                        val n = node!!.getChild(i)
                        Log.i(TAG, "---------$n")
                    }


                    println("支付宝自动咻咻:" + getEventName(event.eventType) + "," + event.className)
                    isCanCyclingClick = true
                    val btn = getButtonInfo(nodeRoot)
                    if (btn != null) {
                        dontStopClick(btn)
                    }
                }
            }
        }
    }

    private fun getEventName(type: Int): String {
        when (type) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> return "窗口内容改变"
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> return "状态改变"
        }
        return ""
    }

    //筛选出咻咻的button，进行不停的点击
    private fun getButtonInfo(parent: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (parent != null && parent.childCount > 0) {
            for (i in 0 until parent.childCount) {
                val node = parent.getChild(i)
                if ("android.widget.Button" == node.className) {
                    Log.i(TAG, "找到按钮")
                    Log.d(TAG, "按钮属性--->$node")
                    //                    node.setClickable(true);
                    //                    Log.d(TAG,"按钮属性--->"+node);
                    return node
                }
            }
        }
        return null
    }

    private fun dontStopClick(btn: AccessibilityNodeInfo) {
        if (timer == null) {
            timer = Timer()
            Log.d(TAG, "定时点击")
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    if (isCanCyclingClick) {
                        //                        Log.d(TAG,"点击中");
                        val m = mHandler.obtainMessage(MSG_NODE_CLICK, btn)
                        mHandler.sendMessage(m)
                    }
                }
            }, 100, delayedTime().toLong())//100，100  0.1秒开始，延迟0.1秒点击

        }
    }

    fun delayedTime(): Int {
        return PreferencesUtils.xiuYiXiuDelay * 100
    }

    companion object {

        private const val TAG = "HongbaoService"

        //支付宝包名
        private const val PACKAGE_ALIPAY = "com.eg.android.AlipayGphone"
        //
        private const val WINDOW_XIUXIU = "com.alipay.android.wallet.newyear.activity.MonkeyYearActivity"
        private const val MSG_NODE_CLICK = 0x110
    }

}
