package com.carlos.grabredenvelope.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.util.PreferencesUtils;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class XiuYiXiu extends MainActivity{

    private static final String TAG ="XiuYiXiu" ;
    private String s_delay="支付宝咻一咻自动点击延迟时间：";
    private ImageButton ib_back;
    private CheckBox cb_xiuyixiu_control;
    private TextView tv_xiuyixiu;
    private SeekBar sb_xiuyixiu;

    private double time;
    private int mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiuyixiu);

        back();

        cb_xiuyixiu_control=getViewById(R.id.cb_xiuyixiu_control);
        tv_xiuyixiu=getViewById(R.id.tv_xiuyixiu);
        sb_xiuyixiu=getViewById(R.id.sb_xiuyixiu);

        loadSaveData();

        cb_xiuyixiu_control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtils.setXiuYiXiuUseStatus(isChecked);
                Log.d(TAG, "check---" + isChecked);
            }
        });

        sb_xiuyixiu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time= progress*0.1;
                time=Math.floor(time*10)/10;//保留一位小数
                Log.d(TAG, "progress-->" + progress);
                if(progress==0){
                    tv_xiuyixiu.setText(s_delay + "连续");
                }else {
                    tv_xiuyixiu.setText(s_delay+ time + "s");
                }
                PreferencesUtils.setXiuYiXiuDelay(progress);
                Log.d(TAG, "time---" + time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void back(){
        ib_back= (ImageButton) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSaveData(){
        cb_xiuyixiu_control.setChecked(PreferencesUtils.getXiuYiXiuUseStatus());
        mark=PreferencesUtils.getXiuYiXiuDelay();
        time=mark*0.1;
        time=Math.floor(time*10)/10;//保留一位小数
        if(mark==0){
            tv_xiuyixiu.setText(s_delay+ "连续");
        }else {
            tv_xiuyixiu.setText(s_delay+ time + "s");
        }
        sb_xiuyixiu.setProgress(mark);
        Log.d(TAG, "time---" + time);
    }

    // 通过泛型简化findViewById
    @SuppressWarnings("unchecked")
    <T extends View> T getViewById(int id) {
        try {
            return (T) findViewById(id);
        } catch (ClassCastException e) {
            Log.e(TAG, "Could not cast View to create class.", e);
            throw e;
        }
    }
}
