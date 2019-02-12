package com.carlos.grabredenvelope.util

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager

/**
 * Created by 小不点 on 2016/2/7.
 */
object WakeupTools {

    //唤醒屏幕解锁
    @SuppressLint("InvalidWakeLockTag")
    fun wakeUpAndUnlock(context: Context) {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "wakeup")
        //点亮屏幕
        wl.acquire()
        //释放资源
        wl.release()

    }
}
