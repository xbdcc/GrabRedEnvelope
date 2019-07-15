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
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

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
        mCbWechatNotificationControl.isChecked =
            RedEnvelopePreferences.wechatControl.isMonitorNotification
        mCbWechatChatControl.isChecked = RedEnvelopePreferences.wechatControl.isMonitorChat
        LogUtils.d("wechatControl:" + RedEnvelopePreferences.wechatControl.toString())

        wechatControlVO = RedEnvelopePreferences.wechatControl
        t_putong = wechatControlVO.delayOpenTime
        mTvWechatPutong.text = "领取红包延迟时间：" + t_putong + "s"
        mSbWechatPutong.progress = t_putong

        t_lingqu = wechatControlVO.delayCloseTime
        mSbWechatLingqu.progress = t_lingqu
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
        if (boolean) mCbWechatControl.setButtonDrawable(R.mipmap.switch_on)
        else mCbWechatControl.setButtonDrawable(R.mipmap.switch_off)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.sb_qq_putong -> {
                LogUtils.d("sb_qq_putong:$progress")
                t_putong = progress
                mTvWechatPutong.text = "领取红包延迟时间：" + t_putong + "s"
                wechatControlVO.delayOpenTime = t_putong
                RedEnvelopePreferences.wechatControl = wechatControlVO
            }

            R.id.sb_qq_lingqu -> {
                LogUtils.d("sb_qq_lingqu:$progress")
                t_lingqu = progress
                mTvWechatLingqu.text = "红包领取页关闭延迟时间：" + t_lingqu + "s"
                if (t_lingqu == 11) {
                    mTvWechatLingqu.text = "红包领取页关闭时间：" + "不关闭"
                }
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
