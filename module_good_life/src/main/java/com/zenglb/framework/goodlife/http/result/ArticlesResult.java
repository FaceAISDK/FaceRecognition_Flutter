package com.zenglb.framework.goodlife.http.result;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class ArticlesResult {
    public static final int DEFAULT = 1;
    public static final int IMG_ONLY = 2;

    private List<ArticlesBean> articles;

    public List<ArticlesBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticlesBean> articles) {
        this.articles = articles;
    }

    public static class ArticlesBean implements MultiItemEntity {
        /**
         * article : {"author":"","title":"警察到家里连续来了12次，这个秘密足以让他倾家荡产","cover":"http://mmbiz.qpic.cn/mmbiz_jpg/nl3wSw8glLqHYzqeCE7C9Y5ODoDiczicCqh723uhzY3DMxSqf0YYWeYEGEKEL3FeDofyhwfQ0n2rEwc806PCDfPA/0?wx_fmt=jpeg","content_url":"http://mp.weixin.qq.com/s?__biz=MjM5NjMzOTk2MA==&mid=2652735785&idx=5&sn=3fa487352732519623ba8524de15bdd8&chksm=bd03e0be8a7469a890ccfab1c5b9cdfbbb8681aa26e0d07d56bbe4e13eac493de1e224f27103&scene=27#wechat_redirect","datetime":1534000800,"digest":"执着","fileid":505252089}
         * authorId : 5b575d8c58e5c4583338d99d
         * _id : {"$oid":"5b6f00f56c56fab5ca345f1f"}
         * author : {"code":"jxtvdsxc","qr":"https://mp.weixin.qq.com/mp/qrcode?scene=10000005&size=102&__biz=MjM5NjMzOTk2MA==&mid=2652733637&idx=1&sn=c689d74ddea4ed866264ae2f6ec3f902&send_time=","headimg":"http://wx.qlogo.cn/mmhead/Q3auHgzwzM7SAicoqZR86wbK3Vyn7KLF8DrlOlNibbe66uVByVV6l55w/0","profile_desc":"来自腾讯微博认证资料：都市现场栏目官方微博，江西王牌民生新闻栏目，在同时段所有频道所有节目中，收视率第一（包括影视剧）全国十大民生新闻栏目，全国五大地面频道。 @都市现场","_id":{"$oid":"5b575d8c58e5c4583338d99d"},"nickname":"都市现场"}
         */

        private ArticleBean article;
        private String authorId;
        private IdBean _id;
        private AuthorBean author;

        @Override
        public int getItemType() {
            return DEFAULT;
        }


        public ArticleBean getArticle() {
            return article;
        }

        public void setArticle(ArticleBean article) {
            this.article = article;
        }

        public String getAuthorId() {
            return authorId;
        }

        public void setAuthorId(String authorId) {
            this.authorId = authorId;
        }

        public IdBean get_id() {
            return _id;
        }

        public void set_id(IdBean _id) {
            this._id = _id;
        }

        public AuthorBean getAuthor() {
            return author;
        }

        public void setAuthor(AuthorBean author) {
            this.author = author;
        }

        public static class ArticleBean {
            /**
             * author :
             * title : 警察到家里连续来了12次，这个秘密足以让他倾家荡产
             * cover : http://mmbiz.qpic.cn/mmbiz_jpg/nl3wSw8glLqHYzqeCE7C9Y5ODoDiczicCqh723uhzY3DMxSqf0YYWeYEGEKEL3FeDofyhwfQ0n2rEwc806PCDfPA/0?wx_fmt=jpeg
             * content_url : http://mp.weixin.qq.com/s?__biz=MjM5NjMzOTk2MA==&mid=2652735785&idx=5&sn=3fa487352732519623ba8524de15bdd8&chksm=bd03e0be8a7469a890ccfab1c5b9cdfbbb8681aa26e0d07d56bbe4e13eac493de1e224f27103&scene=27#wechat_redirect
             * datetime : 1534000800
             * digest : 执着
             * fileid : 505252089
             */

            private String author;
            private String title;
            private String cover;
            private String content_url;
//            private long datetime;
            private String digest;
//            private long fileid;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getContent_url() {
                return content_url;
            }

            public void setContent_url(String content_url) {
                this.content_url = content_url;
            }

//            public long getDatetime() {
//                return datetime;
//            }
//
//            public void setDatetime(long datetime) {
//                this.datetime = datetime;
//            }

            public String getDigest() {
                return digest;
            }

            public void setDigest(String digest) {
                this.digest = digest;
            }

//            public long getFileid() {
//                return fileid;
//            }
//
//            public void setFileid(long fileid) {
//                this.fileid = fileid;
//            }
        }

        public static class IdBean {
            /**
             * $oid : 5b6f00f56c56fab5ca345f1f
             */

            private String $oid;

            public String get$oid() {
                return $oid;
            }

            public void set$oid(String $oid) {
                this.$oid = $oid;
            }
        }

        public static class AuthorBean {
            /**
             * code : jxtvdsxc
             * qr : https://mp.weixin.qq.com/mp/qrcode?scene=10000005&size=102&__biz=MjM5NjMzOTk2MA==&mid=2652733637&idx=1&sn=c689d74ddea4ed866264ae2f6ec3f902&send_time=
             * headimg : http://wx.qlogo.cn/mmhead/Q3auHgzwzM7SAicoqZR86wbK3Vyn7KLF8DrlOlNibbe66uVByVV6l55w/0
             * profile_desc : 来自腾讯微博认证资料：都市现场栏目官方微博，江西王牌民生新闻栏目，在同时段所有频道所有节目中，收视率第一（包括影视剧）全国十大民生新闻栏目，全国五大地面频道。 @都市现场
             * _id : {"$oid":"5b575d8c58e5c4583338d99d"}
             * nickname : 都市现场
             */

            private String code;
            private String qr;
            private String headimg;
            private String profile_desc;
            private IdBeanX _id;
            private String nickname;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getQr() {
                return qr;
            }

            public void setQr(String qr) {
                this.qr = qr;
            }

            public String getHeadimg() {
                return headimg;
            }

            public void setHeadimg(String headimg) {
                this.headimg = headimg;
            }

            public String getProfile_desc() {
                return profile_desc;
            }

            public void setProfile_desc(String profile_desc) {
                this.profile_desc = profile_desc;
            }

            public IdBeanX get_id() {
                return _id;
            }

            public void set_id(IdBeanX _id) {
                this._id = _id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public static class IdBeanX {
                /**
                 * $oid : 5b575d8c58e5c4583338d99d
                 */

                private String $oid;

                public String get$oid() {
                    return $oid;
                }

                public void set$oid(String $oid) {
                    this.$oid = $oid;
                }
            }
        }
    }
}
