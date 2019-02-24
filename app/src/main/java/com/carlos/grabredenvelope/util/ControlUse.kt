package com.carlos.grabredenvelope.util

import android.content.Context
import android.os.Handler
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 小不点 on 2016/2/6.
 */
class ControlUse(private val context: Context) {

    private val result: String? = null
    private val handler: Handler? = null
    private var message: String? = null

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
        Log.i("currentTime", "是否停止使用\n现在是" + year + "年" + (month + 1) + "月" + day + "日" + hour + "时" + minute + "分")

        //设置在2016年2月10号停止使用
        //        if (year>=2016&&month>=1&&day>=15) {
        //            isStop=true;
        ////            SharedPreferences preferences=context.getSharedPreferences("hongbao_stop", context.MODE_PRIVATE);
        ////            SharedPreferences.Editor editor=preferences.edit();
        ////            editor.putString("contorl","stop");
        ////            editor.commit();
        //        }

        message = "本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！"
        //设置使用期限2月25
        //        String stoptime="2016-03-30 00:00:00.000";//大于此时间的才可以使用
        val stoptime = "2019-3-23 00:00:00.000"//大于此时间的才可以使用
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

    fun stopUse(): Boolean {
        return isStop
    }

    fun showDialog() {
        DialogUtils.show_dialog(context, "提示", message)
    }

    companion object {
        var isStop = false
    }
}
