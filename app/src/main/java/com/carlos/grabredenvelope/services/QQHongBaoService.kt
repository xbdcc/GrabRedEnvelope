package com.carlos.grabredenvelope.services

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.carlos.grabredenvelope.dao.QQ_Hongbao
import com.carlos.grabredenvelope.util.DatabaseHelper
import com.carlos.grabredenvelope.util.LogUtil
import com.carlos.grabredenvelope.util.PreferencesUtils
import com.carlos.grabredenvelope.util.WakeupTools
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by 小不点 on 2016/2/18.
 */
class QQHongBaoService(internal var acc: AccessibilityService, private val context: Context, private val event: AccessibilityEvent, private val nodeRoot: AccessibilityNodeInfo) {
    private var nodeInfo: AccessibilityNodeInfo? = null
    private val parent: AccessibilityNodeInfo? = null
    private val child: AccessibilityNodeInfo? = null
    private var list: List<AccessibilityNodeInfo>? = null

    private val packageName: String
    private val className: String
    private val eventType: Int
    private var size: Int = 0
    private val i: Int = 0
    private var time: Int = 0
    private val random: Random? = null

    private var isHasReceived: Boolean = false//true已经通知或聊天页面收到红包
    private var isHasClicked: Boolean = false//true点击了红包
    private var isHasInput: Boolean = false//true输入了红包口令
    private var isHasOpened: Boolean = false//true发送红包口令
    private var isHasReceivedList: Boolean = false//从聊天页面收到后点击红包

    private val delayedOpenPutong = 0//延迟点击普通红包时间
    private val delayedOpenKouling = 1//延迟点击口令红包时间
    private val delayedClick = 2//延迟输入口令时间
    private val delayedSubmit = 3//延迟点击发送时间
    private val delayedClose = 4//延迟关闭红包页面时间

    init {

        packageName = event.packageName.toString()
        className = event.className.toString()
        eventType = event.eventType

        if (packageName == PACKAGE_QQ) {
            //            LogUtil.d("检测到QQ服务");
            //            Log.i(TAG,"event------->"+event);
            when (eventType) {
                AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                    LogUtil.d("检测到QQ通知")
                    val texts = event.text
                    Log.i(TAG, "检测到QQ通知，文本为------------>$texts")
                    if (!texts.isEmpty()) {
                        val text = texts.toString()
                        if (text.contains(QQ_NOTIFICATION_TIP)) {
                            //                            LogUtil.d( "准备打开通知栏");
                            WakeupTools.wakeUpAndUnlock(context)
                            isHasReceived = true
                            //以下是精华，将QQ的通知栏消息打开
                            val notification = event.parcelableData as Notification
                            val pendingIntent = notification.contentIntent
                            try {
                                LogUtil.d("准备打开通知栏")
                                pendingIntent.send()
                            } catch (e: PendingIntent.CanceledException) {
                                e.printStackTrace()
                            }

                        }
                    }
                }
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    LogUtil.d("检测到QQ界面改变")
                    //                    if(className.equals(CLASS_QQ_LIST)){
                    //                        grabHongBao();
                    //                    }
                    closeResult()
                }
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                    LogUtil.d("检测到QQ内容改变")
                    //                    AccessibilityNodeInfo nodeInfo=nodeRoot;
                    //                    LogUtil.d("nodeInfo--------->"+nodeInfo);
                    //                    if(nodeInfo==null){
                    //                        LogUtil.d("rootWindow为空");
                    //                        return;
                    //                    }
                    //                    closeResult();
                    grabHongBao()

                    //                    if(nodeInfo==null){
                    //                        LogUtil.d("rootWindow为空");
                    //                        return;
                    //                    }
                    //                    nodeInfo.findAccessibilityNodeInfosByViewId("fdf");
                    //输入红包口令
                    val node_input = nodeInfo!!.findAccessibilityNodeInfosByText(QQ_CLICK_TO_PASTE_PASSWORD)
                    if (node_input != null) {
                        LogUtil.d("点击输入口令个数" + node_input.size)
                        size = node_input.size
                        if (size > 0) {
                            val parent = node_input[size - 1].parent
                            if (isHasClicked) {
                                isHasClicked = false
                                isHasInput = true
                                delayedControl(2)
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
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
                    val node_send = nodeInfo!!.findAccessibilityNodeInfosByText(QQ_HONG_BAO_SEND)
                    LogUtil.d("点击发送输入的口令个数" + node_send.size)
                    size = node_send.size
                    if (size > 0) {
                        val parent = node_send[size - 1]

                        delayedControl(3)

                        if (isHasInput) {
                            isHasInput = false
                            isHasOpened = true
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                            LogUtil.d("点击发送输入的红包口令")
                            isFinished = true
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
                    val node_hongbao = nodeInfo!!.findAccessibilityNodeInfosByText(QQ_NOTIFICATION_TIP)
                    LogUtil.d("聊天页面出现的红包" + node_hongbao.size)
                    size = node_hongbao.size
                    if (size > 0) {
                        val parent = node_hongbao[size - 1]
                        LogUtil.d("子节点：" + node_hongbao[size - 1])
                        isHasReceived = true
                        isHasReceivedList = true
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        LogUtil.d("点击聊天页面中的红包")
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


                    val length = nodeInfo!!.childCount

                    nodeInfo = event.source
                    LogUtil.d("子节点个数$length")
                    for (i in 0 until length) {
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
                }
            }//                    shuaYiShua();
            //                     /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
            //                    AccessibilityNodeInfo node = (nodeInfo.getChildCount() > 3) ? nodeInfo.getChild(3) : null;
            //                    System.out.println("---------"+node);
            //                    if (node != null && "android.widget.Button".equals(node.getClassName())) {
            //                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            //                    }

        }

    }

    private fun shuaYiShua() {
        //没有发送按钮，不是领红包页面
        list = nodeInfo!!.findAccessibilityNodeInfosByText("搜索")
        if (list!!.size < 1) {
            LogUtil.d("不是领红包页面")
            return
        }

        acc.performGlobalAction(AccessibilityService.GESTURE_SWIPE_DOWN_AND_UP)
        //        if(size>0){
        //            AccessibilityNodeInfo parent=list.get(size-1);
        //            parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        //            LogUtil.d( "点击聊天页面中的111111红包");
        //        }

    }


    private fun grabHongBao() {
        //        WakeupTools.wakeUpAndUnlock(context);//解锁
        nodeInfo = nodeRoot
        if (nodeInfo == null) {
            LogUtil.d("rootWindow为空")
            return
        }

        //没有发送按钮，不是领红包页面
        list = nodeInfo!!.findAccessibilityNodeInfosByText("发送")
        if (list!!.size < 1) {
            LogUtil.d("不是领红包页面")
            return
        }


        LogUtil.d("--->" + nodeInfo!!)
        LogUtil.d("--->" + nodeInfo!!.className)
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
        list = nodeInfo!!.findAccessibilityNodeInfosByText(QQ_DEFAULT_CLICK_OPEN)
        Log.i(TAG, "普通红包的个数为：" + list!!.size)
        size = list!!.size
        //领取最新的红包
        if (size > 0) {
            val parent = list!![size - 1].parent
            Log.i(TAG, "----------------->普通红包：" + parent!!)
            if (parent != null) {
                isFinished = true
                WakeupTools.wakeUpAndUnlock(context)//解锁
                delayedControl(0)
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                isHasClicked = true
                isHasOpened = true
                LogUtil.d("isfinish-------->$isFinished")
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
        list = nodeInfo!!.findAccessibilityNodeInfosByText(QQ_HONG_BAO_PASSWORD)
        Log.i(TAG, "口令红包的个数为：" + list!!.size)
        size = list!!.size
        //领取最新的红包
        if (size > 0) {
            val node_child = list!![size - 1]
            LogUtil.d("-----123$node_child")
            val text = node_child.parent.contentDescription.toString()
            LogUtil.d("--------123$text")
            if (text.contains("\u0014\n" + "\u0014")) {
                LogUtil.d("--->发不出口令抢不到红包")
            } else if (text.contains("\u0014\n")) {
                LogUtil.d("--->能发出不能领")
            } else if (node_child != null && node_child.className == "android.widget.TextView"
                    && node_child.text.toString() == QQ_HONG_BAO_PASSWORD) {
                if (!isSingerClick) {
                    isSingerClick = true
                    isHasClicked = true
                    LogUtil.d("点击领红包")
                    WakeupTools.wakeUpAndUnlock(context)//解锁
                    delayedControl(1)
                    node_child.parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    if (isHasReceived) {
                        isHasReceived = false
                        isHasClicked = true
                        LogUtil.d("点击领红包---")
                        parent!!.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    }
                }
            }//            else if(text.contains("\u0014")){
            //                LogUtil.d("--->发不出能领");
            //            }
            //            Log.i(TAG, "----------------->口令红包：" + node_child);
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

    private fun closeResult() {
        nodeInfo = nodeRoot
        if (nodeInfo == null) {
            LogUtil.d("rootWindow为空")
            return
        }
        LogUtil.d("isFinished$isFinished")
        if (isFinished) {
            list = nodeInfo!!.findAccessibilityNodeInfosByText("查看领取详情")
            LogUtil.d("需要关闭的页面：" + list!!.size)
            isSingerClick = false

            saveHongbao()

            list = nodeInfo!!.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/close_btn")
            if (PreferencesUtils.qqLingquDelay !== 11) {
                delayedControl(4)
            }
            list!![0].performAction(AccessibilityNodeInfo.ACTION_CLICK)
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
            isFinished = false
        }
    }


    fun delayedControl(state: Int) {
        //        delayedControlOne(state);
        delayedControlTwo(state)
    }


    fun delayedControlTwo(state: Int) {
        when (state) {
            0 -> {
                time = PreferencesUtils.qqPutongDelay * 1000
                LogUtil.d("------>putong$time")
            }
            1 -> time = 1 * 1000
            2 -> {
                //                delayedTime(1*1000);
                time = 1 * 1000
                LogUtil.d("------>点击口令$time")
            }
            3 -> {
                //                delayedTime(PreferencesUtils.getQQKoulingDelay()-2);
                time = (PreferencesUtils.qqKoulingDelay - 2) * 1000
                LogUtil.d("------>发送口令$time")
            }
            4 -> {
                time = PreferencesUtils.qqLingquDelay * 1000
                LogUtil.d("------>关闭窗口$time")
            }
        }
        delayedTime(time)
        LogUtil.d("延迟时间：-------->$time")
    }

    /**
     * 自己的时间
     * @param state
     */
    fun delayedControlOne(state: Int) {
        when (state) {
            0 -> {
            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
        }//                time=PreferencesUtils.getQQPutongDelay()*1000;
        //                LogUtil.d("------>putong"+time);
        //设置0.1秒内随机抢
        //                Random random=new Random();
        //                time=random.nextInt(100);
        //                delayedTime(time);
        //设置0.1秒内随机抢
        //                Random random=new Random();
        //                time=random.nextInt(100);
        //                delayedTime(time);
        //                //设置0.1秒内随机抢
        //                random=new Random();
        //                time=random.nextInt(100);
        //                delayedTime(time);
        // 设置0.5-1秒内随机抢
        //                random=new Random();
        //                time=random.nextInt(500);
        //                time=500+time;
        //                delayedTime(time);
        //                delayedTime(1000);
    }

    fun delayedTime(time: Int) {
        LogUtil.d("延迟时间：-------->$time")
        try {
            Thread.sleep(time.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }


    private fun saveHongbao() {

        list = nodeInfo!!.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/hb_error_tv")//红包被领完
        //如果没有被领完才保存记录
        if (list!!.size < 1) {
            val qq_hongbao = QQ_Hongbao()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date = Date(System.currentTimeMillis())
            val time = dateFormat.format(date)
            qq_hongbao.time = time

            list = nodeInfo!!.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/sender_info")//谁发送的红包
            val send_info = list!![0].text.toString()
            qq_hongbao.send_info = send_info

            list = nodeInfo!!.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/wish_word")//红包种类
            val wish_word = list!![0].text.toString()
            qq_hongbao.wish_word = wish_word

            list = nodeInfo!!.findAccessibilityNodeInfosByViewId("com.tencent.mobileqq:id/hb_count_tv")//红包金额
            val hb_count_tv = list!![0].text.toString()
            qq_hongbao.hb_count_tv = hb_count_tv


            var count = PreferencesUtils.qqHongbaoRecordCount
            if (count == 0f) {
                PreferencesUtils.qqHongbaoRecordTime = time//清空过数据，重新设置开始时间
            }
            count += java.lang.Float.valueOf(hb_count_tv)!!
            PreferencesUtils.setQQHongbaoRecordCount(count)

            val databaseHelper = DatabaseHelper(context)
            databaseHelper.insert(qq_hongbao)

        }

    }

    companion object {

        private val TAG = "QQHongBaoService"

        val PACKAGE_QQ = "com.tencent.mobileqq"//QQ包名
        private val CLASS_QQ_LIST = "com.tencent.mobileqq.activity.SplashActivity"//QQ聊天列表页
        private val QQ_NOTIFICATION_TIP = "[QQ红包]"
        private val QQ_DEFAULT_CLICK_OPEN = "点击拆开"
        private val QQ_HONG_BAO_PASSWORD = "口令红包"
        private val QQ_CLICK_TO_PASTE_PASSWORD = "点击输入口令"
        private val QQ_HONG_BAO_SEND = "发送"
        private var isFinished: Boolean = false//true查看抢红包的结果
        private var isSingerClick = false//一次红包口令不重复点击
    }
}
