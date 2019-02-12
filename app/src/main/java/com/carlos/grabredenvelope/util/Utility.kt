package com.carlos.grabredenvelope.util

import android.widget.ListView

/**
 * Created by 小不点 on 2016/2/14.
 * ScrollView嵌套ListView重新计算高度类
 */
object Utility {

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val adapter = listView.adapter ?: return
        var i = 0
        var j = 0
        while (true) {
            if (j >= adapter.count) {
                val params = listView.layoutParams
                params.height = i + listView.dividerHeight * (-1 + adapter.count)
                listView.layoutParams = params
                return
            }
            val view = adapter.getView(j, null, listView)
            view.measure(0, 0)
            i += view.measuredHeight
            j++
        }
    }

}