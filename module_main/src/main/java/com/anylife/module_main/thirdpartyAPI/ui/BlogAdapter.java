package com.anylife.module_main.thirdpartyAPI.ui;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.anylife.module_main.R;
import com.anylife.module_main.thirdpartyAPI.model.Blog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 *
 * Created by zlb on 2018/3/23.
 */
public class BlogAdapter extends BaseQuickAdapter<Blog, BaseViewHolder> {

    public BlogAdapter(List<Blog> results) {
        super(R.layout.blog_item, results);
    }

    /**
     * data --> UI
     *
     * @param helper
     * @param item
     */
    @Override
    protected void convert(BaseViewHolder helper, Blog item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvDescription, item.getDescription());

        if (item.getThumbnail() != null && !TextUtils.isEmpty(item.getThumbnail())) {
            String url = item.getThumbnail();
            helper.getView(R.id.ivThumbnail).setTag(R.id.ivThumbnail, url);
            Glide.with(mContext)
                    .load(url)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (helper.getView(R.id.ivThumbnail).getTag().equals(url)) {
                                ((ImageView) helper.getView(R.id.ivThumbnail)).setImageDrawable(resource);
                            }
                            return false;
                        }
                    }).into((ImageView) helper.getView(R.id.ivThumbnail));
        }


    }

}
