package com.carlos.grabredenvelope.fragment

import android.view.View
import com.carlos.cutils.base.fragment.CBaseFragment

/**
 * Created by Carlos on 2020-01-21.
 */
open class BaseFragment(val layoutid: Int) : CBaseFragment() {

    override fun initView(view: View) {}

    override fun layoutId(): Int {
        return layoutid
    }

}