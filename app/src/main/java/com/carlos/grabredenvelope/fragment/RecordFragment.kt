package com.carlos.grabredenvelope.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ArrayAdapter
import com.carlos.cutils.extend.doubleCount
import com.carlos.cutils.extend.getYearToMinute
import com.carlos.cutils.util.LogUtils
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.db.DingDingRedEnvelopeDb
import com.carlos.grabredenvelope.db.WechatRedEnvelopeDb
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.coroutines.*

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
 * Created by Carlos on 2020-01-22.
 */
class RecordFragment : BaseFragment(R.layout.fragment_record) {

    var list = ArrayList<String>()
    lateinit var arrayAdapter: ArrayAdapter<String>
    var startTime = getYearToMinute()
    var total = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        initData()
    }

    private fun init(view: View) {
        arrayAdapter = ArrayAdapter(
            view.context, R.layout.item_wechat_record, R.id.tv_item_wechat_record, list
        )
        lv_wechat_record.adapter = arrayAdapter
    }

    private fun initData() {
        job = GlobalScope.launch(Dispatchers.Main) {
            getData()
            if (list.isNullOrEmpty()) return@launch
            tv_record_title.text = "从${startTime}至今已助你抢到${total}元"
            arrayAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun getData() {
        withContext(Dispatchers.IO) {
            list.clear()
            total = 0.0
            val wechatRedEnvelopes = WechatRedEnvelopeDb.allData
            for (wechatRedEnvelope in wechatRedEnvelopes.asReversed()) {
                total = total.doubleCount(wechatRedEnvelope.count.split("元")[0].toDouble())
                list.add("${getYearToMinute(wechatRedEnvelope.time)} 助你抢到了 ${wechatRedEnvelope.count}")
            }
            if (wechatRedEnvelopes.isNotEmpty()) {
                startTime = getYearToMinute(wechatRedEnvelopes[0].time)
            }
        }
    }

}