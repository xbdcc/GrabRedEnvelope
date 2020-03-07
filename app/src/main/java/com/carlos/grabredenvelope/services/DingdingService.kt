package com.carlos.grabredenvelope.services

import android.view.accessibility.AccessibilityEvent
import com.carlos.cutils.extend.back
import com.carlos.cutils.extend.findAndClickFirstNodeInfoByViewId
import com.carlos.cutils.extend.gestureViewCenter
import com.carlos.cutils.extend.getNodeInfosByViewId
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.db.DingDingRedEnvelope
import com.carlos.grabredenvelope.db.DingDingRedEnvelopeDb
import kotlinx.coroutines.GlobalScope
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
 * Dingding Version 5.0.5.
 */
class DingdingService : BaseAccessibilityService() {

    override var monitorPackageName = DINGDING_PACKAGE

    companion object {
        private const val DINGDING_PACKAGE = "com.alibaba.android.rimet"

        private const val DINGDING_RED_ENVELOPE_ACTIVITY =
            "com.alibaba.android.dingtalk.redpackets.activities.FestivalRedPacketsPickActivity" //钉钉红包弹框

        private const val DINGDING_CHAT_ITEM_ID =
            "com.alibaba.android.rimet:id/chatting_content_view_stub" //钉钉聊天页面可点击列表
        private const val DINGDING_RED_ENVELOPE_TYPE_ID =
            "com.alibaba.android.rimet:id/tv_redpackets_type" //钉钉聊天页面红包类型
        private const val DINGDING_REDENVELOPE_COVER =
            "com.alibaba.android.rimet:id/iv_cover" //钉钉聊天页面红包已领取

        private const val DINGDING_PICK_BOTTOM =
            "com.alibaba.android.rimet:id/iv_pick_bottom" //钉钉拆红包底部弹框
        private const val DINGDING_REDENVELOPE_DETAIL_ACTIVITY =
            "com.alibaba.android.dingtalk.redpackets.activities.RedPacketsDetailActivity" //钉钉拆红包底部弹框
        private const val DINGDING_REDENVELOPE_MONEY =
            "com.alibaba.android.rimet:id/redpackets_money" //钉钉红包金额
    }

    override fun monitorContentChanged(event: AccessibilityEvent) {
        grabRedEnvelope()
    }

    override fun monitorWindowChanged(event: AccessibilityEvent) {
        openRedEnvelope(event)
        quitEnvelope(event)
    }

    /**
     * 聊天页面监控点击红包
     */
    private fun grabRedEnvelope() {
        LogUtils.d("grabRedEnvelope")

        val envelopes = getNodeInfosByViewId(DINGDING_CHAT_ITEM_ID) ?: return
        if (status != WAIT_NEW) {
            return
        }

        /* 发现红包点击进入领取红包页面 */
        if (findAndClickFirstNodeInfoByViewId(
                DINGDING_CHAT_ITEM_ID,
                DINGDING_RED_ENVELOPE_TYPE_ID,
                DINGDING_REDENVELOPE_COVER,
                true
            )
        ) {
            status = HAS_CLICKED
        }
    }


    /**
     * 拆开红包
     */
    private fun openRedEnvelope(event: AccessibilityEvent) {
        if (event.className != DINGDING_RED_ENVELOPE_ACTIVITY) return
        if (status != HAS_CLICKED) return
        gestureViewCenter(DINGDING_PICK_BOTTOM)
        status = HAS_OPENED
    }

    /**
     * 退出红包详情页
     */
    private fun quitEnvelope(event: AccessibilityEvent) {
        LogUtils.d("quitEnvelope")
        if (event.className != DINGDING_REDENVELOPE_DETAIL_ACTIVITY) return
        if (status != HAS_OPENED) return
        GlobalScope.launch {
            saveData()
            back()
        }
        status = WAIT_NEW
    }


    private fun saveData() {
        val count = getNodeInfosByViewId(DINGDING_REDENVELOPE_MONEY)
        count?.get(0)?.let {
            val dingDingRedEnvelope = DingDingRedEnvelope()
            dingDingRedEnvelope.count = it.text.toString()
            DingDingRedEnvelopeDb.insertData(dingDingRedEnvelope)
        }
    }

//    /**
//     * 递归遍历出WebView节点
//     */
//    private var accessibilityNodeInfoWebView: AccessibilityNodeInfo? = null
//
//    private fun findWebViewNode(rootNode: AccessibilityNodeInfo) {
//        for (i in 0 until rootNode.childCount) {
//            val child = rootNode.getChild(i)
//            if (child == null) continue
//            if ("android.webkit.WebView" == child.className) {
//                accessibilityNodeInfoWebView = child
//                LogUtils.d("findWebViewNode--找到webView" + accessibilityNodeInfoWebView)
//                return
//            }
//            if (child.childCount > 0) {
//                findWebViewNode(child)
//            }
//        }
//    }
//
//    fun getNodes(accessibilityNodeInfo: AccessibilityNodeInfo) {
//        for (index in 0 until accessibilityNodeInfo.childCount) {
//            val nodeInfo = accessibilityNodeInfo.getChild(index)
//            LogUtils.d("nodeinfo:$nodeInfo")
//            if (nodeInfo != null)
//                getNodes(nodeInfo)
//        }
//    }

}