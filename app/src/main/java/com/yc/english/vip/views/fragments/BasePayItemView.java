package com.yc.english.vip.views.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;

import butterknife.BindView;
import yc.com.base.BaseView;

/**
 * Created by wanglin  on 2017/11/24 15:07.
 */

public class BasePayItemView extends BaseView {
    @BindView(R.id.vip_icon)
    ImageView vipIcon;
    @BindView(R.id.vip_content)
    TextView vipContent;
    private Drawable drawable;
    private CharSequence content;
    private float textSize;
    private int textColor;


    public BasePayItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.vip_item, 0, 0);

        try {
            drawable = ta.getDrawable(R.styleable.vip_item_vip_icon);
            content = ta.getText(R.styleable.vip_item_vip_content);


            textSize = ta.getDimension(R.styleable.vip_item_vip_content_size, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                    getResources().getDisplayMetrics()));
            textColor = ta.getColor(R.styleable.vip_item_vip_content_color, ContextCompat.getColor(context, R.color.black));
            if (drawable != null) {
                vipIcon.setImageDrawable(drawable);
            }
            if (!TextUtils.isEmpty(content)) {
                vipContent.setText(content);
            }
            vipContent.setTextColor(textColor);
            vipContent.getPaint().setTextSize(textSize);
        } finally {
            ta.recycle();
        }

    }

    public void setContentAndIcon(CharSequence content, int resId) {
        if (!TextUtils.isEmpty(content))
            vipContent.setText(content);
        if (resId != 0) {
            vipIcon.setImageResource(resId);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_pay_popup_item;
    }


}
