package com.carlos.grabredenvelope.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.carlos.grabredenvelope.R;

/**
 * Created by 小不点 on 2016/2/21.
 */
public class DelayedUse {

    private String result;
    private Context context;
    private Handler handler;
    private boolean isStop=false;
    private String message;

    public DelayedUse(Context context){
        this.context=context;
        delayed();
    }

    public void delayed(){
//        Dialog dialog=new Dialog(context,R.style.delayed_dialog_theme);
        Dialog dialog=new Dialog(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.delayed_use,null);
//        dialog.setTitle("gdgd");
        dialog.setContentView(view);
        dialog.show();
    }
}
