
package com.zlb.persistence.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 一定要放在这不太开心啊
 *
 */
@Entity
public class Blog {
    /**
     * title : Background Limitations Android Oreo
     * pubDate : 2019-02-09 11:22:31
     * link : https://androidwave.com/background-limitations-android-oreo/
     * author : admin
     * thumbnail : https://androidwave.com/wp-content/uploads/2019/02/background-limitations-android-oreo-370x247.png
     * description : If you are looking at what exactly limitations are applied in background service in Android in Oreo. You are in right place.
     */

    private String title;
    private String pubDate;
    private String link;
    private String author;
    private String thumbnail;
    private String description;

    @Generated(hash = 1676889568)
    public Blog(String title, String pubDate, String link, String author, String thumbnail, String description) {
        this.title = title;
        this.pubDate = pubDate;
        this.link = link;
        this.author = author;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    @Generated(hash = 1388801592)
    public Blog() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
