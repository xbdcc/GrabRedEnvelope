package com.carlos.grabredenvelope.util

import com.carlos.cutils.util.LogUtils

/**
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2019-05-29.
 */
object WechatConstants {

    val WECHAT_PACKAGE = "com.tencent.mm"
    /* 页面 */
    val WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
    val WECHAT_LUCKYMONEYDETAILUI_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyDetailUI" //微信红包详情页

    var RED_ENVELOPE_TITLE = "[微信红包]" //红包关键字

    /* 微信聊天列表页控件 */
    var RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/bah" //Item可点击控件id
    var RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/bal" //Item内容控件id，通过关键字判断

    /* 微信对话页控件 */
    var RED_ENVELOPE_ID = "com.tencent.mm:id/atb" // 红包框可点击控件id
    var RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aum" // 红包框左下角'微信红包'控件id
    var RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aul" // 红包框中间文字'已领取'控件id

    /* 红包弹框控件*/
    var RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/dan" // 红包点开控件id
    var RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/d84" // 红包弹框关闭控件id

    /* 红包详情页控件 */
    var RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/d62" //红包金额id


    fun setVersion(version: String) {
        LogUtils.d("version:$version")
        when (version) {
            "7.0.3" -> {

                RED_ENVELOPE_ID = "com.tencent.mm:id/aou" //聊天页面红包点击框控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aq6" //聊天页面检测红包已被领控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aq7" //聊天页面区分红包id
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf" //抢红包页面点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cv0" //抢红包页面退出控件id

                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b5q" //红包id
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b5m" //红包RECT id

                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/csy" //红包金额id

            }
            "7.0.4" -> {

                RED_ENVELOPE_ID = "com.tencent.mm:id/ap9" //聊天页面红包点击框控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aqk" //聊天页面检测红包已被领控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aql" //聊天页面区分红包id
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d02" //抢红包页面点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cwm" //抢红包页面退出控件id

                //下面是点过的
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b6g" //红包id
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b6c" //红包RECT id

                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/cuk" //红包金额id

            }
            "7.0.5" -> {

                RED_ENVELOPE_ID = "com.tencent.mm:id/ar0" //聊天页面红包点击框控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/asa" //聊天页面检测红包已被领控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/asb" //聊天页面区分红包id
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d4h" //抢红包页面点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/d0y" //抢红包页面退出控件id

                //下面是点过的
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b97" //红包id
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b93" //红包RECT id

                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/cyw" //红包金额id

            }
            "7.0.8", "7.0.9", "7.0.10", "7.0.11" -> {
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/atb" // 红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aum" // 红包框左下角'微信红包'控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aul" // 红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/dan" // 红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/d84" // 红包弹框关闭控件id
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/bah" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/bal" //Item内容控件id，通过关键字判断
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/d62" //红包金额id
            }
            "7.0.12" -> {
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b1t" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/cto" //Item内容控件id，通过关键字判断
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/ajp" // 红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/r2" // 红包框左下角'微信红包'控件id，等待
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/qr" // 红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d_4" // 红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/d_3" // 红包弹框关闭控件id
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/d4y" //红包金额id
            }
        }
    }

}