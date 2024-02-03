package com.carlos.grabredenvelope.services

import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
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
 */
class WechatService : BaseAccessibilityService() {

    override var monitorPackageName = WECHAT_PACKAGE
    override var notificationTitle = RED_ENVELOPE_TITLE

    override fun onCreate() {
        super.onCreate()
        WechatConstants.setVersion(AppUtils.getVersionName(WECHAT_PACKAGE))
    }

    /**
     * 部分手机特殊场景下偶现出现红包框但是不走TYPE_WINDOW_STATE_CHANGED的情况，导致不会点击开，手动在点击后调一次避免此问题
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)
        if (AccessibilityEvent.TYPE_VIEW_CLICKED == event.eventType) {
            LogUtils.d("monitorViewClicked:$event")
            if ((status != HAS_CLICKED)) return
            openRedEnvelope(event)
        }
    }

    override fun monitorNotificationChanged(event: AccessibilityEvent) {
        LogUtils.d("monitorNotificationChanged:$event")
        if (RedEnvelopePreferences.wechatControl.isMonitorNotification.not()) {
            return
        }
        if (status == HAS_RECEIVED) {
            return
        }

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

        if(WechatFilter.isRemarkFilter(rootInActiveWindow)) return

        GlobalScope.launch {
            delay(300L)
            grabRedEnvelope()
        }
        monitorChat()
    }


    /**
     * 监控微信聊天列表页面是否有红包，经测试若聊天页面与通知同时开启聊天页面比通知先监听到，聊天列表已点击的情况下就不用去点击通知栏
     */
    private fun monitorChat() {
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
        // 进入红包页面点击开按钮
        if (RedEnvelopePreferences.wechatControl.isCustomClick) {
            LogUtils.d("openRedEnvelopeCustom:" + event.className)
            openRedEnvelopeCustom()
        } else {
            LogUtils.d("openRedEnvelopeAuto:" + event.className)
            openRedEnvelopeAuto(event)
        }
    }

    /**
     * 自动点击开按钮，高版本系统微信加载有过程查找开按钮不会马上找到，加入延迟防止不自动点击开按钮
     */
    private fun openRedEnvelopeAuto(event: AccessibilityEvent) {
        // 如果当前不在聊天不是微信红包弹框或者已经没执行点击红包操作，则不执行拆的操作
        if ((event.className != WECHAT_LUCKYMONEY_ACTIVITY) or (status != HAS_CLICKED)) {
            return
        }

        GlobalScope.launch {
            LogUtils.d("start find open id")
            val envelopes = getNodeInfosByViewId(RED_ENVELOPE_OPEN_ID,300, 5)
            LogUtils.d("end find open id")
            if (envelopes.isNullOrEmpty()) {
                // 没有开按钮，则点击退出按钮
                findAndClickFirstNodeInfoByViewId(RED_ENVELOPE_CLOSE_ID, true)
                LogUtils.d("not has open button:$envelopes")
                return@launch
            }

            val delayTime = 500L + 1000L * RedEnvelopePreferences.wechatControl.delayOpenTime
            LogUtils.d("delay open time:$delayTime")
            delay(delayTime)
            clickFirstNodeInfo(envelopes, true)
            status = HAS_OPENED
            LogUtils.d("opened a redenvelope")
        }
    }

    /**
     * Android7.0以上有效，坐标点点击开按钮
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
        val delayTime = 500L + 1000L * RedEnvelopePreferences.wechatControl.delayOpenTime
        LogUtils.d("delay custom open time:$delayTime")
        gesturePath(path,  delayTime, interval = 500, times = 3)
        status = HAS_OPENED
        LogUtils.d("opened a redenvelope")
    }

    /**
     * 退出红包详情页
     */
    private fun quitEnvelope(event: AccessibilityEvent) {
        // 如果当前页面不是红包详情页或者没有点开过拆按钮，则不执行退出操作
        if ((event.className != WECHAT_LUCKYMONEYDETAILUI_ACTIVITY) or (status != HAS_OPENED)) {
            return
        }

        GlobalScope.launch {
            saveData()
            val delayTime = 1000L * RedEnvelopePreferences.wechatControl.delayCloseTime
            LogUtils.d("delay close time:$delayTime")
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
        getNodeInfosByViewId(RED_ENVELOPE_COUNT_ID)?.let {
            if (it.isNullOrEmpty())return
            val wechatRedEnvelope = WechatRedEnvelope()
            wechatRedEnvelope.count = it[0].text.toString()
            WechatRedEnvelopeDb.insertData(wechatRedEnvelope)
        }
    }
}