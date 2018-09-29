package com.zlb.http.result;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by zenglb on 2017/4/23.
 */
@Entity
public class StaffMsg {
    /**
     * id : 233
     * fullname : 张三
     * nickname : 张三疯
     * sex : male
     * mobile : 15011112222
     * created : 2015-11-24 18:49:49
     * updated : 2015-11-24 18:49:49
     * identity_id : 44011119910101666X
     * is_keeper : true
     * avatar_url : http://static.4009515151.com/avatar_url.jpg
     * contact_phones : ["15011112222"]
     * job_can_edit : true
     * role_identity : IDT_PROJECT
     */

    @Id
    private Long id;
    private String fullname;
    private String nickname;
    private String sex;
    private String mobile;
    private String created;
    private String updated;
    private String identity_id;
    private boolean is_keeper;
    private String avatar_url;
    private boolean job_can_edit;
    private String role_identity;
    @Transient
    private List<String> contact_phones;

    public StaffMsg(String mobile, String created) {
        this.mobile = mobile;
        this.created = created;
    }

    @Generated(hash = 1719145766)
    public StaffMsg(Long id, String fullname, String nickname, String sex,
                    String mobile, String created, String updated, String identity_id,
                    boolean is_keeper, String avatar_url, boolean job_can_edit,
                    String role_identity) {
        this.id = id;
        this.fullname = fullname;
        this.nickname = nickname;
        this.sex = sex;
        this.mobile = mobile;
        this.created = created;
        this.updated = updated;
        this.identity_id = identity_id;
        this.is_keeper = is_keeper;
        this.avatar_url = avatar_url;
        this.job_can_edit = job_can_edit;
        this.role_identity = role_identity;
    }

    @Generated(hash = 1838510106)
    public StaffMsg() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public boolean isIs_keeper() {
        return is_keeper;
    }

    public void setIs_keeper(boolean is_keeper) {
        this.is_keeper = is_keeper;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public boolean isJob_can_edit() {
        return job_can_edit;
    }

    public void setJob_can_edit(boolean job_can_edit) {
        this.job_can_edit = job_can_edit;
    }

    public String getRole_identity() {
        return role_identity;
    }

    public void setRole_identity(String role_identity) {
        this.role_identity = role_identity;
    }

    public List<String> getContact_phones() {
        return contact_phones;
    }

    public void setContact_phones(List<String> contact_phones) {
        this.contact_phones = contact_phones;
    }

    @Override
    public String toString() {
        return "StaffMsg{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", mobile='" + mobile + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", identity_id='" + identity_id + '\'' +
                ", is_keeper=" + is_keeper +
                ", avatar_url='" + avatar_url + '\'' +
                ", job_can_edit=" + job_can_edit +
                ", role_identity='" + role_identity + '\'' +
                ", contact_phones=" + contact_phones +
                '}';
    }

    public boolean getIs_keeper() {
        return this.is_keeper;
    }

    public boolean getJob_can_edit() {
        return this.job_can_edit;
    }

}
