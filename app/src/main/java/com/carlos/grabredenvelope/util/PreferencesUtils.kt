package com.carlos.grabredenvelope.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by 小不点 on 2016/2/19.
 */
object PreferencesUtils {

    private val context = MyApplication.instance.applicationContext
    private var editor: SharedPreferences.Editor? = null

    /**
     * 控制QQ
     */
    private val PREFERENCE_CONTROL = "preference_control"
    private val USE_STATUS = "use_status"
    private val QQ_USE_STATUS = "qq_use_status"
    private val QQ_PUTONG_DELAY = "qq_putong_delay"
    private val QQ_KOULING_DELAY = "qq_kouling_delay"
    private val QQ_LINGQU_DELAY = "qq_lingqu_delay"
    private val QQ_HONGBAO_RECORD_TIME = "qq_hongbao_record_time"
    private val QQ_HONGBAO_RECORD_COUNT = "qq_hongbao_record_count"
    private val QQ_HONGBAO_RECORD_CACHE = "qq_hongbao_record_cache"

    /**
     * 控制支付宝
     */
    //    private static final String PREFERENCE_CONTROL ="preference_control";
    private val XIUYIXIU_USE_STATUS = "zhifubao_use_status"
    private val XIUYIXIU_DELAY = "xiuyixiu_delay"

    val sharedPreferences: SharedPreferences
        get() = context!!.getSharedPreferences(PREFERENCE_CONTROL, Context.MODE_PRIVATE)

    /**
     * 得到可使用的状态
     * @return
     */
    val usestatus: Boolean
        get() = getBoolean(USE_STATUS, true)

    var qqUseStatus: Boolean
        get() = getBoolean(QQ_USE_STATUS, true)
        set(value) = setBoolean(QQ_USE_STATUS, value)

    var qqPutongDelay: Int
        get() = getInt(QQ_PUTONG_DELAY, 1)
        set(value) = setInt(QQ_PUTONG_DELAY, value)

    var qqKoulingDelay: Int
        get() = getInt(QQ_KOULING_DELAY, 3)
        set(value) = setInt(QQ_KOULING_DELAY, value)

    var qqLingquDelay: Int
        get() = getInt(QQ_LINGQU_DELAY, 11)
        set(value) = setInt(QQ_LINGQU_DELAY, value)

    //得到QQ红包记录开始时间
    //设置QQ红包记录开始时间
    var qqHongbaoRecordTime: String
        get() = getString(QQ_HONGBAO_RECORD_TIME)
        set(time) = setString(QQ_HONGBAO_RECORD_TIME, time)

    //得到QQ红包记录金额
    val qqHongbaoRecordCount: Float
        get() = getFloat(QQ_HONGBAO_RECORD_COUNT, 0f)

    var xiuYiXiuUseStatus: Boolean
        get() = getBoolean(XIUYIXIU_USE_STATUS, true)
        set(value) = setBoolean(XIUYIXIU_USE_STATUS, value)

    var xiuYiXiuDelay: Int
        get() = getInt(QQ_PUTONG_DELAY, 0)
        set(value) = setInt(QQ_PUTONG_DELAY, value)

    fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    /**
     * 设置可使用的状态
     * @param value
     */
    fun setUseStatus(value: Boolean) {
        setBoolean(USE_STATUS, value)
    }

    //设置QQ红包记录金额
    fun setQQHongbaoRecordCount(count: Float) {
        setFloat(QQ_HONGBAO_RECORD_COUNT, count)
    }


    fun setString(key: String, value: String) {
        editor = getEditor()
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")
    }

    fun setBoolean(key: String, value: Boolean) {
        editor = getEditor()
        editor!!.putBoolean(key, value)
        editor!!.commit()
    }

    fun getBoolean(key: String, value: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, value)
    }

    fun setInt(key: String, value: Int) {
        editor = getEditor()
        editor!!.putInt(key, value)
        editor!!.commit()
    }

    fun getInt(key: String, value: Int): Int {
        return sharedPreferences.getInt(key, value)
    }


    fun setFloat(key: String, value: Float) {
        editor = getEditor()
        editor!!.putFloat(key, value)
        editor!!.commit()
    }

    fun getFloat(key: String, value: Float): Float {
        return sharedPreferences.getFloat(key, value)
    }

}
