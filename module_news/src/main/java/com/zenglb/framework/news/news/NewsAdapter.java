package com.zenglb.framework.news.news;

import android.content.Context;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zenglb.framework.news.R;
import com.zenglb.framework.news.http.result.AppBean;

import java.util.List;

/**
 * HandyLifeAdapter，简单写法，还没有处理图片加载错乱的问题
 * <p>
 * Created by zlb on 2018/3/23.
 */
public class NewsAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param context
     * @param data
     */
    public NewsAdapter(Context context, List<AppBean> data) {
        super(R.layout.news_list_item, data);
    }



    /**
     * data --> UI
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(BaseViewHolder helper, AppBean item) {
        helper.setText(R.id.topic, item.getWho());
        helper.setText(R.id.description, item.getDesc());
        helper.setText(R.id.create, item.getCreatedAt());
    }

}
