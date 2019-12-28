package com.carlos.cposed

import android.app.ProgressDialog
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.carlos.cutils.util.AppUtils
import com.carlos.cutils.util.InstallUtils
import com.carlos.cutils.util.LogUtils
import com.carlos.cutils.util.NetUtils
import com.carlos.grabredenvelope.BuildConfig
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.util.UpdateInfoParser
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

/**
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2018/12/18.
 */
internal class UpdateDefaultImpl {

    lateinit var context: WeakReference<Context>
    lateinit var savePath: String
    lateinit var downloadUrl: String
    lateinit var loadingDialog: ProgressDialog
    lateinit var progressDialog: ProgressDialog
    private var isDownloading = false

    private val GET_UPDATE_DATA_FAIL = -2
    private val DOWNLOAD_FAIL = -1
    private val GET_UPDATE_DATA_SUCCESS = 0

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            UpdateDefaultImpl()
        }
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                GET_UPDATE_DATA_FAIL -> Toast.makeText(
                    context.get(),
                    "获3333333取更新数据失败",
                    Toast.LENGTH_SHORT
                ).show()
                GET_UPDATE_DATA_SUCCESS -> {
                    download(this)
                }
                DOWNLOAD_FAIL -> Toast.makeText(context.get(), "下载失败", Toast.LENGTH_SHORT).show()
                in 1..100 -> {
                    progressDialog.progress = msg.what
                    if (msg.what == 100) {
                        install()
                        progressDialog.dismiss()
                    }
                }
            }

        }
    }

    fun updateDefault(context: Context, url: String) {
        this.context = WeakReference(context)
        showLoadingDialog()
        Thread {
            //            checkGithub(url)
            checkVersion(url)
        }.start()
    }


    fun checkGithub(url: String) {
//        val url = "https://api.github.com/repos/xbdcc/GrabRedEnvelope/releases/latest"

        val data = NetUtils().get(url)
        LogUtils.d("post request data:$data")

        if (data.isEmpty()) {
            LogUtils.d("post request body: null")
            handler.sendEmptyMessage(GET_UPDATE_DATA_FAIL)
            return
        }

        val jsonObject = JSONObject(data)
        val lastVersion = jsonObject.getString("tag_name")
        val asset = jsonObject.getJSONArray("assets").getJSONObject(0)
        val appName = asset.getString("name")
        val appVersion = BuildConfig.VERSION_NAME
        LogUtils.d("appVersion：" + appVersion + "lastVersion：" + lastVersion)

        if (appVersion >= lastVersion) {
            LogUtils.d("this is the latest version.")
            return
        }

        downloadUrl = asset.getString("browser_download_url")
        LogUtils.d("downL:" + downloadUrl)
        savePath = context.get()?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path +
                File.separator + appName
        LogUtils.d("savePath:$savePath")
        handler.sendEmptyMessage(GET_UPDATE_DATA_SUCCESS)
    }

    private fun checkVersion(url: String) {
        try {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10 * 0//设置超时时间10秒
            val inputStream = connection.inputStream
            val infos = UpdateInfoParser.getUpdateInfo(inputStream)

            var temp = 0
            var position = 0
            var versionName = ""
            for (i in infos.indices) {
                val j = infos[i].versionCode
                if (j > temp) {
                    temp = j
                    position = i
                }
            }
            if (temp > AppUtils.getVersionCode(MyApplication.instance.applicationContext)) {
                LogUtils.i("有新的的版本" + infos[position].versionName)
                //                PreferencesUtils.setUseStatus(false);//设置不可用
                versionName = infos[position].versionName

                downloadUrl = infos[position].apkUrl
                LogUtils.d("downL:" + downloadUrl)
                savePath =
                    context.get()?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path +
                            File.separator + infos[position].versionName + ".apk"
                LogUtils.d("savePath:$savePath")

                handler.sendEmptyMessage(GET_UPDATE_DATA_SUCCESS)

            } else {
                LogUtils.i("当前已是最新版本")
//                handler.sendEmptyMessage(Update.NO_NEED_TO_UPDATE)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtils.e("error:", e)
//            handler.sendEmptyMessage(GET_UPDATE_DATA_FAIL)
//            e.printStackTrace()
//            LogUtils.e("error:", e)
        }

    }

    fun download(handler: Handler) {
        loadingDialog.dismiss()
        if (isDownloading)
            return
        isDownloading = true
        showProgressDialog()
        Thread {
            NetUtils().download(downloadUrl, savePath, handler)
            isDownloading = false
        }.start()
    }

    fun install() {
        InstallUtils.install(MyApplication.instance.applicationContext, savePath)
    }

//    private fun showUpdateDialog() {
//
//        AlertDialog.Builder(context).setTitle("版本更新").setMessage(updateResultVO.updateDesc)
//            .setPositiveButton("确定") { dialog, which ->
//                download(handler)
//            }
//            .setNegativeButton("取消") { dialog, which ->
//                LogUtils.d("cancel")
//                if (updateResultVO.forceUpdate)
//                    System.exit(0)
//            }
//            .setCancelable(false)
//            .create().show()
//    }
//
//    private fun showInstallDialog() {
//        val alertDialog = AlertDialog.Builder(context).setTitle("下载完成").setMessage("是否安装？")
//            .setPositiveButton("确定", null)
//            .setNegativeButton("取消") { dialog, which ->
//                progressDialog.dismiss()
//                if (updateResultVO.forceUpdate)
//                    System.exit(0)
//            }
//            .setCancelable(false)
//            .create()
//        alertDialog.show()
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//            progressDialog.dismiss()
//            install()
//            if (!updateResultVO.forceUpdate)
//                alertDialog.dismiss()
//        }
//    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog(context.get())
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("下载进度")
        progressDialog.show()
    }


    fun showLoadingDialog() {
        if (context.get() == null) return
        loadingDialog = ProgressDialog(context.get())
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setCancelable(false)
        loadingDialog.setTitle("加载中。。。")
        loadingDialog.show()
    }


}