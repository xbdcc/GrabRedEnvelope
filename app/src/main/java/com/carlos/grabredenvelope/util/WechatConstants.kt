package com.carlos.grabredenvelope.util

import com.carlos.cutils.util.LogUtils

/**
 * Created by Carlos on 2019-05-29.
 */
object WechatConstants {

    var WECHAT_PACKAGE = "com.tencent.mm"
    var WECHAT_LUCKYMONEY_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
    var WECHAT_LUCKYMONEYDETAILUI_ACTIVITY =
        "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyDetailUI" //微信红包详情页


    var RED_ENVELOPE_ID = "com.tencent.mm:id/aou" //聊天页面红包点击框控件id
    var RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aq6" //聊天页面检测红包已被领控件id
    var RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aq7" //聊天页面区分红包id
    var RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf" //抢红包页面点开控件id
    var RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cv0" //抢红包页面退出控件id

    var RED_ENVELOPE_DETAIL_CLOSE_ID = "com.tencent.mm:id/ka" //红包详情页面退出控件id
    var RED_ENVELOPE_TITLE = "[微信红包]" //红包文字
    var RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b5q" //红包id
    var RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b5m" //红包RECT id

    var RED_ENVELOPE_DETAIL_SEND_ID = "com.tencent.mm:id/csu" //红包发送人id
    var RED_ENVELOPE_WISH_WORD_ID = "com.tencent.mm:id/csw" //红包文字id
    var RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/csy" //红包金额id

    var WECHAR_ID = "com.tencent.mm:id/dag" //微信id

    fun setVersion(version: String) {
        LogUtils.d("version:" + version)
        when (version) {
            "7.0.3" -> {

                WECHAT_LUCKYMONEY_ACTIVITY =
                    "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
                WECHAT_LUCKYMONEYDETAILUI_ACTIVITY =
                    "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyDetailUI" //微信红包详情页


                RED_ENVELOPE_ID = "com.tencent.mm:id/aou" //聊天页面红包点击框控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aq6" //聊天页面检测红包已被领控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aq7" //聊天页面区分红包id
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/cyf" //抢红包页面点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cv0" //抢红包页面退出控件id

                RED_ENVELOPE_DETAIL_CLOSE_ID = "com.tencent.mm:id/ka" //红包详情页面退出控件id
                RED_ENVELOPE_TITLE = "[微信红包]" //红包文字
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b5q" //红包id
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b5m" //红包RECT id

                RED_ENVELOPE_DETAIL_SEND_ID = "com.tencent.mm:id/csu" //红包发送人id
                RED_ENVELOPE_WISH_WORD_ID = "com.tencent.mm:id/csw" //红包文字id
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/csy" //红包金额id

                WECHAR_ID = "com.tencent.mm:id/dag" //微信id
            }
            "7.0.4" -> {

                WECHAT_LUCKYMONEY_ACTIVITY =
                    "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI" //微信红包弹框
                WECHAT_LUCKYMONEYDETAILUI_ACTIVITY =
                    "$WECHAT_PACKAGE.plugin.luckymoney.ui.LuckyMoneyDetailUI" //微信红包详情页


                RED_ENVELOPE_ID = "com.tencent.mm:id/ap9" //聊天页面红包点击框控件id
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/aqk" //聊天页面检测红包已被领控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/aql" //聊天页面区分红包id
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/d02" //抢红包页面点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/cwm" //抢红包页面退出控件id

                //下面是点过的
                RED_ENVELOPE_DETAIL_CLOSE_ID = "com.tencent.mm:id/kx" //红包详情页面退出控件id
                RED_ENVELOPE_TITLE = "[微信红包]" //红包文字
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/b6g" //红包id
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b6c" //红包RECT id

                RED_ENVELOPE_DETAIL_SEND_ID = "com.tencent.mm:id/cug" //红包发送人id
                RED_ENVELOPE_WISH_WORD_ID = "com.tencent.mm:id/cui" //红包文字id
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/cuk" //红包金额id

                WECHAR_ID = "com.tencent.mm:id/dcf" //微信id
            }
        }
    }

}