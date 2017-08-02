package com.carlos.grabredenvelope.util;

import android.content.Context;
import android.widget.Toast;

import com.carlos.grabredenvelope.R;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class ToastUtils {

    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context,int resId){
        Toast.makeText(context,resId, Toast.LENGTH_SHORT).show();
    }

    public static void showNoNet(Context context){
        showToast(context, R.string.no_net);
    }
    public static void showError(Context context){
        showToast(context,R.string.unkonown_error);
    }
}
