package com.carlos.grabredenvelope.activity

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import com.carlos.cutils.base.activity.CBaseAccessibilityActivity
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.util.ControlUse
import com.umeng.analytics.MobclickAgent

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
 * Github: https://github.com/xbdcc/.
 * Created by 小不点 on 2016/5/27.
 */
open class BaseActivity : CBaseAccessibilityActivity() {

    private lateinit var mBack: ImageButton
    private lateinit var tv_title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controlUse()
    }

    override fun onResume() {
        super.onResume()
        JPushInterface.onResume(this)
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JPushInterface.onPause(this)
        MobclickAgent.onPause(this)
    }

    fun back() {
        mBack = findViewById(R.id.ib_back)
        mBack.setOnClickListener { finish() }
    }

    fun setMenuTitle(title: String) {
        tv_title = findViewById(R.id.tv_title)
        tv_title.text = title
    }

    private fun controlUse() {
        val controlUse = ControlUse(this)
        if (controlUse.stopUse()) {
            val dialog = AlertDialog.Builder(this).setTitle("提示")
                .setMessage("本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！").setCancelable(false)
                .setPositiveButton("确定") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            //		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show()
        }
    }

}
