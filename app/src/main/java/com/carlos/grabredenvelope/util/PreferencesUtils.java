package com.carlos.grabredenvelope.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 小不点 on 2016/2/19.
 */
public class PreferencesUtils {

    private static final Context context=MyApplication.getAppContext();
    private static SharedPreferences.Editor editor;

    /**
     * 控制QQ
     */
    private static final String PREFERENCE_CONTROL ="preference_control";
    private static final String USE_STATUS="use_status";
    private static final String QQ_USE_STATUS="qq_use_status";
    private static final String QQ_PUTONG_DELAY="qq_putong_delay";
    private static final String QQ_KOULING_DELAY="qq_kouling_delay";
    private static final String QQ_LINGQU_DELAY="qq_lingqu_delay";
    private static final String QQ_HONGBAO_RECORD_TIME="qq_hongbao_record_time";
    private static final String QQ_HONGBAO_RECORD_COUNT="qq_hongbao_record_count";
    private static final String QQ_HONGBAO_RECORD_CACHE="qq_hongbao_record_cache";

    /**
     * 控制支付宝
     */
//    private static final String PREFERENCE_CONTROL ="preference_control";
    private static final String XIUYIXIU_USE_STATUS="zhifubao_use_status";
    private static final String XIUYIXIU_DELAY="xiuyixiu_delay";

    public static SharedPreferences getSharedPreferences(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCE_CONTROL,Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 设置可使用的状态
     * @param value
     */
    public static void setUseStatus(boolean value){
        setBoolean(USE_STATUS,value);
    }

    /**
     * 得到可使用的状态
     * @return
     */
    public static boolean getUsestatus(){
        return getBoolean(USE_STATUS,true);
    }

    public static void setQQUseStatus(boolean value){
        setBoolean(QQ_USE_STATUS, value);
    }

    public static boolean getQQUseStatus(){
        return getBoolean(QQ_USE_STATUS, true);
    }

    public static void setQQPutongDelay(int value){
        setInt(QQ_PUTONG_DELAY, value);
    }

    public static int getQQPutongDelay(){
        return getInt(QQ_PUTONG_DELAY, 1);
    }

    public static void setQQKoulingDelay(int value){
        setInt(QQ_KOULING_DELAY, value);
    }

    public static int getQQKoulingDelay(){
        return getInt(QQ_KOULING_DELAY, 3);
    }

    public static void setQQLingquDelay(int value){
        setInt(QQ_LINGQU_DELAY, value);
    }

    public static int getQQLingquDelay(){
        return getInt(QQ_LINGQU_DELAY, 11);
    }


    //设置QQ红包记录开始时间
    public static void setQQHongbaoRecordTime(String time){
        setString(QQ_HONGBAO_RECORD_TIME, time);
    }

    //得到QQ红包记录开始时间
    public static String getQQHongbaoRecordTime(){
        return getString(QQ_HONGBAO_RECORD_TIME);
    }

    //设置QQ红包记录金额
    public static void setQQHongbaoRecordCount(float count){
        setFloat(QQ_HONGBAO_RECORD_COUNT, count);
    }

    //得到QQ红包记录金额
    public static Float getQQHongbaoRecordCount(){
        return getFloat(QQ_HONGBAO_RECORD_COUNT,0);
    }



    public static void setXiuYiXiuUseStatus(boolean value){
        setBoolean(XIUYIXIU_USE_STATUS, value);
    }

    public static boolean getXiuYiXiuUseStatus(){
        return getBoolean(XIUYIXIU_USE_STATUS, true);
    }

    public static void setXiuYiXiuDelay(int value){
        setInt(QQ_PUTONG_DELAY, value);
    }

    public static int getXiuYiXiuDelay(){
        return getInt(QQ_PUTONG_DELAY, 0);
    }






    public static void setString(String key,String value){
        editor=getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key){
        return getSharedPreferences().getString(key, "");
    }

    public static void setBoolean(String key,boolean value){
        editor=getEditor();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key,boolean value){
        return getSharedPreferences().getBoolean(key, value);
    }

    public static void setInt(String key,int value){
        editor=getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key,int value){
        return getSharedPreferences().getInt(key, value);
    }


    public static void setFloat(String key,float value){
        editor=getEditor();
        editor.putFloat(key,value);
        editor.commit();
    }

    public static float getFloat(String key,float value){
        return getSharedPreferences().getFloat(key, value);
    }

}
