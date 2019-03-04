package com.carlos.grabredenvelope.old2016

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/2/21.
 */
class DelayedUse(private val context: Context) {

    private val result: String? = null
    private val handler: Handler? = null
    private val isStop = false
    private val message: String? = null

    init {
        delayed()
    }

    fun delayed() {
        //        Dialog dialog=new Dialog(context,R.style.delayed_dialog_theme);
        val dialog = Dialog(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.delayed_use, null)
        //        dialog.setTitle("gdgd");
        dialog.setContentView(view)
        dialog.show()
    }
}
