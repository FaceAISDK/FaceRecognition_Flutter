package com.zenglb.framework.news.handylife;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.ArticlesResult;
import java.util.List;

/**
 * HandyLifeAdapter
 *
 * Created by zlb on 2018/3/23.
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<ArticlesResult.ArticlesBean, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param context
     * @param data
     */
    public NewsAdapter(Context context, List<ArticlesResult.ArticlesBean> data) {
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
    protected void convert(BaseViewHolder helper, ArticlesResult.ArticlesBean item) {
        switch (helper.getItemViewType()) {
            case ArticlesResult.DEFAULT:
                helper.setText(R.id.topic, "["+item.getAuthor().getNickname()+"] "+item.getArticle().getTitle());
                helper.setText(R.id.description, item.getArticle().getDigest());
                Glide.with(mContext).load(item.getArticle().getCover()).into((ImageView) helper.getView(R.id.image));
                break;

        }
    }

}
