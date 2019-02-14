//package com.carlos.grabredenvelope.main
//
//import android.app.AlertDialog
//import android.os.Bundle
//import android.util.Log
//import android.view.Window
//import android.widget.Button
//import android.widget.ListView
//import android.widget.SimpleAdapter
//import android.widget.TextView
//import butterknife.Bind
//import butterknife.ButterKnife
//import butterknife.OnClick
//import com.carlos.grabredenvelope.R
//import com.carlos.grabredenvelope.util.DatabaseHelper
//import com.carlos.grabredenvelope.util.PreferencesUtils
//import com.carlos.grabredenvelope.util.Utility
//import java.util.*
//
///**
// * Created by 小不点 on 2016/5/29.
// */
//class HongBaoRecrodActivity : BaseActivity() {
//
//    @Bind(R.id.b_clear_old)
//    internal var mBClearOld: Button? = null
//    @Bind(R.id.lv_hongbao_record)
//    internal var mLvHongbaoRecord: ListView? = null
//    @Bind(R.id.tv_record)
//    internal var mTvRecord: TextView? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        setContentView(R.layout.activity_hongbao_record)
//        ButterKnife.bind(this)
//
//        back()
//        setMenuTitle(resources.getString(R.string.menu_hongbao_record))
//
//        loadData()
//    }
//
//    private fun loadData() {
//
//        val time = PreferencesUtils.qqHongbaoRecordTime
//        val count = PreferencesUtils.qqHongbaoRecordCount
//        if (count != 0f) {
//            val s = String.format("%.2f", count)//四舍五入保留两位小数
//            mTvRecord!!.text = "亲，从" + time + "开始到现在已经为您抢到" + s + "元QQ红包哦！"
//        }
//
//        val databaseHelper = DatabaseHelper(this)
//        val qq_hongbaos = databaseHelper.get()
//        val list = ArrayList<Map<String, String>>()
//        for (i in qq_hongbaos.indices) {
//            val map = HashMap<String, String>()
//            map["time"] = qq_hongbaos[i].time + ""
//            map["hb_count"] = qq_hongbaos[i].hb_count_tv!! + "元"
//            Log.e("00000000", qq_hongbaos[i].time)
//            Log.e("00000000", qq_hongbaos[i].hb_count_tv!! + "元")
//            Log.e("00000000", "来自：" + qq_hongbaos[i].send_info!!)
//            list.add(map)
//        }
//        val simpleAdapter = SimpleAdapter(this,
//                list, R.layout.item_hongbao_record, arrayOf("time", "hb_count"), intArrayOf(R.id.tv_time, R.id.tv_hb_count))
//        mLvHongbaoRecord!!.adapter = simpleAdapter
//        Utility.setListViewHeightBasedOnChildren(mLvHongbaoRecord!!)
//        mLvHongbaoRecord!!.setOnItemClickListener { parent, view, position, id ->
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("红包详情")
//            val qq_hongbao = qq_hongbaos[position]
//            builder.setMessage("抢到时间:\t" + qq_hongbao.time + "\n"
//                    + "发送人：:\t" + qq_hongbao.send_info + "\n"
//                    + "红包种类:\t" + qq_hongbao.wish_word + "\n"
//                    + "红包金额:\t" + qq_hongbao.hb_count_tv + "元\n"
//            )
//            builder.setPositiveButton("确定", null)
//            builder.create()
//            builder.show()
//        }
//
//    }
//
//    @OnClick(R.id.b_clear_old)
//    fun onClick() {
//
//    }
//}
