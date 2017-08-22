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
import com.yc.english.base.model.ShareInfo;

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

    private static ShareInfo.INFO mShareInfo;

    public static void setmShareInfo(ShareInfo.INFO mShareInfo) {
        SharePopupWindow.mShareInfo = mShareInfo;
    }

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
                        shareInfo(tmpI);
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


    private void shareInfo(int tag) {
        if (mShareInfo != null) {
            UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(mShareInfo.getTitle(), mShareInfo.getUrl(),
                    mShareInfo.getDesp(), R
                    .drawable
                    .share, getShareMedia(tag + ""));
        } else {
            String title = "说说英语APP上线啦！随时随地想学就学";
            String url = "http://mp.weixin.qq.com/s/JepGpluow-Zf6VhI0wMJEA";
            String desc = "说说英语自营首款APP学英语软件上线了，涵盖市面所有主流英语教材，配套各种版本教科书（完全免费），让你随时随地就能通过手机打开书本，进行学习，单词记忆。还有各种趣味方式助你学英语。";
            UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.drawable
                    .share, getShareMedia(tag + ""));
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

    @Override
    public int getAnimationID() {
        return R.style.share_anim;
    }

}
