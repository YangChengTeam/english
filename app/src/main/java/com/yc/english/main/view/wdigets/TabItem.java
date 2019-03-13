package com.yc.english.main.view.wdigets;

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
 * Created by zhangkai on 16/9/29.
 */
public class TabItem extends BaseView {
    @BindView(R.id.icon)
    ImageView ivIcon;

    @BindView(R.id.title)
    TextView tvTitle;

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab_item);
        CharSequence title = a.getText(R.styleable.tab_item_text);
        if (title != null) {
            tvTitle.setText(title);
        }
        Drawable iconSrc = a.getDrawable(R.styleable.tab_item_src);
        if (iconSrc != null) {
            ivIcon.setImageDrawable(iconSrc);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_view_tab_item;
    }

    public void selected(Drawable src) {
        ivIcon.setImageDrawable(src);
        tvTitle.setTextColor(getResources().getColor(R.color.primary));
    }

    public void normal(Drawable src) {
        ivIcon.setImageDrawable(src);
        tvTitle.setTextColor(getResources().getColor(R.color.gray_999));
    }
}
