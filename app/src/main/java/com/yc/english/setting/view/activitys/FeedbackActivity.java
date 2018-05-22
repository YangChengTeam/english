package com.yc.english.setting.view.activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.setting.contract.FeedbackContract;
import com.yc.english.setting.presenter.FeedbackPersenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

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


    @Override
    public void init() {
        mToolbar.setTitle("在线客服");
        mToolbar.showNavigationIcon();

        mPresenter = new FeedbackPersenter(this, this);

        RxView.clicks(mCompleteButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                KeyboardUtils.hideSoftInput(FeedbackActivity.this);
                CharSequence context = mContextEditView.getText().toString();
                mPresenter.postMessage(context.toString());
            }
        });


        RxView.clicks(llTel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AlertDialog alertDialog = new AlertDialog(FeedbackActivity.this);
                alertDialog.setDesc("拨打电话与客服进行沟通？");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhoneUtils.call("15926287915");
                    }
                });
                alertDialog.show();
            }
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
