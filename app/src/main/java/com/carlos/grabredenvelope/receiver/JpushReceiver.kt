package com.carlos.grabredenvelope.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.carlos.grabredenvelope.main.MainActivity
import com.carlos.grabredenvelope.util.PreferencesUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by 小不点 on 2016/2/22.
 */
/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
class JpushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.action + ", extras: " + printBundle(bundle!!))

        if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
            val regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId!!)
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE)!!)
            //            processCustomMessage(context, bundle);
            val message = bundle.getString(JPushInterface.EXTRA_MESSAGE)
            if (message == "start") {
                Log.d(TAG, "可以用")
                PreferencesUtils.setUseStatus(true)
            } else if (message == "stop") {
                Log.d(TAG, "不可以使用")
                PreferencesUtils.setUseStatus(false)
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知")
            val notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: $notifactionId")

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知")
            val json = intent.extras!!.getString(JPushInterface.EXTRA_EXTRA)
            Log.d(TAG, "[object]" + json!!)
            try {
                val `object` = JSONObject(json)
                val it = `object`.keys()
                while (it.hasNext()) {
                    val key = it.next()
                    Log.d(TAG, "[object]key$key")
                    val value = `object`.optString(key)
                    Log.d(TAG, "[object]value$value")
                    //如果附加字段中key为url则点击跳转到链接
                    if (key == "url") {
                        val uri = Uri.parse(value)
                        val i = Intent(Intent.ACTION_VIEW, uri)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                    }
                    //如果链接附加字段中为update则点击到主页面
                    if (key == "update") {
                        val i = Intent()
                        i.setClass(context, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                    }
                    //如果是信息打开
                    if (key == "message") {
                        //打开自定义的Activity
                        val i = Intent(context, MessageActivity::class.java)
                        i.putExtras(bundle)
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                    }
                    if (key == "main") {
                        //打开自定义的Activity
                        val i = Intent(context, MainActivity::class.java)
                        i.putExtras(bundle)
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(i)
                    }
                    //                    if(key.equals("use")){
                    //                        if(value.equals("true")){
                    //                            PreferencesUtils.setUseStatus(true);
                    //                        }else {
                    //                            PreferencesUtils.setUseStatus(false);
                    //                            Log.d(TAG,"停止使用");
                    //                        }
                    //                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            //
            //            //打开自定义的Activity
            //            Intent i = new Intent(context, TestActivity.class);
            //            i.putExtras(bundle);
            //            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            //            context.startActivity(i);


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK == intent.action) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA)!!)
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            val json = intent.extras!!.getString(JPushInterface.EXTRA_EXTRA)
            Log.d(TAG, "[object2]" + json!!)
            //            try {
            //                JSONObject object=new JSONObject(json);
            //                for(int i=0;i<object.length();i++){
            //                    Log.d(TAG, "[object]" + object.);
            //                }
            //            } catch (JSONException e) {
            //                e.printStackTrace();
            //            }
            try {
                val array = JSONArray(json)
                for (i in 0 until array.length()) {
                    val `object` = array.getJSONObject(i)
                    Log.d(TAG, "[object]$`object`")

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE == intent.action) {
            val connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false)
            Log.w(TAG, "[MyReceiver]" + intent.action + " connected state change to " + connected)
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.action!!)
        }
    }

    companion object {
        private val TAG = "JpushReceiver"


        // 打印所有的 intent extra 数据
        private fun printBundle(bundle: Bundle): String {
            val sb = StringBuilder()
            for (key in bundle.keySet()) {
                if (key == JPushInterface.EXTRA_NOTIFICATION_ID) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getInt(key))
                } else if (key == JPushInterface.EXTRA_CONNECTION_CHANGE) {
                    sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key))
                } else if (key == JPushInterface.EXTRA_EXTRA) {
                    if (bundle.getString(JPushInterface.EXTRA_EXTRA)!!.isEmpty()) {
                        Log.i(TAG, "This message has no Extra data")
                        continue
                    }

                    try {
                        val json = JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA))
                        val it = json.keys()

                        while (it.hasNext()) {
                            val myKey = it.next().toString()
                            sb.append("\nkey:" + key + ", value: [" +
                                    myKey + " - " + json.optString(myKey) + "]")
                        }
                    } catch (e: JSONException) {
                        Log.e(TAG, "Get message extra JSON error!")
                    }

                } else {
                    sb.append("\nkey:" + key + ", value:" + bundle.getString(key))
                }
            }
            return sb.toString()
        }
    }

}
