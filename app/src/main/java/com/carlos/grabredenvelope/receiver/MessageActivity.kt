package com.carlos.grabredenvelope.receiver

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.activity.MainActivity

/**
 * Created by 小不点 on 2016/2/24.
 */
class MessageActivity : Activity() {

    private var ib_back: ImageButton? = null
    private var tv_message: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.message)

        ib_back = findViewById(R.id.ib_back) as ImageButton
        ib_back!!.setOnClickListener {
            val intent = Intent(this@MessageActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tv_message = findViewById(R.id.tv_message) as TextView

        val intent = intent
        if (null != intent) {
            val bundle = getIntent().extras
            tv_message!!.text = "\t\t" + bundle!!.getString(JPushInterface.EXTRA_ALERT)!!
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
