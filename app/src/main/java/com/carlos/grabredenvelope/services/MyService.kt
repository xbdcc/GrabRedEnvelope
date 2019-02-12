package com.carlos.grabredenvelope.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by 小不点 on 2016/2/23.
 */
class MyService : Service() {


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }


}
