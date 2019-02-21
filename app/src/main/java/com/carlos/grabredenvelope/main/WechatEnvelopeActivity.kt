package com.carlos.grabredenvelope.main

import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.dao.WechatControlVO

/**
 * Created by 小不点 on 2016/5/27.
 */
class WechatEnvelopeActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var mCbWechatControl: CheckBox
    private lateinit var mCbWechatNotificationControl: CheckBox
    private lateinit var mCbWechatChatControl: CheckBox
    private lateinit var mTvWechatPutong: TextView
    private lateinit var mSbWechatPutong: SeekBar
    private lateinit var mTvWechatLingqu: TextView
    private lateinit var mSbWechatLingqu: SeekBar

    private var wechatControlVO = WechatControlVO()
    private var t_putong: Int = 0
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
        mCbWechatControl = findViewById(R.id.cb_qq_control)
        mCbWechatNotificationControl = findViewById(R.id.cb_wechat_notification_control)
        mCbWechatChatControl = findViewById(R.id.cb_wechat_chat_control)

        mTvWechatPutong = findViewById(R.id.tv_qq_putong)
        mSbWechatPutong = findViewById(R.id.sb_qq_putong)
        mTvWechatLingqu = findViewById(R.id.tv_qq_lingqu)
        mSbWechatLingqu = findViewById(R.id.sb_qq_lingqu)

        mCbWechatControl.setOnCheckedChangeListener { buttonView, isChecked ->
            wechatControlVO.isMonitor = isChecked
            RedEnvelopePreferences.wechatControl = wechatControlVO
            LogUtils.d("check---$isChecked")
        }
        mCbWechatNotificationControl.setOnCheckedChangeListener { buttonView, isChecked ->
            wechatControlVO.isMonitorNotification = isChecked
            RedEnvelopePreferences.wechatControl = wechatControlVO
        }
        mCbWechatChatControl.setOnCheckedChangeListener { buttonView, isChecked ->
            wechatControlVO.isMonitorChat = isChecked
            RedEnvelopePreferences.wechatControl = wechatControlVO
        }

        mSbWechatPutong.setOnSeekBarChangeListener(this)
        mSbWechatLingqu.setOnSeekBarChangeListener(this)

    }


    private fun loadSaveData() {
        wechatControlVO = RedEnvelopePreferences.wechatControl
        mCbWechatControl.isChecked = wechatControlVO.isMonitor
        t_putong = wechatControlVO.delayOpenTime
        mTvWechatPutong.text = "领取红包延迟时间：" + t_putong + "s"
        mSbWechatPutong.progress = t_putong - 1

        t_lingqu = wechatControlVO.delayCloseTime
        mSbWechatLingqu.progress = t_lingqu - 3
        if (t_lingqu == 11) {
            mTvWechatLingqu.text = "红包领取页关闭时间：" + "不关闭"
        } else {
            mTvWechatLingqu.text = "红包领取页关闭时间：" + t_lingqu + "s"
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_qq_putong -> {
                t_putong = progress + 1
                mTvWechatPutong.text = "领取红包延迟时间：" + t_putong + "s"
                wechatControlVO.delayOpenTime = t_putong
                RedEnvelopePreferences.wechatControl = wechatControlVO
            }

            R.id.sb_qq_lingqu -> {
                t_lingqu = progress + 3
                if (progress == 8) {
                    mTvWechatLingqu.text = "红包领取页关闭时间：" + "不关闭"
                } else {
                    mTvWechatLingqu.text = "红包领取页关闭时间：" + t_lingqu + "s"
                }
                wechatControlVO.delayCloseTime = t_lingqu
                RedEnvelopePreferences.wechatControl = wechatControlVO

            }
        }
    }


    override fun onResume() {
        super.onResume()
        LogUtils.d("wechatdata:$wechatControlVO")
        LogUtils.d("wechatdata:${RedEnvelopePreferences.wechatControl}")
    }
    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }
}
