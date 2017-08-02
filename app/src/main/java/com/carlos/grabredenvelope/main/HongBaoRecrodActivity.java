package com.carlos.grabredenvelope.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.dao.QQ_Hongbao;
import com.carlos.grabredenvelope.util.DatabaseHelper;
import com.carlos.grabredenvelope.util.PreferencesUtils;
import com.carlos.grabredenvelope.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 小不点 on 2016/5/29.
 */
public class HongBaoRecrodActivity extends BaseActivity {

    @Bind(R.id.b_clear_old)
    Button mBClearOld;
    @Bind(R.id.lv_hongbao_record)
    ListView mLvHongbaoRecord;
    @Bind(R.id.tv_record)
    TextView mTvRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hongbao_record);
        ButterKnife.bind(this);

        back();
        setMenuTitle(getResources().getString(R.string.menu_hongbao_record));

        loadData();
    }

    private void loadData() {

        String time = PreferencesUtils.getQQHongbaoRecordTime();
        Float count = PreferencesUtils.getQQHongbaoRecordCount();
        if(count!=0){
            String s=String.format("%.2f", count);//四舍五入保留两位小数
            mTvRecord.setText("亲，从"+time+"开始到现在已经为您抢到"+s+"元QQ红包哦！");
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<QQ_Hongbao> qq_hongbaos = databaseHelper.get();
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < qq_hongbaos.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("time", qq_hongbaos.get(i).getTime());
            map.put("hb_count", qq_hongbaos.get(i).getHb_count_tv() + "元");
            Log.e("00000000", qq_hongbaos.get(i).getTime());
            Log.e("00000000", qq_hongbaos.get(i).getHb_count_tv() + "元");
            Log.e("00000000", "来自：" + qq_hongbaos.get(i).getSend_info());
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                list, R.layout.item_hongbao_record, new String[]{"time", "hb_count"}, new int[]{
                R.id.tv_time, R.id.tv_hb_count});
        mLvHongbaoRecord.setAdapter(simpleAdapter);
        Utility.setListViewHeightBasedOnChildren(mLvHongbaoRecord);
        mLvHongbaoRecord.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("红包详情");
            QQ_Hongbao qq_hongbao = qq_hongbaos.get(position);
            builder.setMessage("抢到时间:\t" + qq_hongbao.getTime() + "\n"
                    + "发送人：:\t" + qq_hongbao.getSend_info() + "\n"
                    + "红包种类:\t" + qq_hongbao.getWish_word() + "\n"
                    + "红包金额:\t" + qq_hongbao.getHb_count_tv() + "元\n"
            );
            builder.setPositiveButton("确定", null);
            builder.create();
            builder.show();
        });

    }

    @OnClick(R.id.b_clear_old)
    public void onClick() {

    }
}
