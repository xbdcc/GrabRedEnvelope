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
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

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
