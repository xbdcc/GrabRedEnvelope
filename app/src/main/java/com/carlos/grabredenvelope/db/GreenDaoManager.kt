package com.carlos.grabredenvelope.data
import com.carlos.grabredenvelope.MyApplication
import com.carlos.grabredenvelope.db.DaoMaster
import com.carlos.grabredenvelope.db.DaoSession

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

/**
 * Github: https://github.com/xbdcc/.
 * Created by caochang on 2017/8/27.
 */

class GreenDaoManager {
    var master: DaoMaster //以一定的模式管理Dao类的数据库对象
    var session: DaoSession //管理制定模式下的所有可用Dao对象
    var newSession: DaoSession? = null
        get() {
            session = master.newSession()
            return session
        }

    init {
        val devOpenHelper = DaoMaster.DevOpenHelper(MyApplication.instance.applicationContext, "grabredenvelope", null)
        master = DaoMaster(devOpenHelper.writableDatabase)
        session = master.newSession()
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GreenDaoManager()
        }
    }
}