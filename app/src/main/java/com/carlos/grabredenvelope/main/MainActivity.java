package com.carlos.grabredenvelope.main;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.util.ControlUse;
import com.carlos.grabredenvelope.util.ToastUtils;
import com.carlos.grabredenvelope.util.Update;
import com.carlos.grabredenvelope.util.Utility;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 小不点 on 2016/2/14.
 */
public class MainActivity extends Activity implements AccessibilityManager.AccessibilityStateChangeListener{

    private static final String TAG="MainActivity---";

    private ListView listView;
    private ArrayList<String> list;
    private Intent intent;
    private ArrayAdapter<String> adapter;

    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ControlUse controlUse=new ControlUse(MainActivity.this);
        if(controlUse.stopUse()){
            show_dialog();
        }

        Update update=new Update(MainActivity.this,1);
        update.update();
//        if(update.hasNewVersion()){
//            ControlUse.isStop=true;
//        }
//        new DelayedUse(MainActivity.this);

        Log.d(TAG,"oncreate");
        //监听AccessibilityService 变化
        accessibilityManager= (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);

//        Update update=new Update(MainActivity.this,1);
//        update.update();

        listView= (ListView) findViewById(R.id.listview);
        list=new ArrayList<>();
        list.add(s(R.string.menu_service));
        list.add(s(R.string.menu_hongbao_record));
        list.add(s(R.string.menu_qq_hongbao));
        list.add(s(R.string.menu_weixin_hongbao));
        list.add(s(R.string.menu_zhifubao_xiuyixiu));
        list.add(s(R.string.menu_qq_shuayishua));
        list.add(s(R.string.about));
        list.add(s(R.string.advice));
        list.add(s(R.string.update));
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent=new Intent();
                switch (position){
                    case 0:
                        intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                        if(isServiceEnabled()){
                            Toast.makeText(MainActivity.this, "找到抢红包，然后关闭服务。", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "找到抢红包，然后开启服务。", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        intent.setClass(MainActivity.this,HongBaoRecrodActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(MainActivity.this,QQHongBao.class);
                        startActivity(intent);
                        break;
                    case 3:
                        ToastUtils.showToast(MainActivity.this,"待完善");
                        break;
                    case 4:
                        intent.setClass(MainActivity.this, XiuYiXiu.class);
                        startActivity(intent);
                        break;
                    case 5:
                        ToastUtils.showToast(MainActivity.this,"待开发");
                        break;
                    case 6:
                        ToastUtils.showToast(getApplicationContext(),"关于");
                        intent.setClass(MainActivity.this, About.class);
                        startActivity(intent);
                        break;
                    case 7:
                        ToastUtils.showToast(MainActivity.this,"待开发");
                        break;
                    case 8:
                        Update update=new Update(MainActivity.this,2);
                        update.update();
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //监听AccessibilityService 变化
        updateServiceStatus();
        adapter.notifyDataSetChanged();
        Log.d(TAG, "--->onResume");
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    private String s(int id){
        return getResources().getString(id);
    }


        private void show_dialog() {
        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 自动生成的方法存根
                        finish();
                    }
                })
                .create();
//		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * 更新当前 QiangHongBaoService 显示状态
     */
    private void updateServiceStatus(){
        if(isServiceEnabled()){
            list.set(0, "关闭服务");
//            Log.d(TAG,"关闭服务");
        }else{
            list.set(0, "开启服务");
//            Log.d(TAG,"开启服务");
        }
    }

    /**
     * 获取 QiangHongBaoService 是否启用状态
     * @return
     */
    private boolean isServiceEnabled(){
        List<AccessibilityServiceInfo> accessibilityServiceInfoList=accessibilityManager.
                getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for(AccessibilityServiceInfo info:accessibilityServiceInfoList){
            if(info.getId().equals(getPackageName()+"/.services.QiangHongBaoService")){
                Log.d(TAG,"ture");
                return true;
            }
        }
        Log.d(TAG,"false");
        return  false;
    }

    @Override
    protected void onDestroy() {
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
        Log.d(TAG, "ondestroy");
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
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
