package com.carlos.grabredenvelope.services;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.main.MainActivity;
import com.carlos.grabredenvelope.util.ControlUse;
import com.carlos.grabredenvelope.util.LogUtil;
import com.carlos.grabredenvelope.util.MyApplication;
import com.carlos.grabredenvelope.util.PreferencesUtils;

/**
 * Created by 小不点 on 2016/2/5.
 */
public class QiangHongBaoService extends AccessibilityService {

    private static final String TAG="QiangHongBaoService";
    private static final String TAG_SHUA="ShuaYiSHua";
    public  boolean isStopUse;

//
//    private static final String PACKAGE_QQ = "com.tencent.mobileqq";//QQ包名
//    private static final String CLASS_QQ_LIST="com.tencent.mobileqq.activity.SplashActivity";//QQ聊天列表页
//    private final static String QQ_NOTIFICATION_TIP = "[QQ红包]";
//    private final static String QQ_DEFAULT_CLICK_OPEN = "点击拆开";
//    private final static String QQ_HONG_BAO_PASSWORD = "口令红包";
//    private final static String QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令";
//    private final static String QQ_HONG_BAO_SEND="发送";

    private AccessibilityNodeInfo nodeRoot;
//    private AccessibilityNodeInfo nodeInfo;
//
//    private String packageName;
//    private String className;
//    private int eventType;
//
//    private boolean isHasReceived;//通知或聊天页面收到红包
//    private boolean isHasClicked;//点击红包
//    private boolean isHasInput;//输入红包口令
//    private boolean isHasOpened;//发送红包口令
//    private boolean isHasReceivedList;//从聊天页面收到后点击红包


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.INSTANCE.d("service oncreate.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.INSTANCE.d("service onstartcommand.");
        Notification.Builder builder = new Notification.Builder(MyApplication.Companion.getAppContext());
        Intent notificationIntent = new Intent(this, MainActivity.class);

        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // set the large icon in the drop down list.
                .setContentTitle("ATAP") // set the caption in the drop down list.
                .setSmallIcon(R.mipmap.ic_launcher) // set the small icon in state.
                .setContentText("ATAP") // set context content.
                .setWhen(System.currentTimeMillis()); // set the time for the notification to occur.

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;// set default sound.

        startForeground(110, notification);
        flags = Service.START_FLAG_REDELIVERY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.INSTANCE.d("service ondestroy.");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        Log.d(TAG_SHUA,"------->"+event.toString());

        ControlUse controlUse=new ControlUse(getApplicationContext());
        if(controlUse.stopUse()){
            Log.d(TAG,"time---停止使用");
            isStopUse=true;
//            return;
        }
        Log.d(TAG,"state"+ PreferencesUtils.INSTANCE.getUsestatus());
        if(!PreferencesUtils.INSTANCE.getUsestatus()){
            Log.d(TAG,"use---停止使用");
            isStopUse=true;
//            return;
        }

        Log.d(TAG,"use---"+isStopUse);
        LogUtil.INSTANCE.d( "....");
        if(!isStopUse){
            nodeRoot=getRootInActiveWindow();
            try{
//            QQHongBaoService qqHongBaoService=new QQHongBaoService(getApplicationContext(),event,nodeRoot);
                if(PreferencesUtils.INSTANCE.getQqUseStatus()){
                    QQHongBaoService qqHongBaoService=new QQHongBaoService(this,getApplicationContext(),event,nodeRoot);
                }
                if(PreferencesUtils.INSTANCE.getXiuYiXiuUseStatus()){
                    XiuYiXiuService xiuYiXiuService=new XiuYiXiuService(getApplicationContext(),event,nodeRoot);
                }else{
                    Log.d(TAG,"支付宝自动咻一咻功能未开启");
                }

            }catch (Exception e){
                Log.e(TAG,"异常错误");
            }
        }

//        int eventType=event.getEventType();
//        if(getPackageName().equals(QQHongBaoService.PACKAGE_QQ)){
//            switch (eventType){
//                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                    LogUtil.d( "检测到QQ界面改变");
////                    if(className.equals(CLASS_QQ_LIST)){
////                        grabHongBao();
////                    }
//                    AccessibilityNodeInfo nodeInfo=event.getSource();
//                    //没有发送按钮，不是领红包页面
//                    List<AccessibilityNodeInfo>  list=nodeInfo.findAccessibilityNodeInfosByText("搜索");
//                    if(list.size()<1){
//                        LogUtil.d("不是搜索页面");
//                        return;
//                    }
//                    performGlobalAction(AccessibilityService.GESTURE_SWIPE_DOWN);
//                    break;
//            }
//        }

//        if(packageName.equals(PACKAGE_QQ)){
////            Log.d(TAG,"检测到QQ服务");
////            Log.i(TAG,"event------->"+event);
//            switch (eventType){
//                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                    Log.d(TAG, "检测到QQ通知");
//                    List<CharSequence> texts=event.getText();
//                    Log.i(TAG, "检测到QQ通知，文本为------------>" + texts);
//                    if(!texts.isEmpty()){
//                        String text=texts.toString();
//                        if(text.contains(QQ_NOTIFICATION_TIP)){
//                            Log.d(TAG,"准备打开通知栏");
//                            wakeUpAndUnlock(getApplicationContext());
//                            isHasReceived=true;
//                            //以下是精华，将微信的通知栏消息打开
//                            Notification notification= (Notification) event.getParcelableData();
//                            PendingIntent pendingIntent=notification.contentIntent;
//                            try {
//                                Log.d(TAG,"准备打开通知栏");
//                                pendingIntent.send();
//                            } catch (PendingIntent.CanceledException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    break;
//                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                    Log.d(TAG, "检测到QQ界面改变");
//                    if(className.equals(CLASS_QQ_LIST)){
//                        wakeUpAndUnlock(getApplicationContext());//解锁
//                        grabHongBao();
//                    }
//                    break;
//                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                    Log.d(TAG, "检测到QQ内容改变");
////                    AccessibilityNodeInfo nodeInfo=getRootInActiveWindow();
////                    Log.d(TAG,"nodeInfo--------->"+nodeInfo);
////                    if(nodeInfo==null){
////                        Log.d(TAG,"rootWindow为空");
////                        return;
////                    }
//                    grabHongBao();
//                    if(nodeInfo==null){
//                        Log.d(TAG,"rootWindow为空");
//                        return;
//                    }
//
////                    if(nodeInfo==null){
////                        Log.d(TAG,"rootWindow为空");
////                        return;
////                    }
////                    nodeInfo.findAccessibilityNodeInfosByViewId("fdf");
//                    //输入红包口令
//                    List<AccessibilityNodeInfo> node_input=nodeInfo.findAccessibilityNodeInfosByText(QQ_CLICK_TO_PASTE_PASSWORD);
//                    Log.d(TAG, "点击输入口令个数" + node_input.size());
//                    for(int i=node_input.size()-1;i>=0;i--){
//                        AccessibilityNodeInfo parent=node_input.get(i).getParent();
//                        if(isHasClicked){
//                            isHasClicked=false;
//                            isHasInput=true;
////                            Log.d(TAG, "点击输入红包口令");
//                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }
//                    }
//
//                    //发送红包口令
//                    List<AccessibilityNodeInfo> node_send=nodeInfo.findAccessibilityNodeInfosByText(QQ_HONG_BAO_SEND);
//                    Log.d(TAG, "点击发送输入的口令个数" + node_send.size());
//                    for(int i=node_send.size()-1;i>=0;i--){
//                        AccessibilityNodeInfo parent=node_send.get(i);
////                        Log.d(TAG, "子节点：" + node_send.get(i));
////                        Log.d(TAG, "子节点：" + node_send.get(i).getParent());
//
//                        //设置0.5-1秒内随机抢
//                        Random random=new Random();
//                        int time=random.nextInt(1000);
//                        time=500+time;
//
////                        //设置1.5-2秒内随机抢
////                        Random random=new Random();
////                        int time=random.nextInt(500);
////                        time=1500+time;
//
//
//                        System.out.println(time);
//                        try {
//                            Thread.sleep(time);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if(isHasInput){
//                            isHasInput=false;
//                            isHasOpened=true;
//                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                            Log.d(TAG, "点击发送输入的红包口令");
//                        }
//                    }
//
//                    //聊天页面出现红包
//                    List<AccessibilityNodeInfo> node_hongbao=nodeInfo.findAccessibilityNodeInfosByText(QQ_NOTIFICATION_TIP);
//                    Log.d(TAG, "聊天页面出现的红包" + node_hongbao.size());
//                    for(int i=node_hongbao.size()-1;i>=0;i--){
//                        AccessibilityNodeInfo parent=node_hongbao.get(i);
//                        Log.d(TAG, "子节点：" + node_hongbao.get(i));
//                        isHasReceived=true;
//                        isHasReceivedList=true;
//                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        Log.d(TAG, "点击聊天页面中的红包");
////                        if(isHasInput){
////                            isHasInput=false;
////                            isHasSent=true;
////                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                            Log.d(TAG, "点击发送输入的红包口令");
////                        }
//                    }
//
////                    grabHongBao();
//
////                    if(isHasReceivedList){
////                        isHasReceivedList=false;
////                        grabHongBao();
////                    }
//                    int length=nodeInfo.getChildCount();
//                    nodeInfo=event.getSource();
//                    Log.d(TAG, "子节点个数" + length);
//                    for(int i=0;i<length;i++){
////                        AccessibilityNodeInfo node=nodeInfo.getChild(i);
////                        Log.d(TAG, "子节点："+node);
////                        //已经拆开红包，关闭中。
////                        if(node!=null&&isHasOpened&&node.getClassName().equals("android.widget.ImageButton")){
////                            try {
////                                Thread.sleep(1*1000);//延迟一秒关闭
////                            }catch (Exception e){
////                                e.printStackTrace();
////                            }
////                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                        }
////                        if(node.getText().toString().contains(QQ_NOTIFICATION_TIP)){
////                            Log.d(TAG,"--------->聊天中出现红包");
////                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                        }
////                        if (node.getClassName().equals("android.widget.EditText")){
////                            node.setText("kkk");
////                            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT);
////                        }
//
////                        if(node.getText().toString().equals(QQ_CLICK_TO_PASTE_PASSWORD)){
////                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                        }
//                        //返回列表
////                        if(i==0){
////                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                        }
//                    }
////                     /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
////                    AccessibilityNodeInfo node = (nodeInfo.getChildCount() > 3) ? nodeInfo.getChild(3) : null;
////                    System.out.println("---------"+node);
////                    if (node != null && "android.widget.Button".equals(node.getClassName())) {
////                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                    }
//
////                    shuaYiShua(event);
//
//                    break;
//            }
//
//        }



    }

//    private void shuaYiShua(AccessibilityEvent event){
//        nodeInfo=getRootInActiveWindow();
//        if(nodeInfo==null) {
//            Log.d(TAG_SHUA, "rootWindow为空");
//            return;
//        }
//        Log.d(TAG_SHUA,"-------"+event.toString());
//        int length=nodeInfo.getChildCount();
//        Log.d(TAG_SHUA, "子节点个数" + length);
//        //发送红包口令
////        List<AccessibilityNodeInfo> node_send=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/recent_chat_list");
////        Log.d(TAG, "点击发送输入的口令个数" + node_send.size());
////        for(int i=node_send.size()-1;i>=0;i--){
////            AccessibilityNodeInfo parent=node_send.get(i);
////                        Log.d(TAG_SHUA, "子节点：" + node_send.get(i));
////                        Log.d(TAG_SHUA, "子节点33333333333：" + node_send.get(i).getParent());
//////            if(isHasInput){
//////                isHasInput=false;
//////                isHasOpened=true;
//////                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////////                            Log.d(TAG, "点击发送输入的红包口令");
//////            }
////        }
//
//    }

//    private void grabHongBao(){
//
//        nodeInfo=getRootInActiveWindow();
//        if(nodeInfo==null){
//            Log.d(TAG,"rootWindow为空");
//            return;
//        }
//
//        //设置0.1秒内随机抢
//        Random random=new Random();
//        int time=random.nextInt(100);
//        System.out.println(time);
//        try {
//            Thread.sleep(time);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
////        //设置1-1.5秒内随机抢
////        Random random=new Random();
////        int time=1000+random.nextInt(500);
////        System.out.println(time);
////        try {
////            Thread.sleep(time);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//        //普通红包
//        List<AccessibilityNodeInfo> list=nodeInfo.findAccessibilityNodeInfosByText(QQ_DEFAULT_CLICK_OPEN);
//        Log.i(TAG,"普通红包的个数为："+list.size());
//        //最新的红包领起
//        for(int i=list.size()-1;i>=0;i--){
//            AccessibilityNodeInfo parent=list.get(i).getParent();
//            Log.i(TAG, "----------------->普通红包：" + parent);
//            if(parent!=null){
//                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                isHasClicked=true;
//                isHasOpened=true;
////                Log.d(TAG,"点击领红包");
////                if(isHasReceived){
////                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//////                    isHasReceived=false;
////                    isHasClicked=true;
////                    Log.d(TAG,"点击领红包");
////                }
//                break;
//            }
//        }
//        //口令红包
//        list=nodeInfo.findAccessibilityNodeInfosByText(QQ_HONG_BAO_PASSWORD);
//        Log.i(TAG,"口令红包的个数为："+list.size());
//        //最新的红包领起
//        for(int i=list.size()-1;i>=0;i--){
////            AccessibilityNodeInfo parent=list.get(i).getParent();
//            AccessibilityNodeInfo parent=list.get(i);
//            Log.i(TAG, "----------------->口令红包：" + parent);
////            Log.i(TAG, "----------------->口令红包：" + parent.getParent());
//            if(parent!=null&&parent.getClassName().equals("android.widget.TextView")){
//                isHasClicked=true;
//                Log.d(TAG, "点击领红包");
//                parent.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                if(isHasReceived){
////                    isHasReceived=false;
////                    isHasClicked=true;
////                    Log.d(TAG, "点击领红包");
////                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                }
//                break;
//            }
//        }
//    }


    @Override
    public void onInterrupt() {
        Log.e(TAG,"出错");
    }



    //唤醒屏幕解锁
    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km=(KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl=km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP| PowerManager.SCREEN_DIM_WAKE_LOCK,"wakeup");
        //点亮屏幕
        wl.acquire();
        //释放资源
        wl.release();

    }
}
