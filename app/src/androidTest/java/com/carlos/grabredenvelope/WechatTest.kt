package com.carlos.grabredenvelope

import android.content.Context
import android.content.Intent
import android.view.KeyEvent.*
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.carlos.cutils.util.LogUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
 * Created by Carlos on 2019-10-31.
 * 自动发红包脚本
 */
@RunWith(AndroidJUnit4::class)
class WechatTest {

    private lateinit var mUiDevice: UiDevice
    private val PACKAGE_NAME = "com.tencent.mm"
    private val FRIEND_NAME = "昵称"
    private val GROUP_NAME = "小不点"
    private val LAUNCH_TIMEOUT = 10*1000L
    private val ELEMENT_TIMEOUT = 7*1000L

    @Before
    fun start() {
        LogUtils.d("start:" +Date())
        LogUtils.isShowLog = true

        mUiDevice = UiDevice.getInstance(getInstrumentation())

        val context = getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_NAME)
        if (intent == null) {
            LogUtils.d("can not find this app.")
            return
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        LogUtils.d("" +Date())
        // Wait for the app to appear
        mUiDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT)
    }

    @Test
    fun sendPacket() {
        sendPacketToGroup()
//        sendPacketToFriend()
    }

    fun sendPacketToGroup() {
        // 找到对应的列表并点击
        val lists =  mUiDevice.findObjects(By.res("com.tencent.mm:id/b4r"))
        for (list in lists) {
            mUiDevice.wait(Until.findObject(By.text(GROUP_NAME)), ELEMENT_TIMEOUT).click()
            break
        }

        //点击加号显示底部更多菜单
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/aks")), ELEMENT_TIMEOUT).click()
        //如果不加此等待更新可能会出现获取到的子UiObject为空
        mUiDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT)
        //点击红包按钮
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/pw")), ELEMENT_TIMEOUT).children[4].click()
        mUiDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT)
        //红包EditText框，输入红包金额等

        val editText = mUiDevice.findObjects(By.clazz("android.widget.EditText"))
        for (uiObject2 in editText) {
            uiObject2.print()
        }
        editText[0].text = "0.03"
        editText[1].text = "3"
        //点击塞钱进红包按钮
        mUiDevice.findObject(By.res("com.tencent.mm:id/ddo")).click()
        //出现输入密码框
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/gwo")), ELEMENT_TIMEOUT)
        //输入支付密码
        mUiDevice.pressKeyCode(KEYCODE_1)
        mUiDevice.pressKeyCode(KEYCODE_3)
        mUiDevice.pressKeyCode(KEYCODE_4)
        mUiDevice.pressKeyCode(KEYCODE_6)
        mUiDevice.pressKeyCode(KEYCODE_7)
        mUiDevice.pressKeyCode(KEYCODE_9)

    }

    fun sendPacketToFriend() {
        // 找到对应的列表并点击
        val lists =  mUiDevice.findObjects(By.res("com.tencent.mm:id/bah"))
        for (list in lists) {
            mUiDevice.wait(Until.findObject(By.text(FRIEND_NAME)), ELEMENT_TIMEOUT).click()
        }

        //点击加号显示底部更多菜单
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/aqk")), ELEMENT_TIMEOUT).click()
        //如果不加此等待更新可能会出现获取到的子UiObject为空
        mUiDevice.waitForWindowUpdate(PACKAGE_NAME, LAUNCH_TIMEOUT)
        //点击红包按钮
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/zo")), ELEMENT_TIMEOUT).children[4].click()
        //输入金额
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/d8f")), ELEMENT_TIMEOUT).text = "0.03"
        //点击塞钱进红包按钮
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/da3")), ELEMENT_TIMEOUT).click()
        //出现输入密码框
        mUiDevice.wait(Until.findObject(By.res("com.tencent.mm:id/aos")), ELEMENT_TIMEOUT)
        //输入支付密码
        mUiDevice.pressKeyCode(KEYCODE_1)
        mUiDevice.pressKeyCode(KEYCODE_3)
        mUiDevice.pressKeyCode(KEYCODE_4)
        mUiDevice.pressKeyCode(KEYCODE_6)
        mUiDevice.pressKeyCode(KEYCODE_7)
        mUiDevice.pressKeyCode(KEYCODE_9)
    }

    @After
    fun after() {
        LogUtils.d("after:" +Date())
//        mUiDevice.abc()
    }


}



fun UiObject2.print() {
    println("text")
    LogUtils.d("id:" + this.resourceName + ";text:" + this.text )
}

