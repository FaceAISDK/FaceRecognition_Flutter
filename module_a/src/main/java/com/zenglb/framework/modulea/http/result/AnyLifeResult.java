package com.zenglb.framework.modulea.http.result;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class AnyLifeResult implements MultiItemEntity {
    public static final int DEFAULT = 1;
    public static final int IMG_ONLY = 2;

    private String image;
    private String tilte; //这是错误的示范
    private String description;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getItemType() {
        if(TextUtils.isEmpty(tilte)&&TextUtils.isEmpty(description)){
            return IMG_ONLY;
        }else{
            return DEFAULT;
        }
    }
}
