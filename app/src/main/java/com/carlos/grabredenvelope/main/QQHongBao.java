package com.carlos.grabredenvelope.main;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.util.PreferencesUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 小不点 on 2016/5/27.
 */
public class QQHongBao extends BaseActivity implements SeekBar.OnSeekBarChangeListener{

    private static final String TAG ="QQHongBao" ;

    @Bind(R.id.cb_qq_control)
    CheckBox mCbQqControl;
    @Bind(R.id.tv_qq_putong)
    TextView mTvQqPutong;
    @Bind(R.id.sb_qq_putong)
    SeekBar mSbQqPutong;
    @Bind(R.id.tv_qq_kouling)
    TextView mTvQqKouling;
    @Bind(R.id.sb_qq_kouling)
    SeekBar mSbQqKouling;
    @Bind(R.id.tv_qq_lingqu)
    TextView mTvQqLingqu;
    @Bind(R.id.sb_qq_lingqu)
    SeekBar mSbQqLingqu;
    @Bind(R.id.et_qq_kouling)
    EditText mEtQqKouling;
    @Bind(R.id.b_qq_kouling1)
    Button mBQqKouling1;
    @Bind(R.id.b_qq_kouling2)
    Button mBQqKouling2;
    @Bind(R.id.b_qq_kouling3)
    Button mBQqKouling3;

    private int t_putong;
    private int t_kouling;
    private int t_lingqu;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qq_hongbao);
        ButterKnife.bind(this);

        back();

        mCbQqControl.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreferencesUtils.setQQUseStatus(isChecked);
            Log.d(TAG,"check---"+isChecked);
        });

        mSbQqPutong.setOnSeekBarChangeListener(this);
        mSbQqKouling.setOnSeekBarChangeListener(this);
        mSbQqLingqu.setOnSeekBarChangeListener(this);

        loadSaveData();
    }


    private void loadSaveData(){
        mCbQqControl.setChecked(PreferencesUtils.getQQUseStatus());
        t_putong=PreferencesUtils.getQQPutongDelay();
        mTvQqPutong.setText("普通红包延迟时间：" + t_putong + "s");
        mSbQqPutong.setProgress((t_putong - 1));

        t_kouling=PreferencesUtils.getQQKoulingDelay();
        mTvQqKouling.setText("口令红包延迟时间：" + t_kouling + "s");
        mSbQqKouling.setProgress(t_kouling - 3);

        t_lingqu=PreferencesUtils.getQQLingquDelay();
        mSbQqLingqu.setProgress(t_lingqu-3);
        if(t_lingqu==11){
            mTvQqLingqu.setText("红包领取页关闭时间："+"不关闭");
        }else {
            mTvQqLingqu.setText("红包领取页关闭时间："+t_lingqu+"s");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sb_qq_putong:
                t_putong=progress+1;
                mTvQqPutong.setText("普通红包延迟时间：" + t_putong + "s");
                PreferencesUtils.setQQPutongDelay(t_putong);
                break;
            case R.id.sb_qq_kouling:
                t_kouling=progress+3;
                mTvQqKouling.setText("口令红包延迟时间："+t_kouling+"s");
                PreferencesUtils.setQQKoulingDelay(t_kouling);
                break;
            case R.id.sb_qq_lingqu:
                t_lingqu=progress+3;
                if(progress==8){
                    mTvQqLingqu.setText("红包领取页关闭时间："+"不关闭");
                }else{
                    mTvQqLingqu.setText("红包领取页关闭时间："+t_lingqu+"s");
                }
                PreferencesUtils.setQQLingquDelay(t_lingqu);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @OnClick({R.id.b_qq_kouling1, R.id.b_qq_kouling2, R.id.b_qq_kouling3})
    public void onClick(View view) {
        result=mEtQqKouling.getText().toString();
        switch (view.getId()) {
            case R.id.b_qq_kouling1:
//                result+="\u0014";//Android发不出能领
                result+="\024";//Android发不出能领
                break;
            case R.id.b_qq_kouling2:
//                result+="\u0014\n";//Android能发出领不了
                result+="\024\n";//Android能发出领不了
                break;
            case R.id.b_qq_kouling3:
//                result+="\u0014\n\u0014";
                result+="\024\n\024";
                break;
        }
        Log.d(TAG,"------>"+result);
        //复制到剪贴板
        ClipboardManager copy= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(result);
        Toast.makeText(QQHongBao.this, "口令已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

}
