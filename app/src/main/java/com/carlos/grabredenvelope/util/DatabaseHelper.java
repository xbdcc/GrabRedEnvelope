package com.carlos.grabredenvelope.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlos.grabredenvelope.dao.QQ_Hongbao;

import java.util.ArrayList;

/**
 * Created by 小不点 on 2016/5/29.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    // private SQLiteDatabase mydb=null;//声明数据库
    private final static String DATABASE_NAME = "HongBaoRecord.db";// 数据库名称
    private final static String TABLE_NAME = "QQ_Hongbao";// 表名称
    private final static String ID = "_id";// 数据项
    private final static String TIME = "time";// 数据项
    private final static String SEND_INFO = "send_info";// 数据项
    private final static String WISH_WORD = "wish_word";// 数据项
    private final static String HB_COUNT_TV = "hb_count_tv";// 数据项
    private final static String CREATE_TABLE="CREATE TABLE "+TABLE_NAME
            +" ("+ID+" INTEGER PRIMARY KEY,"+TIME+" TEXT," +SEND_INFO+" TEXT,"
            + WISH_WORD+" TEXT,"+ HB_COUNT_TV+" TEXT)";// 创建表的SQL语句

    private final static int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    private QQ_Hongbao mQQ_hongbao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);// 创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

    public String insert(QQ_Hongbao qq_hongbao) {

        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIME, qq_hongbao.getTime());
        cv.put(SEND_INFO, qq_hongbao.getSend_info());
        cv.put(WISH_WORD, qq_hongbao.getWish_word());
        cv.put(HB_COUNT_TV, qq_hongbao.getHb_count_tv());
        long id=db.insert(TABLE_NAME, null, cv);// 插入数据
        return id+"";
    }

    public void delete(int id) {

        db = this.getWritableDatabase();

        String whereClause="_id=?";
        String[] whereArgs={Integer.toString(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);//删除数据

    }

    public String update(int id,QQ_Hongbao qq_hongbao) {

        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIME, qq_hongbao.getTime());
        cv.put(SEND_INFO, qq_hongbao.getSend_info());
        cv.put(WISH_WORD, qq_hongbao.getWish_word());
        cv.put(HB_COUNT_TV, qq_hongbao.getHb_count_tv());
        String whereClause="_id=?";
        String[] whereArgs={Integer.toString(id)};
        db.update(TABLE_NAME, cv, whereClause, whereArgs);// 插入数据
        return id+"";
    }

    public ArrayList<QQ_Hongbao> get() {

        ArrayList<QQ_Hongbao> qq_hongbaos=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.query(TABLE_NAME, new String[] { ID, TIME, SEND_INFO, WISH_WORD,HB_COUNT_TV}, null,
                null, null, null, null);

        int count=cur.getCount();//获取个数
        if (cur!=null&&count>=0) {
            if (cur.moveToFirst()) {//移动到第一个
                do {
                    mQQ_hongbao=new QQ_Hongbao();
                    mQQ_hongbao.setId(cur.getString(0));
                    mQQ_hongbao.setTime(cur.getString(1));
                    mQQ_hongbao.setSend_info(cur.getString(2));
                    mQQ_hongbao.setWish_word(cur.getString(3));
                    mQQ_hongbao.setHb_count_tv(cur.getString(4));
                    qq_hongbaos.add(mQQ_hongbao);

                } while (cur.moveToNext());//移动到下一个
            }
        }
        return qq_hongbaos;
    }


}
