package com.carlos.grabredenvelope.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.carlos.grabredenvelope.dao.QQ_Hongbao;
import com.carlos.grabredenvelope.util.DatabaseHelper;
import com.carlos.grabredenvelope.util.LogUtil;
import com.carlos.grabredenvelope.util.PreferencesUtils;
import com.carlos.grabredenvelope.util.WakeupTools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by 小不点 on 2016/2/18.
 */
public class QQHongBaoService {

    private Context context;
    private AccessibilityEvent event;
    private AccessibilityNodeInfo nodeRoot;
    private AccessibilityNodeInfo nodeInfo;
    private AccessibilityNodeInfo parent;
    private AccessibilityNodeInfo child;
    private List<AccessibilityNodeInfo> list;

    private static final String TAG="QQHongBaoService";

    public static final String PACKAGE_QQ = "com.tencent.mobileqq";//QQ包名
    private static final String CLASS_QQ_LIST="com.tencent.mobileqq.activity.SplashActivity";//QQ聊天列表页
    private final static String QQ_NOTIFICATION_TIP = "[QQ红包]";
    private final static String QQ_DEFAULT_CLICK_OPEN = "点击拆开";
    private final static String QQ_HONG_BAO_PASSWORD = "口令红包";
    private final static String QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令";
    private final static String QQ_HONG_BAO_SEND="发送";

    private String packageName;
    private String className;
    private int eventType;
    private int size;
    private int i;
    private int time;
    private Random random;

    private boolean isHasReceived;//true已经通知或聊天页面收到红包
    private boolean isHasClicked;//true点击了红包
    private boolean isHasInput;//true输入了红包口令
    private boolean isHasOpened;//true发送红包口令
    private boolean isHasReceivedList;//从聊天页面收到后点击红包
    private static boolean isFinished;//true查看抢红包的结果
    private static boolean isSingerClick=false;//一次红包口令不重复点击

    private int delayedOpenPutong=0;//延迟点击普通红包时间
    private int delayedOpenKouling=1;//延迟点击口令红包时间
    private int delayedClick=2;//延迟输入口令时间
    private int delayedSubmit=3;//延迟点击发送时间
    private int delayedClose=4;//延迟关闭红包页面时间

    AccessibilityService acc;

    public QQHongBaoService(AccessibilityService acc,Context context, AccessibilityEvent event, AccessibilityNodeInfo nodeRoot){
        this.context=context;
        this.event=event;
        this.nodeRoot=nodeRoot;
        this.acc=acc;

        packageName=event.getPackageName().toString();
        className=event.getClassName().toString();
        eventType=event.getEventType();

        if(packageName.equals(PACKAGE_QQ)){
//            LogUtil.d("检测到QQ服务");
//            Log.i(TAG,"event------->"+event);
            switch (eventType){
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    LogUtil.d( "检测到QQ通知");
                    List<CharSequence> texts=event.getText();
                    Log.i(TAG, "检测到QQ通知，文本为------------>" + texts);
                    if(!texts.isEmpty()){
                        String text=texts.toString();
                        if(text.contains(QQ_NOTIFICATION_TIP)){
//                            LogUtil.d( "准备打开通知栏");
                            WakeupTools.wakeUpAndUnlock(context);
                            isHasReceived=true;
                            //以下是精华，将QQ的通知栏消息打开
                            Notification notification= (Notification) event.getParcelableData();
                            PendingIntent pendingIntent=notification.contentIntent;
                            try {
                                LogUtil.d("准备打开通知栏");
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    LogUtil.d( "检测到QQ界面改变");
//                    if(className.equals(CLASS_QQ_LIST)){
//                        grabHongBao();
//                    }
                    closeResult();
//                    shuaYiShua();
                    break;
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    LogUtil.d( "检测到QQ内容改变");
//                    AccessibilityNodeInfo nodeInfo=nodeRoot;
//                    LogUtil.d("nodeInfo--------->"+nodeInfo);
//                    if(nodeInfo==null){
//                        LogUtil.d("rootWindow为空");
//                        return;
//                    }
//                    closeResult();
                    grabHongBao();

//                    if(nodeInfo==null){
//                        LogUtil.d("rootWindow为空");
//                        return;
//                    }
//                    nodeInfo.findAccessibilityNodeInfosByViewId("fdf");
                    //输入红包口令
                    List<AccessibilityNodeInfo> node_input=nodeInfo.findAccessibilityNodeInfosByText(QQ_CLICK_TO_PASTE_PASSWORD);
                    if(node_input!=null){
                        LogUtil.d( "点击输入口令个数" + node_input.size());
                        size=node_input.size();
                        if(size>0){
                            AccessibilityNodeInfo parent=node_input.get(size-1).getParent();
                            if(isHasClicked){
                                isHasClicked=false;
                                isHasInput=true;
                                delayedControl(2);
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            LogUtil.d( "点击输入红包口令");
                            }
                        }
//                        for(int i=node_input.size()-1;i>=0;i--){
//                            AccessibilityNodeInfo parent=node_input.get(i).getParent();
//                            if(isHasClicked){
//                                isHasClicked=false;
//                                isHasInput=true;
//                                delayedControl(2);
//                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                            LogUtil.d( "点击输入红包口令");
//                            }
//                        }
                    }

                    //发送红包口令
                    List<AccessibilityNodeInfo> node_send=nodeInfo.findAccessibilityNodeInfosByText(QQ_HONG_BAO_SEND);
                    LogUtil.d( "点击发送输入的口令个数" + node_send.size());
                    size=node_send.size();
                    if(size>0){
                        AccessibilityNodeInfo parent=node_send.get(size-1);

                        delayedControl(3);

                        if(isHasInput){
                            isHasInput=false;
                            isHasOpened=true;
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            LogUtil.d( "点击发送输入的红包口令");
                            isFinished=true;
                        }
                    }
//                    for(int i=node_send.size()-1;i>=0;i--){
//                        AccessibilityNodeInfo parent=node_send.get(i);
//
//                        delayedControl(3);
//
//                        if(isHasInput){
//                            isHasInput=false;
//                            isHasOpened=true;
//                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            LogUtil.d( "点击发送输入的红包口令");
//                            isFinished=true;
//                        }
//                    }

                    //聊天页面出现红包
                    List<AccessibilityNodeInfo> node_hongbao=nodeInfo.findAccessibilityNodeInfosByText(QQ_NOTIFICATION_TIP);
                    LogUtil.d( "聊天页面出现的红包" + node_hongbao.size());
                    size=node_hongbao.size();
                    if(size>0){
                        AccessibilityNodeInfo parent=node_hongbao.get(size-1);
                        LogUtil.d( "子节点：" + node_hongbao.get(size-1));
                        isHasReceived=true;
                        isHasReceivedList=true;
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        LogUtil.d( "点击聊天页面中的红包");
                    }
//                    for(int i=node_hongbao.size()-1;i>=0;i--){
//                        AccessibilityNodeInfo parent=node_hongbao.get(i);
//                        LogUtil.d( "子节点：" + node_hongbao.get(i));
//                        isHasReceived=true;
//                        isHasReceivedList=true;
//                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        LogUtil.d( "点击聊天页面中的红包");
////                        if(isHasInput){
////                            isHasInput=false;
////                            isHasSent=true;
////                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////                            LogUtil.d( "点击发送输入的红包口令");
////                        }
//                    }


                    int length=nodeInfo.getChildCount();

                    nodeInfo=event.getSource();
                    LogUtil.d( "子节点个数" + length);
                    for(int i=0;i<length;i++){
//                        AccessibilityNodeInfo node=nodeInfo.getChild(i);
//                        LogUtil.d( "子节点："+node);
//                        //已经拆开红包，关闭中。
//                        if(node!=null&&isHasOpened&&node.getClassName().equals("android.widget.ImageButton")){
//                            try {
//                                Thread.sleep(1*1000);//延迟一秒关闭
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }
//                        if(node.getText().toString().contains(QQ_NOTIFICATION_TIP)){
//                            LogUtil.d("--------->聊天中出现红包");
//                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }
//                        if (node.getClassName().equals("android.widget.EditText")){
//                            node.setText("kkk");
//                            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT);
//                        }

//                        if(node.getText().toString().equals(QQ_CLICK_TO_PASTE_PASSWORD)){
//                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }
                        //返回列表
//                        if(i==0){
//                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        }
                    }
//                     /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
//                    AccessibilityNodeInfo node = (nodeInfo.getChildCount() > 3) ? nodeInfo.getChild(3) : null;
//                    System.out.println("---------"+node);
//                    if (node != null && "android.widget.Button".equals(node.getClassName())) {
//                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    }


                    break;
            }

        }

    }

    private void shuaYiShua() {
        //没有发送按钮，不是领红包页面
        list=nodeInfo.findAccessibilityNodeInfosByText("搜索");
        if(list.size()<1){
            LogUtil.d("不是领红包页面");
            return;
        }

        acc.performGlobalAction(AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP);
//        if(size>0){
//            AccessibilityNodeInfo parent=list.get(size-1);
//            parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
//            LogUtil.d( "点击聊天页面中的111111红包");
//        }

    }



    private void grabHongBao(){
//        WakeupTools.wakeUpAndUnlock(context);//解锁
        nodeInfo=nodeRoot;
        if(nodeInfo==null){
            LogUtil.d("rootWindow为空");
            return;
        }

        //没有发送按钮，不是领红包页面
        list=nodeInfo.findAccessibilityNodeInfosByText("发送");
        if(list.size()<1){
            LogUtil.d("不是领红包页面");
            return;
        }


        LogUtil.d("--->"+nodeInfo);
        LogUtil.d( "--->" + nodeInfo.getClassName());
//        //设置0.3秒内随机抢
//        Random random=new Random();
//        int time=random.nextInt(300);
//        System.out.println(time);
//        try {
//            Thread.sleep(time);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //普通红包
        list=nodeInfo.findAccessibilityNodeInfosByText(QQ_DEFAULT_CLICK_OPEN);
        Log.i(TAG,"普通红包的个数为："+list.size());
        size=list.size();
        //领取最新的红包
        if(size>0){
            AccessibilityNodeInfo parent=list.get(size-1).getParent();
            Log.i(TAG, "----------------->普通红包：" + parent);
            if(parent!=null){
                isFinished=true;
                WakeupTools.wakeUpAndUnlock(context);//解锁
                delayedControl(0);
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                isHasClicked=true;
                isHasOpened=true;
                LogUtil.d("isfinish-------->"+isFinished);
        }
//        //最新的红包领起
//        for(int i=list.size()-1;i>=0;i--){
//            AccessibilityNodeInfo parent=list.get(i).getParent();
//            Log.i(TAG, "----------------->普通红包：" + parent);
//            if(parent!=null){
//                isFinished=true;
//                WakeupTools.wakeUpAndUnlock(context);//解锁
//                delayedControl(0);
//                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                isHasClicked=true;
//                isHasOpened=true;
//                LogUtil.d("isfinish-------->"+isFinished);
////                isFinished=true;
////                LogUtil.d("点击领红包");
////                if(isHasReceived){
////                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//////                    isHasReceived=false;
////                    isHasClicked=true;
////                    LogUtil.d("点击领红包");
////                }
//
//                break;
//            }
        }


        //口令红包
        list=nodeInfo.findAccessibilityNodeInfosByText(QQ_HONG_BAO_PASSWORD);
        Log.i(TAG,"口令红包的个数为："+list.size());
        size=list.size();
        //领取最新的红包
        if(size>0){
            AccessibilityNodeInfo node_child=list.get(size-1);
            LogUtil.d("-----123"+node_child);
            String text=node_child.getParent().getContentDescription().toString();
            LogUtil.d("--------123"+text);
            if(text.contains("\u0014\n" + "\u0014")){
                LogUtil.d("--->发不出口令抢不到红包");
            }else if(text.contains("\u0014\n")){
                LogUtil.d("--->能发出不能领");
            }
//            else if(text.contains("\u0014")){
//                LogUtil.d("--->发不出能领");
//            }
//            Log.i(TAG, "----------------->口令红包：" + node_child);
            else if(node_child!=null&&node_child.getClassName().equals("android.widget.TextView")
                    &&node_child.getText().toString().equals(QQ_HONG_BAO_PASSWORD)){
                if(!isSingerClick){
                    isSingerClick=true;
                    isHasClicked=true;
                    LogUtil.d( "点击领红包");
                    WakeupTools.wakeUpAndUnlock(context);//解锁
                    delayedControl(1);
                    node_child.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if(isHasReceived){
                        isHasReceived=false;
                        isHasClicked=true;
                        LogUtil.d( "点击领红包---");
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
        }
//        for(int i=list.size()-1;i>=0;i--){
////            AccessibilityNodeInfo node_child=list.get(i).getParent();
//            AccessibilityNodeInfo node_child=list.get(i);
//            LogUtil.d("-----123"+node_child);
//            String text=node_child.getParent().getContentDescription().toString();
//            LogUtil.d("--------123"+text);
//            if(text.contains("\u0014\n" + "\u0014")){
//                LogUtil.d("--->发不出口令抢不到红包");
//            }else if(text.contains("\u0014\n")){
//                LogUtil.d("--->能发出不能领");
//            }
////            else if(text.contains("\u0014")){
////                LogUtil.d("--->发不出能领");
////            }
////            Log.i(TAG, "----------------->口令红包：" + node_child);
//            else if(node_child!=null&&node_child.getClassName().equals("android.widget.TextView")
//                    &&node_child.getText().toString().equals(QQ_HONG_BAO_PASSWORD)){
//                isHasClicked=true;
//                LogUtil.d( "点击领红包");
//                WakeupTools.wakeUpAndUnlock(context);//解锁
//                delayedControl(1);
//                node_child.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                if(isHasReceived){
//                    isHasReceived=false;
//                    isHasClicked=true;
//                    LogUtil.d( "点击领红包");
//                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
//                break;
//            }
        }
    }

    private void closeResult(){
        nodeInfo=nodeRoot;
        if(nodeInfo==null){
            LogUtil.d("rootWindow为空");
            return;
        }
        LogUtil.d("isFinished"+isFinished);
        if(isFinished){
            list=nodeInfo.findAccessibilityNodeInfosByText("查看领取详情");
            LogUtil.d("需要关闭的页面："+list.size());
            isSingerClick=false;

            saveHongbao();

            list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/close_btn");
            if (PreferencesUtils.getQQLingquDelay() != 11) {
                delayedControl(4);
            }
            list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            for(i=list.size()-1;i>=0;i--){
//                isSingerClick=false;
//                list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/close_btn");
//                if (PreferencesUtils.getQQLingquDelay() != 11) {
//                            delayedControl(4);
//                    list.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
////                isFinished = false;
////                LogUtil.d( "isfinish-------->" + isFinished);
//            }
            isFinished = false;
        }
    }


    public void delayedControl(int state){
//        delayedControlOne(state);
        delayedControlTwo(state);
    }


    public void delayedControlTwo(int state){
        switch (state){
            case 0:
                time=PreferencesUtils.getQQPutongDelay()*1000;
                LogUtil.d("------>putong"+time);
                break;
            case 1:
                time=1*1000;
                break;
            case 2:
                //                delayedTime(1*1000);
                time=1*1000;
                LogUtil.d("------>点击口令"+time);
                break;
            case 3:
//                delayedTime(PreferencesUtils.getQQKoulingDelay()-2);
                time=(PreferencesUtils.getQQKoulingDelay()-2)*1000;
                LogUtil.d( "------>发送口令" + time);
                break;
            case 4:
                time=PreferencesUtils.getQQLingquDelay()*1000;
                LogUtil.d( "------>关闭窗口" + time);
                break;
        }
        delayedTime(time);
        LogUtil.d("延迟时间：-------->"+time);
    }

    /**
     * 自己的时间
     * @param state
     */
    public void delayedControlOne(int state){
        switch (state){
            case 0:
//                time=PreferencesUtils.getQQPutongDelay()*1000;
//                LogUtil.d("------>putong"+time);
                //设置0.1秒内随机抢
//                Random random=new Random();
//                time=random.nextInt(100);
//                delayedTime(time);
                break;
            case 1:
                //设置0.1秒内随机抢
//                Random random=new Random();
//                time=random.nextInt(100);
//                delayedTime(time);
                break;
            case 2:
//                //设置0.1秒内随机抢
//                random=new Random();
//                time=random.nextInt(100);
//                delayedTime(time);
                break;
            case 3:
               // 设置0.5-1秒内随机抢
//                random=new Random();
//                time=random.nextInt(500);
//                time=500+time;
//                delayedTime(time);
                break;
            case 4:
//                delayedTime(1000);
                break;
        }
    }

    public void delayedTime(int time){
        LogUtil.d("延迟时间：-------->"+time);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void saveHongbao(){

        list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/hb_error_tv");//红包被领完
        //如果没有被领完才保存记录
        if(list.size()<1){
            QQ_Hongbao qq_hongbao=new QQ_Hongbao();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date=new Date(System.currentTimeMillis());
            String time=dateFormat.format(date);
            qq_hongbao.setTime(time);

            list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/sender_info");//谁发送的红包
            String send_info=list.get(0).getText().toString();
            qq_hongbao.setSend_info(send_info);

            list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/wish_word");//红包种类
            String wish_word=list.get(0).getText().toString();
            qq_hongbao.setWish_word(wish_word);

            list=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/hb_count_tv");//红包金额
            String hb_count_tv=list.get(0).getText().toString();
            qq_hongbao.setHb_count_tv(hb_count_tv);


            float count= PreferencesUtils.getQQHongbaoRecordCount();
            if(count==0){
                PreferencesUtils.setQQHongbaoRecordTime(time);//清空过数据，重新设置开始时间
            }
            count+=Float.valueOf(hb_count_tv);
            PreferencesUtils.setQQHongbaoRecordCount(count);

            DatabaseHelper databaseHelper=new DatabaseHelper(context);
            databaseHelper.insert(qq_hongbao);

        }

    }
}
