package com.carlos.grabredenvelope.activity

import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import com.bugtags.library.Bugtags
import com.carlos.cutils.base.CBaseAccessibilityActivity
import com.carlos.grabredenvelope.R
import com.umeng.analytics.MobclickAgent

/**
 * Created by 小不点 on 2016/5/27.
 */
open class BaseActivity : CBaseAccessibilityActivity() {

    private lateinit var mBack: ImageButton
    private lateinit var tv_title: TextView

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
        Bugtags.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
        Bugtags.onPause(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Bugtags.onDispatchTouchEvent(this, event)
        return super.dispatchTouchEvent(event)
    }

    fun back() {
        mBack = findViewById(R.id.ib_back)
        mBack.setOnClickListener { v -> finish() }
        mBack.setOnClickListener { finish() }
    }

    fun setMenuTitle(title: String) {
        tv_title = findViewById(R.id.tv_title)
        tv_title.text = title
    }

}
