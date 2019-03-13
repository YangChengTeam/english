package com.yc.english.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.yc.english.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by zhangkai on 2017/7/28.
 */

public class ShareItemView extends BaseView {

    @BindView(R.id.iv_icon)
    ImageView mIconImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    public ShareItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);

        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            mIconImageView.setImageDrawable(iconSrc);
        }

        CharSequence title = a.getText(R.styleable.tab_item_text);
        if (title != null) {
            mTitleTextView.setText(title);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.share_item_view;
    }
}
