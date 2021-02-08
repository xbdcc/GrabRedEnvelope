package com.carlos.grabredenvelope

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.carlos.cutils.util.LogUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

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
 * Created by Carlos on 2021/02/08.
 * 自动发送表情脚本, 基于微信8.0.0
 */
@RunWith(AndroidJUnit4::class)
class AutoSendEmojiTest {

    private lateinit var mUiDevice: UiDevice
    private val PACKAGE_NAME = "com.tencent.mm"
    private val CHAT_NAME = "小不点"//聊天对象名称
    private val SEND_TEXT = "[烟花]"//输入框输入表情：[烟花],[庆祝],[炸弹]。[Fireworks],[Party],[Bomb]
    private val LAUNCH_TIMEOUT = 10 * 1000L
    private val ELEMENT_TIMEOUT = 5 * 1000L

    @Before
    fun start() {
        LogUtils.d("start:" + Date())
        LogUtils.isShowLog = true

        mUiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        openAppByDefault()
//        openAppByCommand("com.tencent.mm/.ui.LauncherUI")

        // Wait for the app to appear
        mUiDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT)

    }

    private fun openAppByDefault() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }


    //通过命令启动
    private fun openAppByCommand(string: String) {
        try {
            mUiDevice.executeShellCommand("am start -n $string") //执行一个shell命令，需要5.0以上手机
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Test
    fun sendOne() {
        clickList(CHAT_NAME)

        fillText(SEND_TEXT)

        sendMessage()
    }

    @Test
    fun sendTimes() {
        val times = 13//发送次数
        clickList(CHAT_NAME)

        for (index in 1..times) {
            LogUtils.d("第${index}次发送$SEND_TEXT")
            fillText(SEND_TEXT)
            sendMessage()
        }
    }


    /**
     * 从聊天列表找到要发送的聊天对象名称并点击
     * @param chatName 聊天对象名称
     */
    private fun clickList(chatName: String) {
        // 找到对应的列表并点击
        val lists = mUiDevice.findObjects(By.clazz("android.widget.ListView"))
        for (list in lists) {
            list.findObject(By.text(chatName)).click()
            break
        }
    }

    /**
     * 填充内容
     */
    private fun fillText(sendText: String) {
        //如果不加此等待更新可能会出现获取到的子UiObject为空
        mUiDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT)

        //输入框输入表情：[烟花],[庆祝],[炸弹]。[Fireworks],[Party],[Bomb]
        val editTexts = mUiDevice.findObjects(By.clazz("android.widget.EditText"))
        editTexts.first().text = sendText
    }

    /**
     * 等待发送按钮出现并发送内容
     */
    private fun sendMessage() {
        val buttons =
            mUiDevice.wait(Until.findObjects(By.clazz("android.widget.Button")), ELEMENT_TIMEOUT)
        for (button in buttons) {
            if (button.text == "发送") {
                button.click()
            }
        }
    }

    @After
    fun after() {
        LogUtils.d("after:" + Date())
    }

}


