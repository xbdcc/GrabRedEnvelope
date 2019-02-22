package com.carlos.grabredenvelope.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.activity.MainActivity
import com.carlos.grabredenvelope.util.PreferencesUtils

/**
 * Created by 小不点 on 2016/2/20.
 */
class XiuYiXiu : MainActivity() {
    private val s_delay = "支付宝咻一咻自动点击延迟时间："
    private var ib_back: ImageButton? = null
    private var cb_xiuyixiu_control: CheckBox? = null
    private var tv_xiuyixiu: TextView? = null
    private var sb_xiuyixiu: SeekBar? = null

    private var time: Double = 0.toDouble()
    private var mark: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.xiuyixiu)

        back()

        cb_xiuyixiu_control = getViewById(R.id.cb_xiuyixiu_control)
        tv_xiuyixiu = getViewById(R.id.tv_xiuyixiu)
        sb_xiuyixiu = getViewById(R.id.sb_xiuyixiu)

        loadSaveData()

        cb_xiuyixiu_control!!.setOnCheckedChangeListener { buttonView, isChecked ->
            PreferencesUtils.xiuYiXiuUseStatus = isChecked
            Log.d(TAG, "check---$isChecked")
        }

        sb_xiuyixiu!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                time = progress * 0.1
                time = Math.floor(time * 10) / 10//保留一位小数
                Log.d(TAG, "progress-->$progress")
                if (progress == 0) {
                    tv_xiuyixiu!!.text = s_delay + "连续"
                } else {
                    tv_xiuyixiu!!.text = s_delay + time + "s"
                }
                PreferencesUtils.xiuYiXiuDelay = progress
                Log.d(TAG, "time---$time")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    fun back() {
        ib_back = findViewById(R.id.ib_back) as ImageButton
        ib_back!!.setOnClickListener { finish() }
    }

    private fun loadSaveData() {
        cb_xiuyixiu_control!!.isChecked = PreferencesUtils.xiuYiXiuUseStatus
        mark = PreferencesUtils.xiuYiXiuDelay
        time = mark * 0.1
        time = Math.floor(time * 10) / 10//保留一位小数
        if (mark == 0) {
            tv_xiuyixiu!!.text = s_delay + "连续"
        } else {
            tv_xiuyixiu!!.text = s_delay + time + "s"
        }
        sb_xiuyixiu!!.progress = mark
        Log.d(TAG, "time---$time")
    }

    // 通过泛型简化findViewById
    internal fun <T : View> getViewById(id: Int): T {
        try {
            return findViewById(id) as T
        } catch (e: ClassCastException) {
            Log.e(TAG, "Could not cast View to create class.", e)
            throw e
        }

    }

    companion object {

        private val TAG = "XiuYiXiu"
    }
}
