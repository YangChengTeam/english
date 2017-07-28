package com.yc.english.base.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/28.
 */

public class SharePopupWindow extends BasePopupWindow {

    @BindView(R.id.si_weixin_friend)
    ShareItemView mWxFriendShareItemView;

    @BindView(R.id.si_weixin_line)
    ShareItemView mWxLineShareItemView;

    @BindView(R.id.si_share_qq)
    ShareItemView mShareQQFriendShareItemView;

    @BindView(R.id.si_share_weibo)
    ShareItemView mWeiBoFriendShareItemView;

    @BindView(R.id.si_share_qzone)
    ShareItemView mQzoneFriendShareItemView;

    @BindView(R.id.si_share_class)
    ShareItemView mClassFriendShareItemView;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;


    public SharePopupWindow(Activity context) {
        super(context);
    }

    @Override
    public void init() {
        setOutsideTouchable(true);

        RxView.clicks(mCancelTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
               dismiss();
            }
        });

        List<ShareItemView> shareItemViews = new ArrayList<>();
        shareItemViews.add(mWxFriendShareItemView);
        shareItemViews.add(mWeiBoFriendShareItemView);
        shareItemViews.add(mShareQQFriendShareItemView);
        shareItemViews.add(mWxLineShareItemView);
        shareItemViews.add(mQzoneFriendShareItemView);
        shareItemViews.add(mClassFriendShareItemView);

        for(int i = 0 ; i < shareItemViews.size() ; i++) {
            final  ShareItemView shareItemView = shareItemViews.get(i);
            RxView.clicks(shareItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    shareItemView.setTag(0);
                    if (onShareItemClickListener != null) {
                        onShareItemClickListener.onClick(shareItemView);
                    }
                    dismiss();
                }
            });
        }

    }
    private OnShareItemClickListener onShareItemClickListener;

    public OnShareItemClickListener getOnShareItemClickListener() {
        return onShareItemClickListener;
    }

    public void setOnShareItemClickListener(OnShareItemClickListener onShareItemClickListener) {
        this.onShareItemClickListener = onShareItemClickListener;
    }

    public interface OnShareItemClickListener {
        void onClick(View view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_ppw_share;
    }

    public void show(View view){
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }



    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }
}
