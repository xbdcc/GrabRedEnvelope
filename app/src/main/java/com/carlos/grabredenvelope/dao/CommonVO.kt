package com.carlos.grabredenvelope.dao

import cn.bmob.v3.BmobObject
import com.carlos.cutils.util.DeviceUtils
import com.carlos.grabredenvelope.MyApplication

/**
 * Created by Carlos on 2019/2/23.
 */
class CommonVO(
    var imei: String = DeviceUtils.getImei(MyApplication.instance.applicationContext),
    var broad: String =DeviceUtils.getPhoneBroad(),
    var model: String = DeviceUtils.getPhoneModel(),
    var osVersion: String = DeviceUtils.getOSVersion(),
    var screenResolution: String = DeviceUtils.getScreenResolution(MyApplication.instance.applicationContext),
    var appVersionName : String = DeviceUtils.getAppVersionName(MyApplication.instance.applicationContext),
    var appVersionCode : Int = DeviceUtils.getAppVersionCode(MyApplication.instance.applicationContext)
) : BmobObject()