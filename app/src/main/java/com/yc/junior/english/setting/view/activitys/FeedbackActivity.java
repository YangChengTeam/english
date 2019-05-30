package com.yc.junior.english.setting.view.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.AlertDialog;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.setting.contract.FeedbackContract;
import com.yc.junior.english.setting.presenter.FeedbackPersenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.TipsHelper;
import yc.com.blankj.utilcode.util.AppUtils;
import yc.com.blankj.utilcode.util.ClipboardUtils;
import yc.com.blankj.utilcode.util.KeyboardUtils;
import yc.com.blankj.utilcode.util.UIUitls;


/**
 * Created by zhangkai on 2017/7/24.
 */

public class FeedbackActivity extends FullScreenActivity<FeedbackPersenter> implements FeedbackContract.View {

    @BindView(R.id.et_context)
    EditText mContextEditView;

    @BindView(R.id.btn_complete)
    Button mCompleteButton;

    @BindView(R.id.ll_tel)
    LinearLayout llTel;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.tv_wx)
    TextView tvWx;


    @Override
    public void init() {
        mToolbar.setTitle("在线客服");
        mToolbar.showNavigationIcon();

        mPresenter = new FeedbackPersenter(this, this);

//        tvWx.setText(  Html.fromHtml("<u>  nuanjiguiren886</u>"));
//
        tvWx.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvWx.getPaint().setAntiAlias(true);

        RxView.clicks(mCompleteButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            KeyboardUtils.hideSoftInput(FeedbackActivity.this);
            CharSequence context = mContextEditView.getText().toString();
            mPresenter.postMessage(context.toString());
        });


        RxView.clicks(llTel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            AlertDialog alertDialog = new AlertDialog(FeedbackActivity.this);
            alertDialog.setDesc("拨打电话与客服进行沟通？");
            alertDialog.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:13164125027");
                intent.setData(data);
                startActivity(intent);
            });
            alertDialog.show();
        });

        RxView.clicks(llWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            ClipboardUtils.copyText(tvWx.getText().toString().trim());
            TipsHelper.tips(FeedbackActivity.this, "复制成功, 正在前往微信");
            UIUitls.postDelayed(1000, () -> {
                String weixin = "com.tencent.mm";
                if (AppUtils.isInstallApp(weixin)) {
                    AppUtils.launchApp(weixin);
                }
            });
        });

//        RxView.clicks(mWeixinTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                AlertDialog alertDialog = new AlertDialog(FeedbackActivity.this);
//                alertDialog.setDesc("打开微信、添加好友与客服进行沟通？");
//                alertDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ClipboardUtils.copyText("15926287915");
//                        TipsHelper.tips(FeedbackActivity.this, "复制成功, 正在前往微信");
//                        UIUitls.postDelayed(1000, new Runnable() {
//                            @Override
//                            public void run() {
//                                String weixin = "com.tencent.mm";
//                                if (AppUtils.isInstallApp(weixin)) {
//                                    AppUtils.launchApp(weixin);
//                                }
//                            }
//                        });
//                    }
//                });
//                alertDialog.show();
//            }
//        });

//        RxView.clicks(mQQRelativeLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                AlertDialog alertDialog = new AlertDialog(FeedbackActivity.this);
//                alertDialog.setDesc("打开QQ与客服进行沟通？");
//                alertDialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (checkQQInstalled(FeedbackActivity.this)) {
//                            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + 18508609;
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                        } else {
//                            TipsHelper.tips(FeedbackActivity.this, "手机QQ未安装或该版本不支持");
//                        }
//                    }
//                });
//                alertDialog.show();
//            }
//        });
    }

    public boolean checkQQInstalled(Context context) {
        Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_feedback_new;
    }

    @Override
    public void emptyView() {
        mContextEditView.setText("");
    }


}
