package com.carlos.grabredenvelope.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import cn.jpush.android.api.JPushInterface
import com.carlos.cutils.listener.PermissionListener
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.data.RedEnvelopePreferences
import com.carlos.grabredenvelope.old2016.About
import com.carlos.grabredenvelope.old2016.ToastUtils
import com.carlos.grabredenvelope.old2016.Update
import com.carlos.grabredenvelope.old2016.Utility
import com.carlos.grabredenvelope.util.ControlUse

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
 * Created by 小不点 on 2016/2/14.
 */
open class MainActivity : BaseActivity() {

    private lateinit var listView: ListView
    private lateinit var list: Array<String>
    private lateinit var cIntent: Intent
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val controlUse = ControlUse(this)
        if (controlUse.stopUse()) {
            show_dialog()
        }

        val update = Update(this, 1)
        update.update()
//        val check_update = "https://api.github.com/repos/xbdcc/GrabRedEnvelope/releases/latest"
//        val check_update = "https://xbdcc.cn/GrabRedEnvelope/update.xml"
//        UpdateDefaultImpl().updateDefault(this,check_update)

        Log.d(TAG, "oncreate")

        listView = findViewById(R.id.listview)
        list = resources.getStringArray(R.array.list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
        Utility.setListViewHeightBasedOnChildren(listView)

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                cIntent = Intent()
                when (position) {
                    0 -> {
                        startActivity(
                            cIntent.setClass(
                                this@MainActivity, WechatEnvelopeActivity::class.java
                            )
                        )

                    }
                    1 -> {

                        ToastUtils.showToast(applicationContext, "关于")
                        startActivity(
                            cIntent.setClass(
                                this@MainActivity,
                                AboutActivity::class.java
                            )
                        )
                    }
                    2 -> {
                        startActivity(
                            cIntent.setClass(
                                this@MainActivity, GithubIssuesActivity::class.java
                            )
                        )

                    }
                    3 -> {
                        val update = Update(this, 2)
                        update.update()
                    }

                    4 -> {
                        startActivity(
                            cIntent.setClass(
                                this@MainActivity, RewardActivity::class.java
                            )
                        )

//                        cIntent.setClass(this@MainActivity, XiuYiXiu::class.java)
//                        startActivity(cIntent)
                    }
                    5 -> ToastUtils.showToast(this@MainActivity, "待开发")
                    6 -> {
                        val registrationId = JPushInterface.getRegistrationID(this)
                        LogUtils.d("1099" + "run:--------->registrationId： $registrationId")

                        JPushInterface.setAlias(this, 1, "xbd")

                        ToastUtils.showToast(applicationContext, "关于")
                        cIntent.setClass(this@MainActivity, About::class.java)
                        startActivity(cIntent)
                    }
                }
            }

    }

    override fun onResume() {
        super.onResume()
        //监听AccessibilityService 变化
//        updateServiceStatus()
        adapter!!.notifyDataSetChanged()
        Log.d(TAG, "--->onResume")
        JPushInterface.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JPushInterface.onPause(this)
    }

    private fun show_dialog() {
        val dialog = AlertDialog.Builder(this).setTitle("提示")
            .setMessage("本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！").setCancelable(false)
            .setPositiveButton("确定") { dialog, which ->
                // TODO 自动生成的方法存根
                finish()
            }
            .create()
        //		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show()
    }


}
