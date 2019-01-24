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
public class SysAlertMess2 {
    @Id(autoincrement = true)
    private Long id;             // int --> long    要不要id序列化

    private String title;
    private String actionType;
    private String message;         // 消息详情
    private String detailUrl;       // 消息详情URL,不喜欢actionId 的命名方式


    @Generated(hash = 1949709738)
    public SysAlertMess2(Long id, String title, String actionType, String message,
            String detailUrl) {
        this.id = id;
        this.title = title;
        this.actionType = actionType;
        this.message = message;
        this.detailUrl = detailUrl;
    }

    @Generated(hash = 1903303945)
    public SysAlertMess2() {
    }


    public SysAlertMess2(String title, String actionType, String message, String detailUrl) {
        this.title = title;
        this.actionType = actionType;
        this.message = message;
        this.detailUrl = detailUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
