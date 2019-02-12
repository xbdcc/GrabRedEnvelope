package com.carlos.grabredenvelope.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/5/27.
 */
open class BaseActivity : Activity() {

    private var mBack: ImageView? = null
    private var tv_title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun back() {
        mBack = getViewById(R.id.ib_back)
        mBack!!.setOnClickListener { v -> finish() }
        mBack!!.setOnClickListener { finish() }

    }

    fun setMenuTitle(title: String) {
        tv_title = findViewById(R.id.tv_title) as TextView
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

        private val TAG = "BaseActivity"
    }

}
