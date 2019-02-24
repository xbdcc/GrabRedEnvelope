package com.carlos.grabredenvelope.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.carlos.grabredenvelope.R

/**
 * Created by 小不点 on 2016/2/22.
 */
class AboutActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setMenuTitle("使用说明")

        back()


        val b_donate_me = findViewById<Button>(R.id.b_donate_me)
        b_donate_me.setOnClickListener {
            startActivity(Intent(this@AboutActivity, RewardActivity::class.java))
        }

    }

}
