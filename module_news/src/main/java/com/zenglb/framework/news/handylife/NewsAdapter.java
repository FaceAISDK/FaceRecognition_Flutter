package com.zenglb.framework.news.handylife;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.ArticlesResult;
import com.zenglb.framework.news.http.result.NewsBean;

import java.util.List;

/**
 * HandyLifeAdapter，简单写法，还没有处理图片加载错乱的问题
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<NewsBean, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param context
     * @param data
     */
    public NewsAdapter(Context context, List<NewsBean> data) {
        super(data);
        addItemType(0, R.layout.news_list_item);
        addItemType(1, R.layout.news_list_item);   //其实这里都是1
    }


    /**
     * data --> UI
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(BaseViewHolder helper, NewsBean item) {
        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.topic, item.getTitle());
                helper.setText(R.id.description, item.getDigest());

                if (item.getPicInfo() != null && item.getPicInfo().size() > 0 && !TextUtils.isEmpty(item.getPicInfo().get(0).getUrl())) {
                    //需要解决视图加载错乱的问题
                    String url = item.getPicInfo().get(0).getUrl();
                    helper.getView(R.id.image).setTag(R.id.image, url);
                    Glide.with(mContext)
                            .load(url)
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    if (helper.getView(R.id.image).getTag().equals(url)) {
                                        ((ImageView) helper.getView(R.id.image)).setImageDrawable(resource);
                                    }
                                    return false;
                                }
                            }).into((ImageView) helper.getView(R.id.image));
                }
                break;

        }

    }

}
