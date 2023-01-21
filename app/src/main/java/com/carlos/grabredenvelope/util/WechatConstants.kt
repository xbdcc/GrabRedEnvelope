package com.carlos.grabredenvelope.util

import com.carlos.cutils.util.LogUtils

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

    /* 聊天文本输入框控件 */
    var CHAT_EDITTEXT_ID = "com.tencent.mm:id/b4a" //聊天文本输入框id
    /* 发送按钮控件 */
    var SEND_TEXT_ID = "com.tencent.mm:id/b8k" //发送按钮id

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
            "7.0.16" -> {
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/b4r" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/cyv" //Item内容控件id，通过关键字判断
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/al7" // 红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/ra" // 红包框左下角'微信红包'控件id，等待
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/r0" // 红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/den" // 红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/dem" // 红包弹框关闭控件id
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/d_h" //红包金额id
            }
            "8.0.0", "8.0.1" -> {
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/bg1" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/e7t" //Item内容控件id，通过关键字判断
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/auf" //红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/u5" //红包框左下角'微信红包'控件id，等待
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/tt" //红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/f4f" //红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/ei" //红包弹框关闭控件id
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/eyq" //红包金额id
                CHAT_EDITTEXT_ID = "com.tencent.mm:id/auj" //聊天文本输入框ID
            }
            "8.0.18", "8.0.19" -> {
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/btg" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/fhz" //Item内容控件id，通过关键字判断
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/b47" //红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/y4" //红包框左下角'微信红包'控件id，等待
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/xs" //红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/gix" //红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/giw" //红包弹框关闭控件id
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/gcq" //红包金额id

                CHAT_EDITTEXT_ID = "com.tencent.mm:id/b4a" //聊天文本输入框id
                SEND_TEXT_ID = "com.tencent.mm:id/b8k" //发送按钮id
            }
            else -> { //8.0.31,8.0.32
                /* 微信聊天列表页控件 */
                RED_ENVELOPE_RECT_TITLE_ID = "com.tencent.mm:id/bth" //Item可点击控件id
                RED_ENVELOPE_TITLE_ID = "com.tencent.mm:id/fhs" //Item内容控件id，通过关键字判断
                /* 微信对话页控件 */
                RED_ENVELOPE_ID = "com.tencent.mm:id/b47" //红包框可点击控件id
                RED_ENVELOPE_FLAG_ID = "com.tencent.mm:id/y4" //红包框左下角'微信红包'控件id，等待
                RED_ENVELOPE_BEEN_GRAB_ID = "com.tencent.mm:id/xs" //红包框中间文字'已领取'控件id
                /* 红包弹框控件*/
                RED_ENVELOPE_OPEN_ID = "com.tencent.mm:id/giq" //红包点开控件id
                RED_ENVELOPE_CLOSE_ID = "com.tencent.mm:id/gip" //红包弹框关闭控件id
                /* 红包详情页控件 */
                RED_ENVELOPE_COUNT_ID = "com.tencent.mm:id/git" //红包金额id

                CHAT_EDITTEXT_ID = "com.tencent.mm:id/b4a" //聊天文本输入框id
                SEND_TEXT_ID = "com.tencent.mm:id/b8k" //发送按钮id
            }
        }
    }

}