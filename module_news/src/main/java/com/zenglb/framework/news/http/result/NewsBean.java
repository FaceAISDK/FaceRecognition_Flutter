package com.zenglb.framework.news.http.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

public  class NewsBean implements MultiItemEntity, Parcelable {
    /**
     * liveInfo : null
     * tcount : 0
     * picInfo : [{"ref":null,"width":null,"url":"http://cms-bucket.ws.126.net/2019/11/05/8a9fca902b594c3a99c0c0285adfe9dc.jpeg","height":null}]
     * docid : ET6URV4P001081EI
     * videoInfo : null
     * channel : home
     * link : https://3g.163.com/all/article/ET6URV4P001081EI.html
     * source : 易起看家居
     * title : 四川林坡村90%的村民住进别墅 金碧辉煌超讲究
     * type : doc
     * imgsrcFrom : null
     * imgsrc3gtype : 1
     * unlikeReason : 重复、旧闻/6,内容质量差/6
     * isTop : null
     * digest : 要说脱贫成功，四川松潘县林坡村应该是一个灰常成功的栗子。毕竟
     * typeid :
     * addata : null
     * tag :
     * category : 家居
     * ptime : 2019-11-05 07:02:30
     */

    private int tcount;
    private String docid;
    private String channel;
    private String link;
    private String source;
    private String title;
    private String type;
    private int imgsrc3gtype;
    private String unlikeReason;
    private String digest;
    private String typeid;
    private String tag;
    private String category;
    private String ptime;
    private List<PicInfoBeanXXXXXXX> picInfo;



    public int getTcount() {
        return tcount;
    }

    public void setTcount(int tcount) {
        this.tcount = tcount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }


    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getImgsrc3gtype() {
        return imgsrc3gtype;
    }

    public void setImgsrc3gtype(int imgsrc3gtype) {
        this.imgsrc3gtype = imgsrc3gtype;
    }

    public String getUnlikeReason() {
        return unlikeReason;
    }

    public void setUnlikeReason(String unlikeReason) {
        this.unlikeReason = unlikeReason;
    }


    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public List<PicInfoBeanXXXXXXX> getPicInfo() {
        return picInfo;
    }

    public void setPicInfo(List<PicInfoBeanXXXXXXX> picInfo) {
        this.picInfo = picInfo;
    }





    @Override
    public int getItemType() {
        return 0;
    }

    public static class PicInfoBeanXXXXXXX {
        /**
         * ref : null
         * width : null
         * url : http://cms-bucket.ws.126.net/2019/11/05/8a9fca902b594c3a99c0c0285adfe9dc.jpeg
         * height : null
         */

        private Object ref;
        private Object width;
        private String url;
        private Object height;

        public Object getRef() {
            return ref;
        }

        public void setRef(Object ref) {
            this.ref = ref;
        }

        public Object getWidth() {
            return width;
        }

        public void setWidth(Object width) {
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Object getHeight() {
            return height;
        }

        public void setHeight(Object height) {
            this.height = height;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tcount);
        dest.writeString(this.docid);
        dest.writeString(this.channel);
        dest.writeString(this.link);
        dest.writeString(this.source);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeInt(this.imgsrc3gtype);
        dest.writeString(this.unlikeReason);
        dest.writeString(this.digest);
        dest.writeString(this.typeid);
        dest.writeString(this.tag);
        dest.writeString(this.category);
        dest.writeString(this.ptime);
        dest.writeList(this.picInfo);
    }

    public NewsBean() {
    }

    protected NewsBean(Parcel in) {
        this.tcount = in.readInt();
        this.docid = in.readString();
        this.channel = in.readString();
        this.link = in.readString();
        this.source = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.imgsrc3gtype = in.readInt();
        this.unlikeReason = in.readString();
        this.digest = in.readString();
        this.typeid = in.readString();
        this.tag = in.readString();
        this.category = in.readString();
        this.ptime = in.readString();
        this.picInfo = new ArrayList<PicInfoBeanXXXXXXX>();
        in.readList(this.picInfo, PicInfoBeanXXXXXXX.class.getClassLoader());
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel source) {
            return new NewsBean(source);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}