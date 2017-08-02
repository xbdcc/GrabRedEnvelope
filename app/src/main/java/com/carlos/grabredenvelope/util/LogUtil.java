package com.carlos.grabredenvelope.util;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void i(String msg) {
        Log.i(TAG, getLogTitle() + msg);
    }

    public static void i(String msg, Throwable throwable) {
        Log.i(TAG, getLogTitle() + msg, throwable);
    }

    public static void d(String msg) {
        Log.d(TAG, getLogTitle() + msg);
    }

    public static void d(String msg, Throwable throwable) {
        Log.d(TAG, getLogTitle() + msg, throwable);
    }

    public static void w(String msg) {
        Log.w(TAG, getLogTitle() + msg);
    }

    public static void w(String msg, Throwable throwable) {
        Log.w(TAG, getLogTitle() + msg, throwable);
    }

    public static void e(String msg) {
        Log.e(TAG, getLogTitle() + msg);
    }

    public static void e(String msg, Throwable throwable) {
        Log.e(TAG, getLogTitle() + msg, throwable);
    }

    public static String getLogTitle() {
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int index = stackTraceElements.length > 4 ? 4 : stackTraceElements.length - 1;
        stringBuilder.append("[");
        stringBuilder.append(stackTraceElements[index].getClassName()).append(".");
        stringBuilder.append(stackTraceElements[index].getMethodName()).append("()").append(":");
        stringBuilder.append("lineNumber=").append(stackTraceElements[index].getLineNumber());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
