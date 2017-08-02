package com.carlos.grabredenvelope.util;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小不点 on 2016/2/20.
 */
public class UpdateInfoParser {

    private final static String TAG="Version";

    public static List<UpdateInfo> getUpdateInfo(InputStream inputStream){

        List<UpdateInfo> infos=null;
        UpdateInfo info=null;

        XmlPullParser parser= Xml.newPullParser();
        try {
            parser.setInput(inputStream, "utf-8");
            int type=parser.getEventType();
            Log.i(TAG, "服务器版本信息");
            while(type!= XmlPullParser.END_DOCUMENT){
                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        infos=new ArrayList<UpdateInfo>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("information")) {
                            info=new UpdateInfo();
                        }else if ("versionCode".equals(parser.getName())) {
                            info.setVersionCode(Integer.valueOf(parser.nextText()));
                            Log.i(TAG, info.getVersionCode() + "");
                        }else if ("versionName".equals(parser.getName())) {
                            info.setVersionName(parser.nextText());
                            Log.i(TAG, info.getVersionName());
                        }else if ("apkUrl".equals(parser.getName())) {
                            info.setApkUrl(parser.nextText());
                            Log.i(TAG, info.getApkUrl());
                        }else if ("description".equals(parser.getName())) {
                            info.setDescription(parser.nextText());
                            Log.i(TAG, info.getDescription());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("information")) {
                            infos.add(info);
                        }
                        break;
                    default:
                        break;
                }
                type=parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return infos;
    }

}