package com.carlos.grabredenvelope.util

import android.app.AlertDialog
import android.content.Context
import android.view.WindowManager
import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/2/20.
 */
object DialogUtils {

    /**
     * 对话框点击确定按钮的接口
     * @author xiaobudian
     */
    interface OnClickSureListener {
        fun onClickSure()
    }

    /**
     * 对话框点击取消的接口
     * @author xiaobudian
     */
    interface OnClickCancelListener {
        fun onClickCancel()
    }

    /**
     * 显示AlertDialog对话框
     * @param context
     * @param title
     * @param message
     * @param sureListener
     * @param cancelListener
     */
    fun showAlertDialog(context: Context, title: String, message: String,
                        sureListener: OnClickSureListener, cancelListener: OnClickCancelListener) {
        val dialog = AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(R.string.sure) { arg0, arg1 -> sureListener.onClickSure() }.setNegativeButton(R.string.cancel) { arg0, arg1 -> cancelListener.onClickCancel() }.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    /**
     * 返回键不取消，显示之上。
     * @param context
     * @param titile
     * @param message
     */
    fun showDialogNotCancel(context: Context, titile: String, message: String, sureListener: OnClickSureListener, cancelListener: OnClickCancelListener) {
        val dialog = AlertDialog.Builder(context).setTitle(titile)
                .setMessage(message).setCancelable(false)
                .setPositiveButton("确定") { dialog, which -> sureListener.onClickSure() }
                .setNegativeButton("取消") { dialog, which -> cancelListener.onClickCancel() }.create()

        dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }

    fun show_dialog(context: Context, title: String, message: String?) {
        val dialog = AlertDialog.Builder(context).setTitle(title)
                .setMessage(message).setCancelable(false)
                .setPositiveButton("确定") { dialog, which -> System.exit(0) }
                .create()

        dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }
}
