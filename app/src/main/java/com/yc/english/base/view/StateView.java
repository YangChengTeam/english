package com.yc.english.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/7.
 */

public class StateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView mLoadingImageView;

    @BindView(R.id.tv_message)
    TextView mMessageTextView;

    @BindView(R.id.btn_refresh)
    Button mRefreshButton;

    private View mContextView;

    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_view_state;
    }

    public void hide() {
        setVisibility(View.GONE);
        if (mContextView != null) {
            mContextView.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(View contextView) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(1080/3);
        layoutParams.height = SizeUtils.dp2px(408/3);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;

        setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        mContextView.setVisibility(View.GONE);
        Glide.with(this).load(R.mipmap.base_loading).into(mLoadingImageView);
    }

    public void showNoData(View contextView, String message) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(312/3);
        layoutParams.height = SizeUtils.dp2px(370/3);
        layoutParams.setMargins(0, SizeUtils.dp2px(130), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        mContextView.setVisibility(View.GONE);
        mMessageTextView.setText(message);
        Glide.with(this).load(R.mipmap.base_no_data).into(mLoadingImageView);
    }

    public void showNoData(View contextView) {
        showNoData(contextView, "暂没数据");
    }

    public void showNoNet(View contextView, String message, final OnClickListener onClickListener) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(312/3);
        layoutParams.height = SizeUtils.dp2px(370/3);
        layoutParams.setMargins(0, SizeUtils.dp2px(130), 0, 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mContextView.setVisibility(View.GONE);
        mMessageTextView.setText(message);
        Glide.with(this).load(R.mipmap.base_no_wifi).into(mLoadingImageView);
        RxView.clicks(mRefreshButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClickListener.onClick(mRefreshButton);
            }
        });
    }
}
