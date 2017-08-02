//package com.example.qianghongbao.test;
//
//import android.app.AlertDialog;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.qianghongbao.R;
//import com.example.qianghongbao.tools.ControlUse;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG="MainActivity";
//
//    private Button button;
//    private EditText editText;
//    private Button button2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        editText= (EditText) findViewById(R.id.editText);
//        button2= (Button) findViewById(R.id.button2);
//
//        /**
//         * 红包口令最长１８位，两种都为大哭表情，推荐第一种代码。
//         * QQ假红包代码，如：     String x="自己改文字，后面代码不可清除\u0014\n";
//         * QQ假红包口令代码，如：  String y="\u0014你好，大家好，才是真的好。哈哈\u0014\n";
//         */
//
//        //安卓手机不可以领的红包
////        final String a="自己改文字，后面代码不可清除\u0014\n";//大哭表情，可输入16位。或者代码\024\n
//        //共18位数，去掉表情和回车剩下16位"
////        final String a="自己改文字，后面代码不可清除\u0014\u001B\n";//其他表情
//        String b="\u0014123456789987654\u0014\n";
//        final String a=editText.getText().toString()+"\u0014\n";//大哭表情，可输入16位。或者代码\024\n
//
//        //安卓手机可以领但发不出的红包
//        String c="2 \u0014";
//
//        String d="GFDG\u0014\n" +
//                "\u0014.\u0014\n";
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //复制到剪贴板
//                ClipboardManager copy = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
//                copy.setText(a);
//                Log.d(TAG,"红包口令--------->"+a);
//                Toast.makeText(MainActivity.this,"口令已复制到剪贴板",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        button= (Button) findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //打开设置中辅助功能
//                Intent intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this, "找到抢红包，然后开启服务。", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        ControlUse controlUse=new ControlUse(MainActivity.this);
//        if (controlUse.stopUse()){
//            Log.d(TAG,"停止使用");
//            show_dialog();
//        }
//
//    }
//
//    private void show_dialog() {
//        AlertDialog dialog=new AlertDialog.Builder(this).setTitle("提示")
//                .setMessage("本软件设定使用时限已到时间，谢谢使用，请点击确定退出。如想继续用可联系小不点，谢谢！").setCancelable(false)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO 自动生成的方法存根
//                        finish();
//                    }
//                })
//                .create();
////		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
//    }
//}
