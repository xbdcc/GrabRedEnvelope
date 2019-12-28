package com.carlos.grabredenvelope.execption

import com.carlos.cutils.execption.CUncaughtExceptionHandler
import io.sentry.Sentry

/**
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2019-11-08.
 */
class MyUncaughtExceptionHandler : CUncaughtExceptionHandler() {

    private val mUncaughHandler: Thread.UncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (t == null || e == null) return
        Sentry.capture(e)
    }

}