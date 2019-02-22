package com.carlos.grabredenvelope

import cn.bmob.v3.BmobObject
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.carlos.cutils.util.LogUtils
import org.junit.Test

/**
 * Created by Carlos on 2019/2/22.
 */
class BmobTest {

    @Test
    fun testBmob() {
        val person = Person("xbd",25)
        person.save(object : SaveListener<String>() {
            override fun done(p0: String?, p1: BmobException?) {
                if(p1==null){
                    LogUtils.d("添加数据成功，返回objectId为：$p0")
                }else{
                    LogUtils.e("创建数据失败：" ,p1)
                }
            }
        })
    }

    @Test
    fun queryData() {

    }

    data class Person(
        var username: String,
        var age: Int
    ) : BmobObject()
}
