package com.carlos.grabredenvelope.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Github: https://github.com/xbdcc/.
 * Created by Carlos on 2020/3/2.
 */
@Entity
public class DingDingRedEnvelope {
    @Id(autoincrement = true)
    private Long id;
    private long time = System.currentTimeMillis();
    private String count = "";
    @Generated(hash = 404457864)
    public DingDingRedEnvelope(Long id, long time, String count) {
        this.id = id;
        this.time = time;
        this.count = count;
    }
    @Generated(hash = 1551771541)
    public DingDingRedEnvelope() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public String getCount() {
        return this.count;
    }
    public void setCount(String count) {
        this.count = count;
    }
}
