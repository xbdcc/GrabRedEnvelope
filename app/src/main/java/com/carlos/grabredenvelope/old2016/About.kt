package com.carlos.grabredenvelope.old2016

import android.app.Activity
import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.carlos.grabredenvelope.R
import com.carlos.grabredenvelope.util.BitmapUtils
import java.io.File

/**
 * Created by 小不点 on 2016/2/22.
 */
class About : Activity() {

    private var ib_back: ImageButton? = null
    private var b_donate_me: Button? = null

    /**
     * 得到安装路径
     * @return
     */
    internal val filedir: File
        get() {
            val sd = Environment.getExternalStorageDirectory()
            val path = sd.path + "/QiangHongBao"
            val filedir = File(path)
            if (!filedir.exists()) {
                filedir.mkdir()
                Log.e("Update", "新建一个文件夹")
            }
            return filedir
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)
        back()


        b_donate_me = findViewById(R.id.b_donate_me) as Button
        b_donate_me!!.setOnClickListener { showDonateDialog() }

    }

    fun back() {

        ib_back = findViewById(R.id.ib_back) as ImageButton
        ib_back!!.setOnClickListener { finish() }

    }

    /** 显示捐赠的对话框 */
    private fun showDonateDialog() {
        val dialog = Dialog(this, R.style.donate_dialog_theme)
        val view = layoutInflater.inflate(R.layout.donate_dialog_layout, null)
        view.setOnClickListener { dialog.dismiss() }
        view.setOnLongClickListener {
            val filedir = filedir
            val output = File(filedir, "xbd.jpg")
            if (!output.exists()) {
                val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.xbd)
                BitmapUtils.saveBitmap(this@About, output, bitmap)
            }
            Toast.makeText(this@About, "已保存到:" + output.absolutePath, Toast.LENGTH_LONG).show()
            true
        }
        dialog.setContentView(view)
        dialog.show()
    }
}
