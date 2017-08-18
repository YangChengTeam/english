package com.yc.english.base.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.share.UMShareImpl;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;

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


    private LoadingDialog loadingDialog;

    public SharePopupWindow(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
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
        shareItemViews.add(mWxLineShareItemView);
        shareItemViews.add(mQzoneFriendShareItemView);
        shareItemViews.add(mWxFriendShareItemView);
        shareItemViews.add(mShareQQFriendShareItemView);
        shareItemViews.add(mWeiBoFriendShareItemView);
        shareItemViews.add(mClassFriendShareItemView);


        for (int i = 0; i < shareItemViews.size(); i++) {
            final int tmpI = i;
            final ShareItemView shareItemView = shareItemViews.get(i);
            RxView.clicks(shareItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    shareItemView.setTag(tmpI + "");
                    if (onShareItemClickListener != null) {
                        onShareItemClickListener.onClick(shareItemView);
                    } else {
                        UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.drawable
                                .share, getShareMedia(shareItemView.getTag() + ""));
                    }
                    dismiss();
                }
            });
        }

        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            loadingDialog.setMessage("正在分享...");
            loadingDialog.show();

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            loadingDialog.dismiss();
            TipsHelper.tips(mContext, "分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            loadingDialog.dismiss();
            TipsHelper.tips(mContext, "分享有误");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            loadingDialog.dismiss();
            TipsHelper.tips(mContext, "取消发送");
        }
    };

    public SHARE_MEDIA getShareMedia(String tag) {
        if (tag.equals("0")) {
            return SHARE_MEDIA.WEIXIN_CIRCLE;
        }

        if (tag.equals("1")) {
            return SHARE_MEDIA.QZONE;
        }

        if (tag.equals("2")) {
            return SHARE_MEDIA.WEIXIN;
        }

        if (tag.equals("3")) {
            return SHARE_MEDIA.QQ;
        }

        return SHARE_MEDIA.SINA;
    }


    private String title = "开学送大礼, 免费领取英语学习神器";

    public SharePopupWindow setTitle(String title) {
        this.title = title;
        return this;
    }

    private String url = "http://en.qqtn.com/Public/activity/2.html";

    public SharePopupWindow setUrl(String url) {
        this.url = url;
        return this;
    }

    private String desc = "中小学英语学习神器， 专注教材点读、单词记忆、作业辅导、在线课堂。";

    public SharePopupWindow setDesc(String desc) {
        this.desc = desc;
        return this;
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

    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }

}
