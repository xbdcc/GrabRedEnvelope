package com.carlos.grabredenvelope.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.carlos.cutils.thirdparty.AlipayReward
import com.carlos.cutils.thirdparty.WechatReward
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.util.BitmapUtils
import java.io.File

/**
 * Created by Carlos on 2019/2/23.
 */
class RewardActivity : BaseActivity() {

    /**
     * 得到安装路径
     * @return
     */
    internal val filedir: File
        get() {
            val sd = Environment.getExternalStorageDirectory()
            val path = sd.path + "/GrabRedEnvelope"
            val filedir = File(path)
            if (!filedir.exists()) {
                filedir.mkdir()
                Log.e("GrabRedEnvelope", "新建一个文件夹")
            }
            return filedir
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        setMenuTitle("捐赠打赏")

        back()


        val ivAlipay = findViewById<ImageView>(R.id.iv_alipay)
        val ivWechat = findViewById<ImageView>(R.id.iv_wechat)

        ivAlipay.setOnLongClickListener {
            val filedir = filedir
            val output = File(filedir, "xbd_alipay.jpg")
            if (!output.exists()) {
                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.alipay)
                BitmapUtils.saveBitmap(this@RewardActivity, output, bitmap)
            }
            Toast.makeText(this@RewardActivity, "已保存到:" + output.absolutePath, Toast.LENGTH_LONG).show()
            true
        }

        ivWechat.setOnLongClickListener {
            val filedir = filedir
            val output = File(filedir, "xbd_wechat.jpg")
            if (!output.exists()) {
                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.wechat)
                BitmapUtils.saveBitmap(this@RewardActivity, output, bitmap)
            }
            Toast.makeText(this@RewardActivity, "已保存到:" + output.absolutePath, Toast.LENGTH_LONG).show()
            true
        }
    }

    fun alipay(view: View) {
        AlipayReward(this)
    }

    fun wechatPay(view: View) {
        WechatReward(this)
    }
}