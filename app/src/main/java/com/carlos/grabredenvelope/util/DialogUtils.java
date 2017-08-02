package com.carlos.grabredenvelope.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.carlos.grabredenvelope.R;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class DialogUtils {

    /**
     * 对话框点击确定按钮的接口
     * @author xiaobudian
     *
     */
    public interface OnClickSureListener{
        abstract void onClickSure();
    }

    /**
     * 对话框点击取消的接口
     * @author xiaobudian
     *
     */
    public interface OnClickCancelListener{
        abstract void onClickCancel();
    }

    /**
     * 显示AlertDialog对话框
     * @param context
     * @param title
     * @param message
     * @param sureListener
     * @param cancelListener
     */
    public static void showAlertDialog(Context context,String title,String message,
                                       final OnClickSureListener sureListener,final OnClickCancelListener cancelListener){
        AlertDialog dialog=new AlertDialog.Builder(context).setTitle(title).setMessage(message).
                setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sureListener.onClickSure();
                    }
                }).
                setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelListener.onClickCancel();
                    }
                }).
                create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 返回键不取消，显示之上。
     * @param context
     * @param titile
     * @param message
     */
    public static void showDialogNotCancel(final Context context,String titile,String message,final OnClickSureListener
            sureListener,final OnClickCancelListener cancelListener){
        AlertDialog dialog=new AlertDialog.Builder(context).setTitle(titile)
                .setMessage(message).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sureListener.onClickSure();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelListener.onClickCancel();
                    }
                }).create();

        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public static void show_dialog(Context context,String title,String message) {
        AlertDialog dialog=new AlertDialog.Builder(context).setTitle(title)
                .setMessage(message).setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .create();

		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
