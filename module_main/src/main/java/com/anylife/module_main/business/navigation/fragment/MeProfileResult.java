package com.anylife.module_main.business.navigation.fragment;

import java.util.List;

public class MeProfileResult {

    private boolean job_can_edit;
    private String updated;
    private String role_identity;
    private String sex;
    private String identity_id;
    private String nickname;
    private int id;
    private String created;
    private String mobile;
    private int state;
    private String avatar_url;
    private String fullname;
    private boolean is_keeper;
    private List<?> contact_phones;

    public boolean isJob_can_edit() {
        return job_can_edit;
    }

    public void setJob_can_edit(boolean job_can_edit) {
        this.job_can_edit = job_can_edit;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getRole_identity() {
        return role_identity;
    }

    public void setRole_identity(String role_identity) {
        this.role_identity = role_identity;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentity_id() {
        return identity_id;
    }

    public void setIdentity_id(String identity_id) {
        this.identity_id = identity_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isIs_keeper() {
        return is_keeper;
    }

    public void setIs_keeper(boolean is_keeper) {
        this.is_keeper = is_keeper;
    }

    public List<?> getContact_phones() {
        return contact_phones;
    }

    public void setContact_phones(List<?> contact_phones) {
        this.contact_phones = contact_phones;
    }

    @Override
    public String toString() {
        return "MeProfileResult{" +
                "job_can_edit=" + job_can_edit +
                ", updated='" + updated + '\'' +
                ", role_identity='" + role_identity + '\'' +
                ", sex='" + sex + '\'' +
                ", identity_id='" + identity_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", id=" + id +
                ", created='" + created + '\'' +
                ", mobile='" + mobile + '\'' +
                ", state=" + state +
                ", avatar_url='" + avatar_url + '\'' +
                ", fullname='" + fullname + '\'' +
                ", is_keeper=" + is_keeper +
                ", contact_phones=" + contact_phones +
                '}';
    }
}
