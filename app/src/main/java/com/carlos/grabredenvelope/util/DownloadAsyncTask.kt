package com.carlos.grabredenvelope.util

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by 小不点 on 2016/2/20.
 */
class DownloadAsyncTask(private val context: Context, private val handler: Handler, private val file: File) : AsyncTask<String, Int, Void>() {
    private var dialog: ProgressDialog? = null

    /**
     * 显示下载进度条
     */
    override fun onPreExecute() {
        dialog = ProgressDialog(context)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog!!.setCanceledOnTouchOutside(false)
        //		dialog.setMax(100);
        //		dialog.setProgressNumberFormat("%1d kb/%2d kb");
        dialog!!.setTitle("下载进度")
        if (show()!!) {
            dialog!!.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        dialog!!.setMessage("请保持网络稳定，等待下载完成再取消！")
        //        dialog.setMessage("下载途中有时可能会卡住一段时间，请耐心等待一会儿，下载完会提示是否安装，还有许多待完善的地方");
        dialog!!.show()
    }

    fun show(): Boolean? {
        return true
    }

    override fun doInBackground(vararg arg0: String): Void? {

        try {
            val url = URL(arg0[0])

            Log.e("ggggggggggg", arg0[0])

            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10 * 0

            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept-Encoding", "identity")
            //conn.setRequestProperty("Referer", urlString);
            connection.setRequestProperty("Charset", "UTF-8")
            connection.setRequestProperty("Connection", "Keep-Alive")

            connection.connect()
            val inputStream = connection.inputStream
            val length = connection.contentLength

            if (file.exists()) {
                file.delete()
            }
            val outputStream = FileOutputStream(file)
            Log.i("总长度", length.toString() + "")

            var count = 0
            var numread = 0
            val buffer = ByteArray(2048)
            var progress = 0
            var lastprogress = 0
            while (true) {
                numread = inputStream.read(buffer)
                count += numread

                Log.i("Download---", "numread==$numread aaaaaaaaaaaaaaaaaaa")
                Log.i("Download---", "count==$count aaaaaaaaaaaaaaaaaaa")

                progress = (count.toFloat() / length * 100).toInt()
                Log.i("Download---", "progress==$progress lastprogress==$lastprogress")
                if (progress >= lastprogress + 1) {
                    lastprogress = progress
                    publishProgress(progress)
                }
                if (numread <= 0) {
                    handler.sendEmptyMessage(Update.DOWNLOAD_FINISHED)
                    break
                }
                outputStream.write(buffer, 0, numread)
            }
            outputStream.close()
            inputStream.close()

            connection.disconnect()

        } catch (e: MalformedURLException) {
            handler.sendEmptyMessage(Update.MalformedURLException)
            e.printStackTrace()
        } catch (e: IOException) {
            handler.sendEmptyMessage(Update.IOException)
            e.printStackTrace()
        }

        return null
    }

    override fun onProgressUpdate(vararg values: Int?) {
        dialog!!.progress = values[0]!!
    }

    override fun onPostExecute(result: Void) {
        dialog!!.dismiss()
    }
}
