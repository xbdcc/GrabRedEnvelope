package com.carlos.grabredenvelope.util

import android.content.Context
import android.util.Log
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.old2016.DialogUtils
import com.carlos.grabredenvelope.old2016.PreferencesUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
 * Created by 小不点 on 2016/2/6.
 */
class ControlUse(private val context: Context) {

    private var message: String? = null
    private var isStop = false

    init {
        setLimitTime()
    }

    fun setLimitTime() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        LogUtils.i("是否停止使用\n现在是" + year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分")
        message = "本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！"
        //设置使用期限2月25
        //        String stoptime="2016-03-30 00:00:00.000";//大于此时间的才可以使用
        val stoptime = "2019-12-31 00:00:00.000"//小于此时间的才可以使用
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Log.i("停止使用时间", stoptime)
        try {
            val stopDate = dateFormat.parse(stoptime)
            val currentDate = Date(System.currentTimeMillis())
            if (currentDate.time > stopDate.time) {
                //                DialogUtils.show_dialog(context, "提示", message);
                isStop = true
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    fun stopUse(): Boolean = isStop or !PreferencesUtils.usestatus

    fun showDialog() {
        DialogUtils.show_dialog(context, "提示", message)
    }

}
