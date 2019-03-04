package com.carlos.grabredenvelope.old2016

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

/**
 * Created by 小不点 on 2016/5/29.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    private var db: SQLiteDatabase? = null

    private var mQQ_hongbao: QQ_Hongbao? = null

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)// 创建表
    }

    override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {

    }

    fun insert(qq_hongbao: QQ_Hongbao): String {

        db = this.writableDatabase

        val cv = ContentValues()
        cv.put(TIME, qq_hongbao.time)
        cv.put(SEND_INFO, qq_hongbao.send_info)
        cv.put(WISH_WORD, qq_hongbao.wish_word)
        cv.put(HB_COUNT_TV, qq_hongbao.hb_count_tv)
        val id = db!!.insert(TABLE_NAME, null, cv)// 插入数据
        return id.toString() + ""
    }

    fun delete(id: Int) {

        db = this.writableDatabase

        val whereClause = "_id=?"
        val whereArgs = arrayOf(Integer.toString(id))
        db!!.delete(TABLE_NAME, whereClause, whereArgs)//删除数据

    }

    fun update(id: Int, qq_hongbao: QQ_Hongbao): String {

        db = this.writableDatabase

        val cv = ContentValues()
        cv.put(TIME, qq_hongbao.time)
        cv.put(SEND_INFO, qq_hongbao.send_info)
        cv.put(WISH_WORD, qq_hongbao.wish_word)
        cv.put(HB_COUNT_TV, qq_hongbao.hb_count_tv)
        val whereClause = "_id=?"
        val whereArgs = arrayOf(Integer.toString(id))
        db!!.update(TABLE_NAME, cv, whereClause, whereArgs)// 插入数据
        return id.toString() + ""
    }

    fun get(): ArrayList<QQ_Hongbao> {

        val qq_hongbaos = ArrayList<QQ_Hongbao>()

        val db = this.readableDatabase

        val cur = db.query(
            TABLE_NAME, arrayOf(
                ID,
                TIME,
                SEND_INFO,
                WISH_WORD,
                HB_COUNT_TV
            ), null, null, null, null, null)

        val count = cur!!.count//获取个数
        if (cur != null && count >= 0) {
            if (cur.moveToFirst()) {//移动到第一个
                do {
                    mQQ_hongbao = QQ_Hongbao()
                    mQQ_hongbao!!.id = cur.getString(0)
                    mQQ_hongbao!!.time = cur.getString(1)
                    mQQ_hongbao!!.send_info = cur.getString(2)
                    mQQ_hongbao!!.wish_word = cur.getString(3)
                    mQQ_hongbao!!.hb_count_tv = cur.getString(4)
                    qq_hongbaos.add(mQQ_hongbao!!)

                } while (cur.moveToNext())//移动到下一个
            }
        }
        return qq_hongbaos
    }

    companion object {


        // private SQLiteDatabase mydb=null;//声明数据库
        private val DATABASE_NAME = "HongBaoRecord.db"// 数据库名称
        private val TABLE_NAME = "QQ_Hongbao"// 表名称
        private val ID = "_id"// 数据项
        private val TIME = "time"// 数据项
        private val SEND_INFO = "send_info"// 数据项
        private val WISH_WORD = "wish_word"// 数据项
        private val HB_COUNT_TV = "hb_count_tv"// 数据项
        private val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME
                + " (" + ID + " INTEGER PRIMARY KEY," + TIME + " TEXT," + SEND_INFO + " TEXT,"
                + WISH_WORD + " TEXT," + HB_COUNT_TV + " TEXT)")// 创建表的SQL语句

        private val DATABASE_VERSION = 1
    }


}
