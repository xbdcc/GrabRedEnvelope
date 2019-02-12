package com.carlos.grabredenvelope.main

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import cn.jpush.android.api.JPushInterface
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.util.*
import java.util.*

/**
 * Created by 小不点 on 2016/2/14.
 */
open class MainActivity : Activity(), AccessibilityManager.AccessibilityStateChangeListener {

    private var listView: ListView? = null
    private var list: ArrayList<String>? = null
    private lateinit var cIntent: Intent
    private var adapter: ArrayAdapter<String>? = null

    //AccessibilityService 管理
    private var accessibilityManager: AccessibilityManager? = null

    /**
     * 获取 QiangHongBaoService 是否启用状态
     * @return
     */
    private val isServiceEnabled: Boolean
        get() {
            val accessibilityServiceInfoList = accessibilityManager!!.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
            for (info in accessibilityServiceInfoList) {
                if (info.id == "$packageName/.services.QiangHongBaoService") {
                    Log.d(TAG, "ture")
                    return true
                }
            }
            Log.d(TAG, "false")
            return false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val controlUse = ControlUse(this@MainActivity)
        if (controlUse.stopUse()) {
            show_dialog()
        }

        val update = Update(this@MainActivity, 1)
        update.update()
        //        if(update.hasNewVersion()){
        //            ControlUse.isStop=true;
        //        }
        //        new DelayedUse(MainActivity.this);

        Log.d(TAG, "oncreate")
        //监听AccessibilityService 变化
        accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        accessibilityManager!!.addAccessibilityStateChangeListener(this)

        //        Update update=new Update(MainActivity.this,1);
        //        update.update();

        listView = findViewById(R.id.listview) as ListView
        list = ArrayList()
        list!!.add(s(R.string.menu_service))
        list!!.add(s(R.string.menu_hongbao_record))
        list!!.add(s(R.string.menu_qq_hongbao))
        list!!.add(s(R.string.menu_weixin_hongbao))
        list!!.add(s(R.string.menu_zhifubao_xiuyixiu))
        list!!.add(s(R.string.menu_qq_shuayishua))
        list!!.add(s(R.string.about))
        list!!.add(s(R.string.advice))
        list!!.add(s(R.string.update))
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list!!)
        listView!!.adapter = adapter
        Utility.setListViewHeightBasedOnChildren(listView!!)

        listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            cIntent = Intent()
            when (position) {
                0 -> {
                    cIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    startActivity(cIntent)


                    if (isServiceEnabled) {
                        Toast.makeText(this@MainActivity, "找到抢红包，然后关闭服务。", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "找到抢红包，然后开启服务。", Toast.LENGTH_SHORT).show()
                    }
                }
                1 -> {
                    cIntent!!.setClass(this@MainActivity, HongBaoRecrodActivity::class.java)
                    startActivity(cIntent)
                }
                2 -> {
                    cIntent!!.setClass(this@MainActivity, QQHongBao::class.java)
                    startActivity(cIntent)
                }
                3 -> ToastUtils.showToast(this@MainActivity, "待完善")
                4 -> {
                    cIntent!!.setClass(this@MainActivity, XiuYiXiu::class.java)
                    startActivity(cIntent)
                }
                5 -> ToastUtils.showToast(this@MainActivity, "待开发")
                6 -> {
                    val registrationId = JPushInterface.getRegistrationID(this)
                    Log.e("1099", "run:--------->registrationId： $registrationId")
                    LogUtil.d("1099"+"run:--------->registrationId： $registrationId")

                    JPushInterface.setAlias(this, 1,"xbd")

                    ToastUtils.showToast(applicationContext, "关于")
                    cIntent!!.setClass(this@MainActivity, About::class.java)
                    startActivity(cIntent)
                }
                7 -> ToastUtils.showToast(this@MainActivity, "待开发")
                8 -> {
                    val update = Update(this@MainActivity, 2)
                    update.update()
                }
            }
        }

        setAlias()
    }

    private val  MSG_SET_ALIAS = 1001
    private fun setAlias() {

//        val bundle = intent.getExtras();
//        val title = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID)
//        LogUtil.d("title:"+title)

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "xbd"))
    }



    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            JPushInterface.setAliasAndTags(getApplicationContext(),
                    "xbd",
                    null,
                    null)
            LogUtil.d("TAG:"+msg.what+"-" + msg.data)
        }
    }

//    private val mAliasCallback = TagAliasCallback { code, alias, tags ->
//        val logs: String
//        when (code) {
//            0 -> {
//                logs = "Set tag and alias success"
//                Log.i(ContentValues.TAG, logs)
//            }
//            6002 -> {
//                logs = "Failed to set alias and tags due to timeout. Try again after 60s."
//                Log.i(ContentValues.TAG, logs)
//                // 延迟 60 秒来调用 Handler 设置别名
//                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60)
//            }
//            else -> {
//                logs = "Failed with errorCode = $code"
//                Log.e(ContentValues.TAG, logs)
//            }
//        }// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//    }


    override fun onResume() {
        super.onResume()
        //监听AccessibilityService 变化
        updateServiceStatus()
        adapter!!.notifyDataSetChanged()
        Log.d(TAG, "--->onResume")
        JPushInterface.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JPushInterface.onPause(this)
    }

    private fun s(id: Int): String {
        return resources.getString(id)
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

    /**
     * 更新当前 QiangHongBaoService 显示状态
     */
    private fun updateServiceStatus() {
        if (isServiceEnabled) {
            list!![0] = "关闭服务"
            //            Log.d(TAG,"关闭服务");
        } else {
            list!![0] = "开启服务"
            //            Log.d(TAG,"开启服务");
        }
    }

    override fun onDestroy() {
        accessibilityManager!!.removeAccessibilityStateChangeListener(this)
        super.onDestroy()
        Log.d(TAG, "ondestroy")
    }

    override fun onAccessibilityStateChanged(enabled: Boolean) {
        updateServiceStatus()
    }

    companion object {

        private val TAG = "MainActivity---"
    }


    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
    //            long exitTime=0;
    //            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
    //            {
    //                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
    //                exitTime = System.currentTimeMillis();
    //            } else {
    //                finish();
    //                System.exit(0);
    //            }
    //
    //            return true;
    //        }
    //        return super.onKeyDown(keyCode, event);
    //    }
}
