package com.yc.junior.english.setting.view.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.BaseView;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/27.
 */

public class MenuItemView extends BaseView {

    @BindView(R.id.iv_icon)
    ImageView mIconImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    public MenuItemView(Context context, AttributeSet attrs) {
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
        return R.layout.setting_menu_item_view;
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }


}
