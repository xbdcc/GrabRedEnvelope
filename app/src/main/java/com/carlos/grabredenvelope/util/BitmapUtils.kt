package com.carlos.grabredenvelope.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

/**
 * Created by 小不点 on 2016/2/23.
 */
object BitmapUtils {

    fun saveBitmap(context: Context, output: File, bitmap: Bitmap): Boolean {
        if (output.exists()) {
            return false
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(output)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            insertMedia(context, output, "image/jpeg")
            return true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: Exception) {
                }

            }
        }


    }

    /** 加入到系统的图库中 */
    private fun insertMedia(context: Context, output: File, mime: String) {
        try {
            val values = ContentValues()
            values.put(MediaStore.Video.Media.DATA, output.absolutePath)
            values.put(MediaStore.Video.Media.MIME_TYPE, mime)
            //记录到系统媒体数据库，通过系统的gallery可以即时查看
            context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            //通知系统去扫描
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(output)))
        } catch (e: Exception) {
        }

    }
}
