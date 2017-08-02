package com.carlos.grabredenvelope.util;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 小不点 on 2016/2/6.
 */
public class MyApplication extends Application
{
    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}