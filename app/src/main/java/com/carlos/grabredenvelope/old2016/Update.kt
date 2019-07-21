package com.carlos.grabredenvelope.old2016

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.util.Log
import com.carlos.cutils.util.LogUtils
import com.carlos.cutils.util.ToastUtil
import com.carlos.grabredenvelope.util.DownloadAsyncTask
import com.carlos.grabredenvelope.util.UpdateInfo
import com.carlos.grabredenvelope.util.UpdateInfoParser
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by 小不点 on 2016/2/20.
 */
class Update(private val context: Context, private val type: Int) {

    private var versionCode: Int = 0
    private var versionName: String? = null
    private var infos: List<UpdateInfo> = arrayListOf()
    private var temp = 0
    private var position = 0
    private var dialog: ProgressDialog? = null
    private var file: File? = null
    private var newVersion: Boolean = false

    /**
     * 开启线程进行联网操作
     */
    internal var runnable: Runnable = Runnable { checkVersion() }

    /**
     * Handler与线程和UI通信
     */
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            LogUtils.d("msg-what:" + msg.what)
            when (msg.what) {
                CAN_BE_UPDATED -> {
                    //                    dialog.dismiss();
                    showUpdateDialog()
                }
                NO_NEED_TO_UPDATE ->
                    //                    dialog.dismiss();
                    if (type == Click) {
                        ToastUtils.showToast(context, "当前已是最新版本")
                    }
                DOWNLOAD_FINISHED -> InstallApk()
                MalformedURLException -> ToastUtils.showToast(
                    context,
                    "MalformedURLException"
                )
                IOException -> ToastUtils.showToast(
                    context,
                    "IOException"
                )
                else -> {
                }
            }
            if (dialog != null) {
                dialog!!.dismiss()
            }
        }
    }

    /**
     * 得到安装路径
     * @return
     */
    internal val filedir: File
        get() {
            val sd = Environment.getExternalStorageDirectory()
            val path = sd.path + "/RedEnvelope"
            val filedir = File(path)
            if (!filedir.exists()) {
                filedir.mkdir()
                Log.e("Update", "新建一个文件夹")
            }
            return filedir
        }

    /**
     * 检测更新
     */
    fun update() {
        getCurrentVersion()
        if (type == Click) {
            dialog = ProgressDialog.show(context, "", "检测版本信息，请稍后...", true, true)
        }
        Thread(runnable).start()

    }

    /**
     * 检测版本信息
     */
    private fun checkVersion() {
        try {
            val url = URL(check_update)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10 * 0//设置超时时间10秒
            val inputStream = connection.inputStream
            infos = UpdateInfoParser.getUpdateInfo(inputStream)
            for (i in infos.indices) {
                val j = infos[i].versionCode
                if (j > temp) {
                    temp = j
                    position = i
                }
            }
            LogUtils.d("temp:" + temp)
            LogUtils.d("versionCode:" + versionCode)
            if (temp > versionCode) {
                Log.i(TAG, "有新的的版本" + infos[position].versionName)
                newVersion = true
                //                PreferencesUtils.setUseStatus(false);//设置不可用
                versionName = infos[position].versionName
                handler.sendEmptyMessage(CAN_BE_UPDATED)
            } else {
                Log.i(TAG, "当前已是最新版本")
                handler.sendEmptyMessage(NO_NEED_TO_UPDATE)
            }
        } catch (e: MalformedURLException) {
            handler.sendEmptyMessage(-1)
            e.printStackTrace()
        } catch (e: IOException) {
            handler.sendEmptyMessage(-2)
            e.printStackTrace()
        }

    }

    /**
     * 获取当前版本号
     */
    private fun getCurrentVersion() {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            versionCode = info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    /**
     * 显示检测更新对话框
     */
    private fun showUpdateDialog() {
        val utils = DialogUtils


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
        DialogUtils.showAlertDialog(context,
            "检测到新版本，是否更新",
            infos[position].description,
            object : DialogUtils.OnClickSureListener {

                override fun onClickSure() {
                    ToastUtil.Builder(context).setText("后台下载中...").build()
                    Download()
                }
            },
            object : DialogUtils.OnClickCancelListener {

                override fun onClickCancel() {
                    if (dialog != null) {
                        dialog!!.dismiss()
                    }
                }
            })
    }


    /**
     * 下载新版APK
     */
    fun Download() {
        val filedir = filedir
        file = File(filedir, "QiangHongBao$versionName.apk")
        //		file=new File(Environment.getExternalStorageDirectory()+"/HuasTools","HuasTools"+versionName);
        val task = DownloadAsyncTask(context, handler, file!!)
        task.execute(infos[position].apkUrl)
    }

    /**
     * 安装APK
     */
    private fun InstallApk() {
        if (file!!.exists()) {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }

    fun hasNewVersion(): Boolean {
        return newVersion
    }

    companion object {
        private val TAG = "Version"

        //获取版本信息来检测更新网址
        //    public static final String check_update="http://xbdcc.ml/xbd/tools/QiangHongBao/update.xml";
        //    public static final String check_update="http://xbdcc.github.io/xiaobudian/HuasTools/update.xml";
        val check_update = "http://xbdcc.cn/GrabRedEnvelope/update.xml"
//        val check_update = "http://xbdcc.github.io/xiaobudian/QiangHongBao/update.xml"

        val MalformedURLException = 4
        val IOException = 5
        /**
         * 检查更新
         */
        val CAN_BE_UPDATED = 8
        val NO_NEED_TO_UPDATE = 9
        val DOWNLOAD_FINISHED = 10

        var Auto = 1
        var Click = 2
    }
}
