package com.carlos.grabredenvelope.util

import android.util.Log

object LogUtil {

    private val TAG = "LogUtil"

    val logTitle: String
        get() {
            val stringBuilder = StringBuilder()
            val stackTraceElements = Thread.currentThread().stackTrace
            val index = if (stackTraceElements.size > 4) 4 else stackTraceElements.size - 1
            stringBuilder.append("[")
            stringBuilder.append(stackTraceElements[index].className).append(".")
            stringBuilder.append(stackTraceElements[index].methodName).append("()").append(":")
            stringBuilder.append("lineNumber=").append(stackTraceElements[index].lineNumber)
            stringBuilder.append("]")
            return stringBuilder.toString()
        }

    fun i(msg: String) {
        Log.i(TAG, logTitle + msg)
    }

    fun i(msg: String, throwable: Throwable) {
        Log.i(TAG, logTitle + msg, throwable)
    }

    fun d(msg: String) {
        Log.d(TAG, logTitle + msg)
    }

    fun d(msg: String, throwable: Throwable) {
        Log.d(TAG, logTitle + msg, throwable)
    }

    fun w(msg: String) {
        Log.w(TAG, logTitle + msg)
    }

    fun w(msg: String, throwable: Throwable) {
        Log.w(TAG, logTitle + msg, throwable)
    }

    fun e(msg: String) {
        Log.e(TAG, logTitle + msg)
    }

    fun e(msg: String, throwable: Throwable) {
        Log.e(TAG, logTitle + msg, throwable)
    }
}
