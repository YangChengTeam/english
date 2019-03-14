package com.yc.junior.english.vip.views.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.junior.english.R;

import butterknife.BindView;
import yc.com.base.BaseView;


/**
 * Created by wanglin  on 2017/11/28 17:05.
 */

public class TutorshipDetailView extends BaseView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_bad_content)
    TextView tvBadContent;
    @BindView(R.id.tv_good_content)
    TextView tvGoodContent;


    public TutorshipDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.tutorship_detail_view);

        String title = ta.getString(R.styleable.tutorship_detail_view_title);
        int titleBgColor = ta.getColor(R.styleable.tutorship_detail_view_title_bg_color, 0xFB7474);
        String badContent = ta.getString(R.styleable.tutorship_detail_view_bad_content);
        String goodContent = ta.getString(R.styleable.tutorship_detail_view_good_content);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(badContent)) {
            tvBadContent.setText(badContent);
        }
        if (!TextUtils.isEmpty(goodContent)) {
            tvGoodContent.setText(goodContent);
        }
        llTitle.setBackgroundColor(titleBgColor);


        ta.recycle();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_tutorship_view;
    }
}
