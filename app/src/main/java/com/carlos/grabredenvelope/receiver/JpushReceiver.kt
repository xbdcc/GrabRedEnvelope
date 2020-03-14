package com.carlos.grabredenvelope.receiver

import android.content.Context
import android.content.Intent
import android.net.Uri
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.activity.MainActivity
import com.carlos.grabredenvelope.data.RedEnvelopePreferences
import org.json.JSONObject

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
 * Created by 小不点 on 2016/2/22.
 */
/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JpushReceiver : JPushMessageReceiver() {


    override fun onRegister(p0: Context?, p1: String?) {
        super.onRegister(p0, p1)
        LogUtils.d("registrationId:" + p1.toString())
    }

    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        super.onMessage(p0, p1)
        LogUtils.d(p1.toString())
    }

    override fun onNotifyMessageArrived(context: Context?, notificationMessage: NotificationMessage?) {
        super.onNotifyMessageArrived(context, notificationMessage)
        if (context == null || notificationMessage == null) return
        LogUtils.d("onNotifyMessageArrived:$notificationMessage")
        val datas = JSONObject(notificationMessage.notificationExtras)
        val keys = datas.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            LogUtils.d("key:" + key +"-value:" + datas.get(key))
            if ("start" == datas.get(key)) {
                RedEnvelopePreferences.useStatus = true
            } else if ("stop" == datas.get(key)) {
                RedEnvelopePreferences.useStatus = false
            }
        }
    }

    override fun onNotifyMessageOpened(context: Context?, notificationMessage: NotificationMessage?) {
        super.onNotifyMessageOpened(context, notificationMessage)
        if (context == null || notificationMessage == null) return
        LogUtils.d("onNotifyMessageOpened:$notificationMessage")
        val datas = JSONObject(notificationMessage.notificationExtras)
        val keys = datas.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = datas.get(key).toString()
            LogUtils.d("key:$key-value:$value")
            when(key) {
                "url" -> {
                    val uri = Uri.parse(value)
                    val intent= Intent(Intent.ACTION_VIEW, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                }
                "update" -> { //如果链接附加字段中为update则点击到主页面
                    val i = Intent()
                    i.setClass(context, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(i)
                }
                "message" -> {
                    //打开自定义的Activity
                    val i = Intent(context, MessageActivity::class.java)
                    i.putExtra("data", notificationMessage.notificationContent)
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(i)
                }
                "main" -> {
                    //打开自定义的Activity
                    val i = Intent(context, MainActivity::class.java)
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(i)
                }
            }
        }
    }

    override fun onNotifyMessageDismiss(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageDismiss(p0, p1)
        LogUtils.d(p1.toString())
    }


}
