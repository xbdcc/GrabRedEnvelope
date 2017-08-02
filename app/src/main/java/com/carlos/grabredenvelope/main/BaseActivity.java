package com.carlos.grabredenvelope.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlos.grabredenvelope.R;

/**
 * Created by 小不点 on 2016/5/27.
 */
public class BaseActivity extends Activity {

    private static final String TAG="BaseActivity";

    private ImageView mBack;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void back(){
        mBack=getViewById(R.id.ib_back);
        mBack.setOnClickListener(v->finish());
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setMenuTitle(String title){
        tv_title= (TextView) findViewById(R.id.tv_title);
    }

    // 通过泛型简化findViewById
    @SuppressWarnings("unchecked")
    <T extends View> T getViewById(int id) {
        try {
            return (T) findViewById(id);
        } catch (ClassCastException e) {
            Log.e(TAG, "Could not cast View to create class.", e);
            throw e;
        }
    }

}
