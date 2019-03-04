package com.carlos.grabredenvelope.old2016

import android.content.Context
import android.widget.Toast

import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/2/20.
 */
object ToastUtils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showToast(context: Context, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    fun showNoNet(context: Context) {
        showToast(context, R.string.no_net)
    }

    fun showError(context: Context) {
        showToast(context, R.string.unkonown_error)
    }
}
