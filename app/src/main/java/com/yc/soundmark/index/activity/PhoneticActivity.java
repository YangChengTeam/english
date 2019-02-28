package com.yc.soundmark.index.activity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.soundmark.base.widget.MainToolBar;
import com.yc.soundmark.index.contract.IndexContract;
import com.yc.soundmark.index.fragment.VipEquitiesFragment;
import com.yc.soundmark.index.model.domain.ContactInfo;
import com.yc.soundmark.index.presenter.IndexPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class PhoneticActivity extends BaseActivity<IndexPresenter> implements IndexContract.View {

    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_service_tel)
    TextView tvServiceTel;
    @BindView(R.id.tv_service_wechat)
    TextView tvServiceWechat;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_phonetic;
    }

    @Override
    public void init() {
        mPresenter = new IndexPresenter(this, this);

        mainToolbar.init(this, null);
        mainToolbar.setRightContainerVisible(false);
        mainToolbar.setTitle(getString(R.string.phonetic_introduce));

        RxView.clicks(ivVip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(PhoneticActivity.this, "VIP-click", "会员权益");
//                Intent intent = new Intent(getActivity(), VipEquitiesActivity.class);
//                startActivity(intent);
                VipEquitiesFragment vipEquitiesFragment = new VipEquitiesFragment();
                vipEquitiesFragment.show(getSupportFragmentManager(), "");

            }
        });

        RxView.clicks(tvServiceWechat).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("weixin", tvServiceWechat.getText().toString().trim()));
                gotoWeixin();
            }
        });

    }


    private void gotoWeixin() {

        try {
            ToastUtil.toast2(this, "复制成功，正在前往微信");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            ToastUtil.toast2(this, "检查到您手机没有安装微信，请安装后使用该功能");
        }

    }

    @Override
    public void showIndexInfo(ContactInfo contactInfo) {
        if (contactInfo != null) {
            if (!TextUtils.isEmpty(contactInfo.getTel()))
                tvServiceTel.setText(contactInfo.getTel());
            if (!TextUtils.isEmpty(contactInfo.getWeixin()))
                tvServiceWechat.setText(contactInfo.getWeixin());
        }

        String userId = getString(R.string.user_id);
        tvUserId.setText(String.format(userId, UserInfoHelper.getUid()));

    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

}
