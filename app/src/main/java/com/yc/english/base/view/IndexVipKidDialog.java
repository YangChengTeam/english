package com.yc.english.base.view;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

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

    public IndexVipKidDialog(Context context) {
        super(context);
        mContext = context;
        getWindow().setWindowAnimations(R.style.vip_style);
    }

    @Override
    public void init() {
        RxView.clicks(ivVipkid).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
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
