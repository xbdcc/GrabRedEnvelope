package com.carlos.grabredenvelope.main

import android.widget.ImageButton
import android.widget.TextView
import com.carlos.cutils.activity.CBaseActivity
import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/5/27.
 */
open class BaseActivity : CBaseActivity() {

    private lateinit var mBack: ImageButton
    private lateinit var tv_title: TextView

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
