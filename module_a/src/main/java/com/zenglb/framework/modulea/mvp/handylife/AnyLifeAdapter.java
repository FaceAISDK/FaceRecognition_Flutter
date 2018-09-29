package com.zenglb.framework.modulea.mvp.handylife;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zenglb.framework.modulea.R;
import com.zenglb.framework.modulea.http.result.AnyLifeResult;

import java.util.List;

/**
 * HandyLifeAdapter
 *
 * Created by zlb on 2018/3/23.
 */
public class AnyLifeAdapter extends BaseMultiItemQuickAdapter<AnyLifeResult, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param context
     * @param data
     */
    public AnyLifeAdapter(Context context, List<AnyLifeResult> data) {
        super(data);
        addItemType(AnyLifeResult.DEFAULT, R.layout.handylife_list_item);
        addItemType(AnyLifeResult.IMG_ONLY, R.layout.handylife_list_item_only_image);
    }

    /**
     * data --> UI
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(BaseViewHolder helper, AnyLifeResult item) {
        switch (helper.getItemViewType()) {
            case AnyLifeResult.DEFAULT:
                helper.setText(R.id.topic, item.getTilte());
                helper.setText(R.id.description, item.getDescription());
                Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.image));
                break;
            case AnyLifeResult.IMG_ONLY:

                // TODO: 2018/3/24  加载网络图片,应该再封装一层的，防止以后换图片加载库，
                Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.image));
                break;
        }
    }

}
