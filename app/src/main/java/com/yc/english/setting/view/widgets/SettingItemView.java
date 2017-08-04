package com.yc.english.setting.view.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseView;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/28.
 */

public class SettingItemView extends BaseView {

    @BindView(R.id.iv_icon)
    ImageView mIconImageView;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_info)
    TextView mInfoTextView;

    @BindView(R.id.iv_arrow_right)
    ImageView mArrowRightImageView;

    public SettingItemView(Context context, AttributeSet attrs) {
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

        CharSequence info = a.getText(R.styleable.tab_item_desc);
        if (info != null) {
            mInfoTextView.setText(info);
        }
    }

    public void setIcon(int resId) {
        right(mIconImageView);
        mIconImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    private void right(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0, 0, SizeUtils.dp2px(15), 0);
        view.setLayoutParams(layoutParams);
        mArrowRightImageView.setVisibility(View.GONE);
    }

    public void rightInfo() {
        right(mInfoTextView);
    }

    public String getInfo(){
        return mInfoTextView.getText().toString();
    }

    public void setInfo(String info) {
        mInfoTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_999));
        mInfoTextView.setText(info);
    }
    public void setHintInfo(String info) {
        mInfoTextView.setHintTextColor(ContextCompat.getColor(getContext(), R.color.group_red_fe908c));
        mInfoTextView.setHint(info);
    }

    public void setAvatar(int resId) {
        ViewGroup.LayoutParams layoutParams = mIconImageView.getLayoutParams();
        int wh = 45;
        layoutParams.width = SizeUtils.dp2px(wh);
        layoutParams.height = SizeUtils.dp2px(wh);
        mIconImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), resId));
    }

    public ImageView getAvatarImageView(){
        return mIconImageView;
    }

    public void hideArrow() {
        mArrowRightImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_setting_item_view;
    }
}
