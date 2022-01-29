package com.carlos.grabredenvelope.services

import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.cutils.extend.*
import com.carlos.cutils.util.AppUtils
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.data.RedEnvelopePreferences
import com.carlos.grabredenvelope.db.WechatRedEnvelope
import com.carlos.grabredenvelope.db.WechatRedEnvelopeDb
import com.carlos.grabredenvelope.util.WechatConstants
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_BEEN_GRAB_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_CLOSE_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_COUNT_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_FLAG_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_OPEN_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_RECT_TITLE_ID
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_TITLE
import com.carlos.grabredenvelope.util.WechatConstants.RED_ENVELOPE_TITLE_ID
import com.carlos.grabredenvelope.util.WechatConstants.WECHAT_LUCKYMONEYDETAILUI_ACTIVITY
import com.carlos.grabredenvelope.util.WechatConstants.WECHAT_LUCKYMONEY_ACTIVITY
import com.carlos.grabredenvelope.util.WechatConstants.WECHAT_PACKAGE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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
 * Adapt Wechat 7.0.3,7.0.4,7.0.5,7.0.8,7.0.9,7.0.10,7.0.11,7.0.12,7.0.16,8.0.0.
 */
class WechatService : BaseAccessibilityService() {

    override var monitorPackageName = WECHAT_PACKAGE
    override var notificationTitle = RED_ENVELOPE_TITLE

    override fun onCreate() {
        super.onCreate()
//        LogUtils.d("service onCreate.")
        WechatConstants.setVersion(AppUtils.getVersionName(WECHAT_PACKAGE))
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)
        if (AccessibilityEvent.TYPE_VIEW_CLICKED == event.eventType) {
            LogUtils.d("monitorViewClicked:$event")
            if ((status != HAS_CLICKED)) return
            openRedEnvelope(event)
        }
    }

//    override fun monitorViewClicked(event: AccessibilityEvent) {
//        super.monitorViewClicked(event)
//        LogUtils.d("monitorViewClicked:$event")
//        openRedEnvelope(event)
//    }

    override fun monitorNotificationChanged(event: AccessibilityEvent) {
        LogUtils.d("monitorNotificationChanged:$event")
        if (RedEnvelopePreferences.wechatControl.isMonitorNotification.not()) {
            return
        }
        if (status == HAS_RECEIVED) {
            return
        }

//        if(WechatFilter.isNotifacationFilter(event.text?.toString())) return

        super.monitorNotificationChanged(event)
    }

    override fun monitorWindowChanged(event: AccessibilityEvent) {
        LogUtils.d("monitorWindowChanged:$event")

        if(WechatFilter.isRemarkFilter(rootInActiveWindow)) return

        openRedEnvelope(event)
        quitEnvelope(event)
    }

    override fun monitorContentChanged(event: AccessibilityEvent) {
        LogUtils.d("monitorContentChanged:$event")


        if(WechatFilter.isRemarkFilter(rootInActiveWindow)) {
            LogUtils.d("in filter, not grab")
            return
        }

        grabRedEnvelope()
        monitorChat()
    }


    /**
     * 监控微信聊天列表页面是否有红包，经测试若聊天页面与通知同时开启聊天页面比通知先监听到，聊天列表已点击的情况下就不用去点击通知栏
     */
    private fun monitorChat() {
//        LogUtils.d("monitorChat")
        // 监控关闭则不执行后续操作
        if (RedEnvelopePreferences.wechatControl.isMonitorChat.not()) {
            return
        }
        if (findAndClickFirstNodeInfoByViewIdContainsText(
                RED_ENVELOPE_RECT_TITLE_ID, RED_ENVELOPE_TITLE_ID, RED_ENVELOPE_TITLE
            )
        ) {
            status = HAS_RECEIVED
            LogUtils.d("received a redenvelope.")
        }
    }

    /**
     * 对话页面监控点击红包, 从最下面开始点起
     */
    private fun grabRedEnvelope() {
//        LogUtils.d("grabRedEnvelope")
        /* 发现红包点击进入领取红包页面 */
        val ifGrabSelf = RedEnvelopePreferences.wechatControl.ifGrabSelf
        if (findAndClickFirstNodeInfoByViewId(
                RED_ENVELOPE_ID, RED_ENVELOPE_FLAG_ID, RED_ENVELOPE_BEEN_GRAB_ID, !ifGrabSelf, true
            )
        ) {
            status = HAS_CLICKED
            LogUtils.d("received a redenvelope and click.")
        }
    }

    /**
     * 拆开红包
     */
    private fun openRedEnvelope(event: AccessibilityEvent) {
        // 如果当前不在聊天不是微信红包弹框或者已经没执行点击红包操作，则不执行拆的操作
        if (status != HAS_CLICKED) {
            return
        }

        LogUtils.d("openEnvelope")
        GlobalScope.launch {
            delay(500L)//小米华为等部分手机瞬间获取不到节点，暂时增加延迟避免无法点击开按钮

            // 进入红包页面点击开按钮
            if (RedEnvelopePreferences.wechatControl.isCustomClick) {
                LogUtils.d("openRedEnvelopeCustom")
                openRedEnvelopeCustom()
            } else {
                LogUtils.d("openRedEnvelopeAuto")
                var envelopes = getNodeInfosByViewId(RED_ENVELOPE_OPEN_ID) ?: return@launch
                if (envelopes.isEmpty()) {
                    // 没有开按钮，则点击退出按钮
                    findAndClickFirstNodeInfoByViewId(RED_ENVELOPE_CLOSE_ID, true)
                    return@launch
                }
                openRedEnvelopeAuto(envelopes)
            }
        }

    }

    private fun openRedEnvelopeAuto(envelopes: MutableList<AccessibilityNodeInfo>) {
        GlobalScope.launch {
            val delayTime = 1000L * RedEnvelopePreferences.wechatControl.delayOpenTime
//            LogUtils.d("delay open time:$delayTime")
            delay(delayTime + 500)//小米华为等部分手机瞬间获取不到节点，暂时增加延迟避免无法点击开按钮
            clickFirstNodeInfo(envelopes, true)
            status = HAS_OPENED
            LogUtils.d("opened a redenvelope")
        }
    }

    /**
     * Android7.0以上有效
     */
    private fun openRedEnvelopeCustom() {
        if (status != HAS_CLICKED) {
            return
        }

        val path = Path()
        if (RedEnvelopePreferences.wechatControl.isCustomClick) {
            path.moveTo(
                RedEnvelopePreferences.wechatControl.pointX.toFloat(),
                RedEnvelopePreferences.wechatControl.pointY.toFloat()
            )
        }
        val delayTime = 1000L * RedEnvelopePreferences.wechatControl.delayOpenTime
        LogUtils.d("delay open time:$delayTime")
        gesturePath(path, delayTime)
        status = HAS_OPENED
        LogUtils.d("opened a redenvelope")
    }


    /**
     * 退出红包详情页
     */
    private fun quitEnvelope(event: AccessibilityEvent) {
//        LogUtils.d("quitEnvelope")
        // 如果当前页面不是红包详情页或者没有点开过拆按钮，则不执行退出操作
        if ((event.className != WECHAT_LUCKYMONEYDETAILUI_ACTIVITY) or (status != HAS_OPENED)) {
            return
        }

        GlobalScope.launch {
            saveData()
            val delayTime = 1000L * RedEnvelopePreferences.wechatControl.delayCloseTime
//            LogUtils.d("delay close time:$delayTime")
            if (delayTime != 11000L) {
                delay(delayTime)
                back()
            }
        }
        status = WAIT_NEW
        LogUtils.d("quit redenvelope detail page.")
    }

    /**
     * 记录抢到的金额本地查看记录
     */
    private fun saveData() {
        getNodeInfosByViewId(RED_ENVELOPE_COUNT_ID)?.get(0)?.let {
            val wechatRedEnvelope = WechatRedEnvelope()
            wechatRedEnvelope.count = it.text.toString()
            WechatRedEnvelopeDb.insertData(wechatRedEnvelope)
        }
    }
}