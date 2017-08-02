package com.carlos.grabredenvelope.main;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.carlos.grabredenvelope.R;
import com.carlos.grabredenvelope.util.BitmapUtils;

import java.io.File;

/**
 * Created by 小不点 on 2016/2/22.
 */
public class About extends Activity{

    private ImageButton ib_back;
    private Button b_donate_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        back();


        b_donate_me= (Button) findViewById(R.id.b_donate_me);
        b_donate_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDonateDialog();
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

    /** 显示捐赠的对话框*/
    private void showDonateDialog() {
        final Dialog dialog = new Dialog(this, R.style.donate_dialog_theme);
        View view = getLayoutInflater().inflate(R.layout.donate_dialog_layout, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                File filedir = getFiledir();
                File output = new File(filedir, "xbd.jpg");
                if(!output.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.xbd);
                    BitmapUtils.saveBitmap(About.this, output, bitmap);
                }
                Toast.makeText(About.this, "已保存到:" + output.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 得到安装路径
     * @return
     */
    File getFiledir() {
        File sd= Environment.getExternalStorageDirectory();
        String path=sd.getPath()+"/QiangHongBao";
        File filedir=new File(path);
        if (!filedir.exists()) {
            filedir.mkdir();
            Log.e("Update", "新建一个文件夹");
        }
        return filedir;
    }
}
