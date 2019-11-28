package com.zenglb.framework.news.http.result;

import java.util.List;

/**
 * 这APi 有点意思
 *
 */
public class HotNewsResult {

    private List<AppBean> Android;
    private List<AppBean> App;
    private List<AppBean> iOS;
    private List<AppBean> 休息视频;
    private List<AppBean> 前端;
    private List<AppBean> 拓展资源;
    private List<AppBean> 瞎推荐;
    private List<AppBean> 福利;

    public List<AppBean> getAndroid() {
        return Android;
    }

    public void setAndroid(List<AppBean> Android) {
        this.Android = Android;
    }

    public List<AppBean> getApp() {
        return App;
    }

    public void setApp(List<AppBean> App) {
        this.App = App;
    }

    public List<AppBean> getIOS() {
        return iOS;
    }

    public void setIOS(List<AppBean> iOS) {
        this.iOS = iOS;
    }

    public List<AppBean> get休息视频() {
        return 休息视频;
    }

    public void set休息视频(List<AppBean> 休息视频) {
        this.休息视频 = 休息视频;
    }

    public List<AppBean> get前端() {
        return 前端;
    }

    public void set前端(List<AppBean> 前端) {
        this.前端 = 前端;
    }

    public List<AppBean> get拓展资源() {
        return 拓展资源;
    }

    public void set拓展资源(List<AppBean> 拓展资源) {
        this.拓展资源 = 拓展资源;
    }

    public List<AppBean> get瞎推荐() {
        return 瞎推荐;
    }

    public void set瞎推荐(List<AppBean> 瞎推荐) {
        this.瞎推荐 = 瞎推荐;
    }

    public List<AppBean> get福利() {
        return 福利;
    }

    public void set福利(List<AppBean> 福利) {
        this.福利 = 福利;
    }


}
