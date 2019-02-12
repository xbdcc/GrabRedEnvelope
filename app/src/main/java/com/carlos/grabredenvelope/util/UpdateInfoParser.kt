package com.carlos.grabredenvelope.util

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by 小不点 on 2016/2/20.
 */
object UpdateInfoParser {

    private val TAG = "Version"

    fun getUpdateInfo(inputStream: InputStream): List<UpdateInfo>? {

        var infos: MutableList<UpdateInfo>? = null
        var info: UpdateInfo? = null

        val parser = Xml.newPullParser()
        try {
            parser.setInput(inputStream, "utf-8")
            var type = parser.eventType
            Log.i(TAG, "服务器版本信息")
            while (type != XmlPullParser.END_DOCUMENT) {
                when (type) {
                    XmlPullParser.START_DOCUMENT -> infos = ArrayList()
                    XmlPullParser.START_TAG -> if (parser.name == "information") {
                        info = UpdateInfo()
                    } else if ("versionCode" == parser.name) {
                        info!!.versionCode = Integer.valueOf(parser.nextText())
                        Log.i(TAG, info.versionCode.toString() + "")
                    } else if ("versionName" == parser.name) {
                        info!!.versionName = parser.nextText()
                        Log.i(TAG, info.versionName)
                    } else if ("apkUrl" == parser.name) {
                        info!!.apkUrl = parser.nextText()
                        Log.i(TAG, info.apkUrl)
                    } else if ("description" == parser.name) {
                        info!!.description = parser.nextText()
                        Log.i(TAG, info.description)
                    }
                    XmlPullParser.END_TAG -> if (parser.name == "information") {
                        if (info != null) {
                            infos!!.add(info)
                        }
                    }
                    else -> {
                    }
                }
                type = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return infos
    }

}