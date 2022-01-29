package com.carlos.grabredenvelope.data

import com.carlos.cutils.base.CBasePreferences
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.dao.WechatControlVO
import kotlinx.serialization.json.JSON

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
 * Created by Carlos on 2019/2/21.
 */
object RedEnvelopePreferences :
    CBasePreferences("redenvelope_preferences", MyApplication.instance.applicationContext) {

    private const val WECHAT_CONTROL = "wechat_control"
    private const val USE_STATUS = "use_status"
    private const val EMOJI_TEXT = "emoji_text"
    private const val EMOJI_TIMES = "emoji_times"
    private const val EMOJI_INTERVAL = "emoji_interval"
    private const val STOP_TIME = "stop_time"

    var wechatControl: WechatControlVO
        get() {
            val data = getString(WECHAT_CONTROL, "")
            if (data.isNullOrEmpty()) return WechatControlVO()
            return try {
                JSON.parse(WechatControlVO.serializer(), data)
            } catch (e: Exception) {
                LogUtils.e("error:", e)
                setString(
                    WECHAT_CONTROL,
                    JSON.stringify(WechatControlVO.serializer(), WechatControlVO())
                )
                WechatControlVO()
            }
        }
        set(value) {
            setString(WECHAT_CONTROL, JSON.stringify(WechatControlVO.serializer(), value))
        }

    var useStatus: Boolean
        get() = getBoolean(USE_STATUS, true)
        set(value) {
            setBoolean(USE_STATUS, value)
        }

    var autoText: String
        get() = getString(EMOJI_TEXT, "[烟花]")
        set(value) {
            setString(EMOJI_TEXT, value)
        }

    var emojiTimes: Int
        get() = getInt(EMOJI_TIMES, 0)
        set(value) {
            setInt(EMOJI_TIMES, value)
        }

    var emojiInterval: Int
        get() = getInt(EMOJI_INTERVAL, 0)
        set(value) {
            setInt(EMOJI_INTERVAL, value)
        }

    var stopTime: String
        get() = getString(STOP_TIME, "")
        set(value) {
            setString(STOP_TIME, value)
        }

}



