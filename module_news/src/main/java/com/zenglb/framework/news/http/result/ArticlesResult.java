package com.zenglb.framework.news.http.result;

import java.util.List;

public class ArticlesResult {

    private List<NewsBean> tech;
    private List<NewsBean> auto;
    private List<NewsBean> money;
    private List<NewsBean> sports;
    private List<NewsBean> dy;
    private List<NewsBean> war;
    private List<NewsBean> ent;
    private List<NewsBean> toutiao;
    private List<NewsBean> home;

    public List<NewsBean> getTech() {
        return tech;
    }

    public void setTech(List<NewsBean> tech) {
        this.tech = tech;
    }



}
