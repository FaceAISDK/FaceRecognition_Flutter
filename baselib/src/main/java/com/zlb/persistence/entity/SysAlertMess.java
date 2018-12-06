package com.zlb.persistence.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 总是需要有一个类注解了Entity
 *
 * Created by zenglb on 2017/3/27.
 */
@Entity
public class SysAlertMess {
    private boolean hasReaded=false;  //是否已经读过了
    private String title;
    private String actionType;
    private String message;         // 消息详情
    private String detailUrl;       // 消息详情URL,不喜欢actionId 的命名方式

    private Long created;           //创建时间
    private Long out_time;          //过期时间，超过这个时间就不弹了
    @Id
    private Long id;             // int --> long    要不要id序列化


    @Generated(hash = 1078731677)
    public SysAlertMess(boolean hasReaded, String title, String actionType,
            String message, String detailUrl, Long created, Long out_time,
            Long id) {
        this.hasReaded = hasReaded;
        this.title = title;
        this.actionType = actionType;
        this.message = message;
        this.detailUrl = detailUrl;
        this.created = created;
        this.out_time = out_time;
        this.id = id;
    }
    @Generated(hash = 536878860)
    public SysAlertMess() {
    }



    public boolean getHasReaded() {
        return this.hasReaded;
    }
    public void setHasReaded(boolean hasReaded) {
        this.hasReaded = hasReaded;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getActionType() {
        return this.actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDetailUrl() {
        return this.detailUrl;
    }
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
    public Long getCreated() {
        return this.created;
    }
    public void setCreated(Long created) {
        this.created = created;
    }
    public Long getOut_time() {
        return this.out_time;
    }
    public void setOut_time(Long out_time) {
        this.out_time = out_time;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

   


}
