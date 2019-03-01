package com.carlos.grabredenvelope.demo

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import com.carlos.cutils.base.CBaseAccessibilityActivity

class MainActivity : CBaseAccessibilityActivity() {

    private val WECHAT_SERVICE_NAME = "com.carlos.grabredenvelope.demo/.WechatService"
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        addAccessibilityServiceListener(object : AccessibilityServiceListeners {
            override fun updateStatus(boolean: Boolean) {
                if (boolean) textView.text = "点击关闭服务"
                else textView.text = "点击开启服务"
            }

        }, WECHAT_SERVICE_NAME)

        updateControlView(checkStatus())

    }

    private fun initView() {
        textView = findViewById(R.id.textView)
        textView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(this@MainActivity, "辅助功能找到（GrabRedEnvelope demo）开启或关闭。", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun updateControlView(boolean: Boolean) {
        if (boolean) textView.text = "点击关闭服务"
        else textView.text = "点击开启服务"
    }

}
