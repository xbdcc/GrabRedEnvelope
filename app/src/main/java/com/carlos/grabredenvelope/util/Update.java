package com.carlos.grabredenvelope.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class Update {

    private Context context;
    private int type;
    private static final String TAG="Version";

    private int versionCode;
    private String versionName;
    private List<UpdateInfo> infos;
    private int temp=0;
    private int position=0;
    private ProgressDialog dialog;
    private File file;
    private boolean newVersion;

    //获取版本信息来检测更新网址
//    public static final String check_update="http://xbdcc.ml/xbd/tools/QiangHongBao/update.xml";
//    public static final String check_update="http://xbdcc.github.io/xiaobudian/HuasTools/update.xml";
    public static final String check_update="http://xbdcc.github.io/xiaobudian/QiangHongBao/update.xml";

    public static final int MalformedURLException=004;
    public static final int IOException=005;
    /**
     * 检查更新
     */
    public static final int CAN_BE_UPDATED=010;
    public static final int NO_NEED_TO_UPDATE=011;
    public static final int DOWNLOAD_FINISHED=012;

    public static int Auto=1;
    public static int Click=2;
    public Update(Context context,int type) {
        super();
        this.context = context;
        this.type=type;
    }

    /**
     * 检测更新
     */
    public void update(){
        getCurrentVersion();
        if(type==Click){
            dialog=ProgressDialog.show(context, "", "检测版本信息，请稍后...",true,true);
        }
        new Thread(runnable).start();

    }

    /**
     * 开启线程进行联网操作
     */
    Runnable runnable=new Runnable() {

        @Override
        public void run() {
            checkVersion();
        }

    };

    /**
     * 检测版本信息
     */
    private void checkVersion() {
        try {
            URL url=new URL(check_update);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10*000);//设置超时时间10秒
            InputStream inputStream=connection.getInputStream();
            infos=UpdateInfoParser.getUpdateInfo(inputStream);
            for(int i=0;i<infos.size();i++){
                int j=infos.get(i).getVersionCode();
                if (j>temp) {
                    temp=j;
                    position=i;
                }
            }
            if (temp>versionCode) {
                Log.i(TAG, "有新的的版本" + infos.get(position).getVersionName());
                newVersion=true;
//                PreferencesUtils.setUseStatus(false);//设置不可用
                versionName=infos.get(position).getVersionName();
                handler.sendEmptyMessage(CAN_BE_UPDATED);
            }else {
                Log.i(TAG, "当前已是最新版本");
                handler.sendEmptyMessage(NO_NEED_TO_UPDATE);
            }
        } catch (MalformedURLException e) {
            handler.sendEmptyMessage(-1);
            e.printStackTrace();
        } catch (IOException e) {
            handler.sendEmptyMessage(-2);
            e.printStackTrace();
        }

    }

    /**
     * 获取当前版本号
     */
    private void getCurrentVersion(){
        PackageManager manager=context.getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(context.getPackageName(), 0);
            versionCode=info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler与线程和UI通信
     */
    Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case CAN_BE_UPDATED:
//                    dialog.dismiss();
                    Toast.makeText(context, infos.get(position).getVersionName(), Toast.LENGTH_SHORT).show();
                    showUpdateDialog();
                    break;
                case NO_NEED_TO_UPDATE:
//                    dialog.dismiss();
                    if(type==Click){
                        ToastUtils.showToast(context, "当前已是最新版本");
                    }
                    break;
                case DOWNLOAD_FINISHED:
                    InstallApk();
                    break;
                case MalformedURLException:
                    ToastUtils.showToast(context, "MalformedURLException");
                    break;
                case IOException:
                    ToastUtils.showToast(context, "IOException");
                    break;
                default:
                    break;
            }
            if(dialog!=null){
                dialog.dismiss();
            }
        };
    };

    /**
     * 显示检测更新对话框
     */
    private void showUpdateDialog(){
        DialogUtils utils=new DialogUtils();


//                utils.showDialogNotCancel(context, "检测到新版本，点击下载",
//                infos.get(position).getDescription(), new DialogUtils.OnClickSureListener() {
//
//                    @Override
//                    public void onClickSure() {
//                        Download();
//                    }
//                }, new DialogUtils.OnClickCancelListener() {
//
//                    @Override
//                    public void onClickCancel() {
////                        System.exit(0);//会把推送也停掉，不行
//
////                        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
////                        MyIntent.addCategory(Intent.CATEGORY_HOME);
////                        context.startActivity(MyIntent);
////                        finish();
//                        android.os.Process.killProcess(android.os.Process.myPid());
//                        if(dialog!=null){
////                            android.os.Process.killProcess(android.os.Process.myPid());
//                            dialog.dismiss();
//                        }
//                    }
//                });


//        可点击取消的更新
        utils.showAlertDialog(context, "检测到新版本，是否更新",
                infos.get(position).getDescription(), new DialogUtils.OnClickSureListener() {

                    @Override
                    public void onClickSure() {
                        Download();
                    }
                }, new DialogUtils.OnClickCancelListener() {

                    @Override
                    public void onClickCancel() {
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });
    }


    /**
     * 下载新版APK
     */
    public void Download(){
        File filedir = getFiledir();
        file=new File(filedir,"QiangHongBao"+versionName+".apk");
//		file=new File(Environment.getExternalStorageDirectory()+"/HuasTools","HuasTools"+versionName);
        DownloadAsyncTask task=new DownloadAsyncTask(context, handler, file);
        task.execute(infos.get(position).getApkUrl());
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

    /**
     * 安装APK
     */
    private void InstallApk(){
        if (file.exists()) {
            Intent intent=new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    public boolean hasNewVersion(){
        return newVersion;
    }
}
