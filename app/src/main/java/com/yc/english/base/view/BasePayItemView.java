package com.yc.english.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.utils.UIUitls;
import com.yc.english.R;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import butterknife.BindView;

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


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.vip_item);

        drawable = ta.getDrawable(R.styleable.vip_item_vip_icon);
        content = ta.getText(R.styleable.vip_item_vip_content);

        textSize = ta.getDimensionPixelSize(R.styleable.vip_item_vip_content_size, 10);
        textColor = ta.getColor(R.styleable.vip_item_vip_content_color, ContextCompat.getColor(context, R.color.black));
        if (drawable != null) {
            vipIcon.setImageDrawable(drawable);
        }
        if (!TextUtils.isEmpty(content)) {
            vipContent.setText(content);
        }
        vipContent.setTextSize(textSize);
        vipContent.setTextColor(textColor);
        ta.recycle();

    }

    @Override
    public int getLayoutId() {
        return R.layout.base_pay_popup_item;
    }

}
