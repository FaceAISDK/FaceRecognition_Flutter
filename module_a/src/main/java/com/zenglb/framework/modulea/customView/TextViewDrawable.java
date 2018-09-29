package com.zenglb.framework.modulea.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zenglb.framework.modulea.R;

/**
 * 文字的上下左右有文字
 *
 *
 * 需要添加一个文字和图之间的距离
 */
public class TextViewDrawable extends AppCompatTextView {

    private int drawableLeftWidth, drawableTopWidth, drawableRightWidth, drawableBottomWidth;
    private int drawableLeftHeight, drawableTopHeight, drawableRightHeight, drawableBottomHeight;
    private boolean isAliganCenter=true;
    private boolean isDwMath_content=false;
    private int mWidth, mHeight;

    public TextViewDrawable(Context context) {
        this(context, null);
    }

    public TextViewDrawable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewDrawable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewDrawable);
        drawableLeftWidth = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableLeftWidth, 0);
        drawableTopWidth = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableTopWidth, 0);
        drawableRightWidth = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableRightWidth, 0);
        drawableBottomWidth = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableBottomWidth, 0);
        drawableLeftHeight = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableLeftHeight, 0);
        drawableTopHeight = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableTopHeight, 0);
        drawableRightHeight = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableRightHeight, 0);
        drawableBottomHeight = typedArray.getDimensionPixelSize(R.styleable.TextViewDrawable_drawableBottomHeight, 0);
        isAliganCenter = typedArray.getBoolean(R.styleable.TextViewDrawable_isAliganCenter, true);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        Drawable drawableTop = drawables[1];
        Drawable drawableRight = drawables[2];
        Drawable drawableBottom = drawables[3];
        if (drawableLeft != null) {
            setDrawable(drawableLeft, 0, drawableLeftWidth, drawableLeftHeight);
        }
        if (drawableTop != null) {
            setDrawable(drawableTop, 1, drawableTopWidth, drawableTopHeight);
        }
        if (drawableRight != null) {
            setDrawable(drawableRight, 2, drawableRightWidth, drawableRightHeight);
        }
        if (drawableBottom != null) {
            setDrawable(drawableBottom, 3, drawableBottomWidth, drawableBottomHeight);
        }
        this.setCompoundDrawables(drawableLeft,drawableTop,drawableRight,drawableBottom);
    }

    private void setDrawable(Drawable drawable, int tag, int drawableWidth, int drawableHeight) {
        //获取图片实际长宽
        int width = drawableWidth == 0 ? drawable.getIntrinsicWidth() : drawableWidth;
        int height = drawableHeight == 0 ? drawable.getIntrinsicHeight() : drawableHeight;
        int left = 0, top = 0, right = 0, bottom = 0;
        switch (tag) {
            case 0:
            case 2:
                left = 0;
                top = isAliganCenter ? 0 : -getLineCount() * getLineHeight() / 2 + getLineHeight() / 2;
                right = width;
                bottom = top + height;
                break;
            case 1:
                left =isAliganCenter ? 0: -mWidth/2+width/2;
                top = 0;
                right = left+width;
                bottom =top+height;
                break;
        }
        drawable.setBounds(left, top, right, bottom);
    }
}
