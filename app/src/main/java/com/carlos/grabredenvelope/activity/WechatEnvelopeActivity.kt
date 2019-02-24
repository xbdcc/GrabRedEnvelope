package com.carlos.grabredenvelope.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.dao.WechatControlVO
import com.carlos.grabredenvelope.data.RedEnvelopePreferences

/**
 * Created by 小不点 on 2016/5/27.
 */
class WechatEnvelopeActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener {

    private val WECHAT_SERVICE_NAME = "com.carlos.grabredenvelope/.services.WechatService"

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

        setMenuTitle("抢微信红包设置")

        initView()

        loadSaveData()

        addListener()


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
            mCbWechatControl.isChecked = !isChecked
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(this@WechatEnvelopeActivity, "辅助功能找到（抢微信红包）开启或关闭。", Toast.LENGTH_SHORT)
                .show()
        }

        mCbWechatNotificationControl.setOnCheckedChangeListener { buttonView, isChecked ->
            wechatControlVO.isMonitorNotification = isChecked
            RedEnvelopePreferences.wechatControl = wechatControlVO
        }
        mCbWechatChatControl.setOnCheckedChangeListener { buttonView, isChecked ->
            wechatControlVO.isMonitorChat = isChecked
            LogUtils.d("ismotior:" + isChecked)
            RedEnvelopePreferences.wechatControl = wechatControlVO
        }

        mSbWechatPutong.setOnSeekBarChangeListener(this)
        mSbWechatLingqu.setOnSeekBarChangeListener(this)

    }


    private fun loadSaveData() {
        mCbWechatNotificationControl.isChecked = RedEnvelopePreferences.wechatControl.isMonitorNotification
        mCbWechatChatControl.isChecked = RedEnvelopePreferences.wechatControl.isMonitorChat
        LogUtils.d("wechatControl:" + RedEnvelopePreferences.wechatControl.toString())


        wechatControlVO = RedEnvelopePreferences.wechatControl
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

    private fun addListener() {
        addAccessibilityServiceListener(object : AccessibilityServiceListeners {
            override fun updateStatus(boolean: Boolean) {
                updateControlView(boolean)
            }
        }, WECHAT_SERVICE_NAME)
        updateControlView(checkStatus())
    }

    private fun updateControlView(boolean: Boolean) {
        if(boolean) mCbWechatControl.setButtonDrawable(R.mipmap.switch_on)
        else mCbWechatControl.setButtonDrawable(R.mipmap.switch_off)
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
                t_lingqu = progress + 2
                mTvWechatLingqu.text = "红包领取页关闭延迟时间：" + t_lingqu +"s"
                wechatControlVO.delayCloseTime = t_lingqu
                RedEnvelopePreferences.wechatControl = wechatControlVO
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {

    }
}
