package com.carlos.grabredenvelope.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Created by 小不点 on 2016/2/7.
 */
public class WakeupTools {

    //唤醒屏幕解锁
    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km=(KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl=km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP| PowerManager.SCREEN_DIM_WAKE_LOCK,"wakeup");
        //点亮屏幕
        wl.acquire();
        //释放资源
        wl.release();

    }
}
