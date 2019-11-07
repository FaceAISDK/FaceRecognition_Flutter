package com.zlb.persistence.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * WorkManger 模拟处理任务，log保存在数据库中
 */
@Entity
public class WorkLog {
    @Id(autoincrement = false)
    private Long id;             // int --> long    要不要id序列化

    private String title;
    private String content;
    private String tips;
    private String time;
    @Generated(hash = 1131852269)
    public WorkLog(Long id, String title, String content, String tips,
            String time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tips = tips;
        this.time = time;
    }
    @Generated(hash = 32197633)
    public WorkLog() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTips() {
        return this.tips;
    }
    public void setTips(String tips) {
        this.tips = tips;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }


}
