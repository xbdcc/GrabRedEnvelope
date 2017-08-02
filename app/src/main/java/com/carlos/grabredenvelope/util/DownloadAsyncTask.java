package com.carlos.grabredenvelope.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class DownloadAsyncTask extends AsyncTask<String, Integer, Void> {

    private Context context;
    private Handler handler;
    private File file;
    private ProgressDialog dialog;



    public DownloadAsyncTask(Context context, Handler handler,File file) {
        super();
        this.context = context;
        this.handler = handler;
        this.file=file;
    }

    /**
     *显示下载进度条
     */
    @Override
    protected void onPreExecute() {
        dialog=new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
//		dialog.setMax(100);
//		dialog.setProgressNumberFormat("%1d kb/%2d kb");
        dialog.setTitle("下载进度");
        if (show()) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.setMessage("请保持网络稳定，等待下载完成再取消！");
//        dialog.setMessage("下载途中有时可能会卡住一段时间，请耐心等待一会儿，下载完会提示是否安装，还有许多待完善的地方");
        dialog.show();
    }

    public Boolean show(){
        return true;
    }

    @Override
    protected Void doInBackground(String... arg0) {

        try {
            URL url=new URL(arg0[0]);

            Log.e("ggggggggggg", arg0[0]);

            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10*000);

            connection.setRequestMethod("GET");
            connection .setRequestProperty("Accept-Encoding", "identity");
            //conn.setRequestProperty("Referer", urlString);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");

            connection.connect();
            InputStream inputStream=connection.getInputStream();
            int length=connection.getContentLength();

            if (file.exists()) {
                file.delete();
            }
            FileOutputStream outputStream=new FileOutputStream(file);
            Log.i("总长度", length+"");

            int count=0;
            int numread=0;
            byte[] buffer=new byte[2048];
            int progress=0;
            int lastprogress=0;
            while(true){
                numread=inputStream.read(buffer);
                count+=numread;

                Log.i("Download---", "numread=="+numread+" aaaaaaaaaaaaaaaaaaa");
                Log.i("Download---", "count=="+count+" aaaaaaaaaaaaaaaaaaa");

                progress=(int)(((float)count/length)*100);
                Log.i("Download---", "progress=="+progress+" lastprogress=="+lastprogress);
                if (progress>=lastprogress+1) {
                    lastprogress=progress;
                    publishProgress(progress);
                }
                if (numread<=0) {
                    handler.sendEmptyMessage(Update.DOWNLOAD_FINISHED);
                    break;
                }
                outputStream.write(buffer, 0, numread);
            }
            outputStream.close();
            inputStream.close();

            connection.disconnect();

        } catch (MalformedURLException e) {
            handler.sendEmptyMessage(Update.MalformedURLException);
            e.printStackTrace();
        } catch (IOException e) {
            handler.sendEmptyMessage(Update.IOException);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        dialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        dialog.dismiss();
    }
}
