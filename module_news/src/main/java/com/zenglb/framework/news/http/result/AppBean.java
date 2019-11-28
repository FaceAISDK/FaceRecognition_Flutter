package com.zenglb.framework.news.http.result;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public  class AppBean implements Parcelable {
    /**
     * _id : 5771ce2b421aa931d274f244
     * createdAt : 2016-06-28T09:08:59.622Z
     * desc : 一款类似豆瓣读书的APP，提供一个书籍查看、搜索、交流的平台，数据来自豆瓣（爬虫），后端LeanCloud。
     * images : ["http://img.gank.io/0b7e425d-f61c-4eff-ae9c-8b5613020be9","http://img.gank.io/c0cc0c8b-17b6-4321-bfdc-1cb0732edd4d","http://img.gank.io/0203a555-edc1-4577-b83b-42a8a723dd87","http://img.gank.io/8671a3f2-5546-4f0b-9d5c-538ad51aa8cf"]
     * publishedAt : 2019-08-06T11:58:37.715Z
     * source : web
     * type : App
     * url : https://github.com/Blankeer/SoleBooks
     * used : true
     * who : 潇湘剑雨
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;
    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.createdAt);
        dest.writeString(this.desc);
        dest.writeString(this.publishedAt);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeByte(this.used ? (byte) 1 : (byte) 0);
        dest.writeString(this.who);
        dest.writeStringList(this.images);
    }

    public AppBean() {
    }

    protected AppBean(Parcel in) {
        this._id = in.readString();
        this.createdAt = in.readString();
        this.desc = in.readString();
        this.publishedAt = in.readString();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = in.readByte() != 0;
        this.who = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Parcelable.Creator<AppBean> CREATOR = new Parcelable.Creator<AppBean>() {
        @Override
        public AppBean createFromParcel(Parcel source) {
            return new AppBean(source);
        }

        @Override
        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };
}