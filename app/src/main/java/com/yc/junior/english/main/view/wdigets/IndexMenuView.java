package com.yc.junior.english.main.view.wdigets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.junior.english.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexMenuView extends BaseView {
    @BindView(R.id.iv_icon)
    ImageView mIconImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_desc)
    TextView mDescTextView;

    public IndexMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);

        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            mIconImageView.setImageDrawable(iconSrc);
        }

        CharSequence title = a.getText(R.styleable.tab_item_text);
        if(title != null){
            mTitleTextView.setText(title);
        }

        CharSequence desc = a.getText(R.styleable.tab_item_desc);
        if(desc != null){
            mDescTextView.setText(desc);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.main_view_index_menu;
    }
}
