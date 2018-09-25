package com.yc.english.base.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.UIUitls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;
import com.yc.english.R;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.SlideInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;


/**
 * Created by wanglin  on 2018/8/27 15:00.
 */
public class IndexVipKidDialog extends BaseDialog {

    @BindView(R.id.iv_vipkid)
    ImageView ivVipkid;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    private Context mContext;
    private String url = "https://activity.vipkid.com.cn/activity/register?channel_id=14753153&sourceId=803&channel_keyword=01";
    private String statics = "vipkid_dialog_click";
    private SlideInfo mInfo;

    public IndexVipKidDialog(Context context, SlideInfo dialogInfo) {
        super(context);
        mContext = context;
        this.mInfo = dialogInfo;
        getWindow().setWindowAnimations(R.style.vip_style);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (null != mInfo) {

            url = mInfo.getUrl();
            statics = mInfo.getStatistics();
            Glide.with(mContext).load(mInfo.getImg()).
                    apply(new RequestOptions().error(R.mipmap.base_no_wifi).diskCacheStrategy(DiskCacheStrategy.DATA)).into(ivVipkid);
        }
    }

    @Override
    public void init() {

        RxView.clicks(ivVipkid).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getContext(), statics);
                Intent intent = new Intent(mContext, WebActivity.class);
//                intent.putExtra("title", slideInfo.getTitle());
                intent.putExtra("url", url);
                mContext.startActivity(intent);
                dismiss();
            }
        });

        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_index_vipkid;
    }
}
