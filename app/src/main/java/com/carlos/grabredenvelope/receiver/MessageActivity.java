package com.carlos.grabredenvelope.receiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.main.MainActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 小不点 on 2016/2/24.
 */
public class MessageActivity extends Activity{

    private ImageButton ib_back;
    private TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message);

        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_message= (TextView) findViewById(R.id.tv_message);

        Intent intent=getIntent();
        if(null!=intent){
            Bundle bundle=getIntent().getExtras();
            tv_message.setText("\t\t"+bundle.getString(JPushInterface.EXTRA_ALERT));
        }


//        TextView tv = new TextView(this);
//        tv.setText("用户自定义打开的Activity");
//        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle = getIntent().getExtras();
//            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//            tv.setText("Title : " + title + "  " + "Content : " + content);
//        }
//        addContentView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
}
