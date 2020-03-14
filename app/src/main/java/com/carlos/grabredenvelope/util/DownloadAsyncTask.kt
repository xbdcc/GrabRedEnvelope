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
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

/**
 * Github: https://github.com/xbdcc/.
 * Created by 小不点 on 2016/2/20.
 */
class DownloadAsyncTask(
    private val context: Context,
    private val handler: Handler,
    private val file: File
) : AsyncTask<String, Int, Void>() {
    private lateinit var dialog: ProgressDialog

    /**
     * 显示下载进度条
     */
    override fun onPreExecute() {
        dialog = ProgressDialog(context)
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setCanceledOnTouchOutside(false)
        //		dialog.setMax(100);
        //		dialog.setProgressNumberFormat("%1d kb/%2d kb");
        dialog.setTitle("下载进度")
        if (show()!!) {
            dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        dialog.setMessage("请保持网络稳定，等待下载完成再取消！")
        //        dialog.setMessage("下载途中有时可能会卡住一段时间，请耐心等待一会儿，下载完会提示是否安装，还有许多待完善的地方");
        dialog.show()
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
        dialog.progress = values[0]!!
    }

    override fun onPostExecute(result: Void) {
        dialog.dismiss()
    }
}
