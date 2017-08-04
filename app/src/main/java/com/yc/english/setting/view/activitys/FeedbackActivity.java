package com.yc.english.setting.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.ShareItemView;
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

    @BindView(R.id.si_qq)
    ShareItemView mQQShareItemView;

    @BindView(R.id.si_tel)
    ShareItemView mTelShareItemView;


    @Override
    public void init() {
        mToolbar.setTitle("意见反馈");
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

        RxView.clicks(mQQShareItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + 972403325;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        RxView.clicks(mTelShareItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                PhoneUtils.call("15902742354");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_feedback;
    }

    @Override
    public void emptyView() {
        mContextEditView.setText("");
    }
}
