package com.carlos.grabredenvelope.services;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.carlos.grabredenvelope.util.PreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class XiuYiXiuService {

    private static final String TAG="HongbaoService";

    private Context context;
    private AccessibilityEvent event;
    private AccessibilityNodeInfo nodeRoot;
    private AccessibilityNodeInfo node;

    //支付宝包名
    private static final String PACKAGE_ALIPAY = "com.eg.android.AlipayGphone";
    //
    private static final String WINDOW_XIUXIU="com.alipay.android.wallet.newyear.activity.MonkeyYearActivity";
    //是否能够不停点击咻咻的开关
    private boolean isCanCyclingClick = false;
    private static final int MSG_NODE_CLICK = 0x110;
    private Timer timer;//定时器



    public XiuYiXiuService(Context context, AccessibilityEvent event, AccessibilityNodeInfo nodeRoot){
        this.context=context;
        this.event=event;
        this.nodeRoot=nodeRoot;

        method();
    }

    public void method(){

        if(event.getPackageName().equals(PACKAGE_ALIPAY)){
//            Log.d(TAG,"全局"+event.getEventType()+","+event.getClassName());
            if(event.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
                //进入咻一咻界面
                if(event.getClassName().equals(WINDOW_XIUXIU)){
                    Log.i(TAG,"进入咻咻界面");

                    Log.i(TAG,"------"+nodeRoot);
                    node=nodeRoot;

                    Log.i(TAG,"----------"+node.getChildCount());

                    for(int i=0;i<node.getChildCount();i++){
                        AccessibilityNodeInfo n=node.getChild(i);
                        Log.i(TAG,"---------"+n);
                    }


                    System.out.println("支付宝自动咻咻:" + getEventName(event.getEventType()) + "," + event.getClassName());
                    isCanCyclingClick=true;
                    AccessibilityNodeInfo btn=getButtonInfo(nodeRoot);
                    if(btn!=null){
                        dontStopClick(btn);
                    }
                }
            }
        }
    }

    private String getEventName(int type){
        switch (type){
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                return "窗口内容改变";
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                return "状态改变";
        }
        return "";
    }

    //筛选出咻咻的button，进行不停的点击
    private AccessibilityNodeInfo getButtonInfo(AccessibilityNodeInfo parent){
        if(parent!=null&&parent.getChildCount()>0){
            for(int i=0;i<parent.getChildCount();i++){
                AccessibilityNodeInfo node=parent.getChild(i);
                if("android.widget.Button".equals(node.getClassName())){
                    Log.i(TAG,"找到按钮");
                    Log.d(TAG, "按钮属性--->" + node);
//                    node.setClickable(true);
//                    Log.d(TAG,"按钮属性--->"+node);
                    return node;
                }
            }
        }
        return null;
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==MSG_NODE_CLICK){
                AccessibilityNodeInfo btnNode= (AccessibilityNodeInfo) msg.obj;
                btnNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    };

    private void dontStopClick(final AccessibilityNodeInfo btn){
        if(timer==null){
            timer=new Timer();
            Log.d(TAG,"定时点击");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isCanCyclingClick) {
//                        Log.d(TAG,"点击中");
                        Message m = mHandler.obtainMessage(MSG_NODE_CLICK, btn);
                        mHandler.sendMessage(m);
                    }
                }
            }, 100, delayedTime());//100，100  0.1秒开始，延迟0.1秒点击

        }
    }

    public int delayedTime(){
        return PreferencesUtils.getXiuYiXiuDelay()*100;
    }

}
