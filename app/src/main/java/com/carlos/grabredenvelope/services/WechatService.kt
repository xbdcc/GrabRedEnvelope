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
 * Adapt Wechat 7.0.3,7.0.4,7.0.5,7.0.8,7.0.9,7.0.10,7.0.11,7.0.12.
 */
class WechatService : BaseAccessibilityService() {

    override var monitorPackageName = WECHAT_PACKAGE
    override var notificationTitle = RED_ENVELOPE_TITLE

    override fun onCreate() {
        super.onCreate()
//        LogUtils.d("service onCreate.")
        WechatConstants.setVersion(AppUtils.getVersionName(WECHAT_PACKAGE))
    }

    override fun monitorNotificationChanged(event: AccessibilityEvent) {
//        LogUtils.d("通知改变:$event")
        if (RedEnvelopePreferences.wechatControl.isMonitorNotification.not()) {
            return
        }
        if (status == HAS_RECEIVED) {
            return
        }
        super.monitorNotificationChanged(event)
    }

    override fun monitorWindowChanged(event: AccessibilityEvent) {
//        LogUtils.d("界面改变:$event")
        openRedEnvelope(event)
        quitEnvelope(event)
    }

    override fun monitorContentChanged(event: AccessibilityEvent) {
//        LogUtils.d("内容改变:$event")
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

        if (findAndClickFirstNodeInfoByViewId(
                RED_ENVELOPE_ID, RED_ENVELOPE_FLAG_ID, RED_ENVELOPE_BEEN_GRAB_ID, true
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
        if ((event.className != WECHAT_LUCKYMONEY_ACTIVITY) or (status != HAS_CLICKED)) {
            return
        }
        var envelopes = getNodeInfosByViewId(RED_ENVELOPE_OPEN_ID) ?: return
        if (envelopes.isEmpty()) {
            // 没有开按钮，则点击退出按钮
            findAndClickFirstNodeInfoByViewId(RED_ENVELOPE_CLOSE_ID, true)
            return
        }
        // 进入红包页面点击开按钮
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            openRedEnvelopeBefore(envelopes)
        } else {
            openRedEnvelopeNew(event)
        }
    }

    private fun openRedEnvelopeBefore(envelopes: MutableList<AccessibilityNodeInfo>) {
        GlobalScope.launch {
            val delayTime = 1000L * RedEnvelopePreferences.wechatControl.delayOpenTime
//            LogUtils.d("delay open time:$delayTime")
            delay(delayTime)
            clickFirstNodeInfo(envelopes, true)
            status = HAS_OPENED
            LogUtils.d("opened a redenvelope")
        }
    }

    private fun openRedEnvelopeNew(event: AccessibilityEvent) {
//        LogUtils.d("Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT)
        if ((status != HAS_CLICKED) or (WECHAT_LUCKYMONEY_ACTIVITY != currentClassName)) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val metrics = resources.displayMetrics
            val dpi = metrics.densityDpi
            val path = Path()
            if (RedEnvelopePreferences.wechatControl.isCustomClick) {
                path.moveTo(
                    RedEnvelopePreferences.wechatControl.pointX.toFloat(),
                    RedEnvelopePreferences.wechatControl.pointY.toFloat()
                )
            } else when (dpi) {
                640 -> //1440
                    path.moveTo(720f, 1575f)
                320 -> //720p
                    path.moveTo(360f, 780f)
                480 -> //1080p
                    path.moveTo(540f, 1465f) //oppo r15,android 9, 小米8 android 9
//                  path.moveTo(540f, 1210f) //小米mix5
                440 -> //1080*2160
                    path.moveTo(450f, 1250f)
                420 -> //420一加5T
                    path.moveTo(540f, 1213f)
                400 ->
                    path.moveTo(550f, 1200f) //华为mate9
                else ->
                    path.moveTo(550f, 1200f)
            }
            gesturePath(path)
        }
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
        LogUtils.d("quit redenvelop detail page.")
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