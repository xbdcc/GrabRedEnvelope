package com.carlos.grabredenvelope.main

import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.util.PreferencesUtils

/**
 * Created by 小不点 on 2016/5/27.
 */
class WechatEnvelopeActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var mCbQqControl: CheckBox
    private lateinit var mTvQqPutong: TextView
    private lateinit var mSbQqPutong: SeekBar
    private lateinit var mTvQqKouling: TextView
    private lateinit var mSbQqKouling: SeekBar
    private lateinit var mTvQqLingqu: TextView
    private lateinit var mSbQqLingqu: SeekBar

    private var t_putong: Int = 0
    private var t_kouling: Int = 0
    private var t_lingqu: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_wechat_envelope)

        back()

        initView()

        loadSaveData()
    }
    
    private fun initView() {
        mCbQqControl = findViewById(R.id.cb_qq_control)
        mTvQqPutong = findViewById(R.id.tv_qq_putong)
        mSbQqPutong = findViewById(R.id.sb_qq_putong)
        mTvQqKouling = findViewById(R.id.tv_qq_kouling)
        mSbQqKouling = findViewById(R.id.sb_qq_kouling)
        mTvQqLingqu = findViewById(R.id.tv_qq_lingqu)
        mSbQqLingqu = findViewById(R.id.sb_qq_lingqu)

        mCbQqControl.setOnCheckedChangeListener { buttonView, isChecked ->
            PreferencesUtils.qqUseStatus = isChecked
            LogUtils.d("check---$isChecked")
        }

        mSbQqPutong.setOnSeekBarChangeListener(this)
        mSbQqKouling.setOnSeekBarChangeListener(this)
        mSbQqLingqu.setOnSeekBarChangeListener(this)
    }


    private fun loadSaveData() {
        mCbQqControl.isChecked = PreferencesUtils.qqUseStatus
        t_putong = PreferencesUtils.qqPutongDelay
        mTvQqPutong.text = "普通红包延迟时间：" + t_putong + "s"
        mSbQqPutong.progress = t_putong - 1

        t_kouling = PreferencesUtils.qqKoulingDelay
        mTvQqKouling.text = "口令红包延迟时间：" + t_kouling + "s"
        mSbQqKouling.progress = t_kouling - 3

        t_lingqu = PreferencesUtils.qqLingquDelay
        mSbQqLingqu.progress = t_lingqu - 3
        if (t_lingqu == 11) {
            mTvQqLingqu.text = "红包领取页关闭时间：" + "不关闭"
        } else {
            mTvQqLingqu.text = "红包领取页关闭时间：" + t_lingqu + "s"
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_qq_putong -> {
                t_putong = progress + 1
                mTvQqPutong.text = "普通红包延迟时间：" + t_putong + "s"
                PreferencesUtils.qqPutongDelay = t_putong
            }
            R.id.sb_qq_kouling -> {
                t_kouling = progress + 3
                mTvQqKouling.text = "口令红包延迟时间：" + t_kouling + "s"
                PreferencesUtils.qqKoulingDelay = t_kouling
            }
            R.id.sb_qq_lingqu -> {
                t_lingqu = progress + 3
                if (progress == 8) {
                    mTvQqLingqu.text = "红包领取页关闭时间：" + "不关闭"
                } else {
                    mTvQqLingqu.text = "红包领取页关闭时间：" + t_lingqu + "s"
                }
                PreferencesUtils.qqLingquDelay = t_lingqu
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }
}
