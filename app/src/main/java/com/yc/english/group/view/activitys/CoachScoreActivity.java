package com.yc.english.group.view.activitys;

import android.widget.Button;

import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.view.widget.BuySuccessDialog;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.vip.utils.VipDialogHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2018/3/12.
 */

public class CoachScoreActivity extends FullScreenActivity {

    @BindView(R.id.btn_score)
    Button mScoreButton;

    private UserInfo userInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coach_score;
    }

    @Override
    public void init() {
        mToolbar.setTitle("提分辅导");
        mToolbar.showNavigationIcon();

        RxView.clicks(mScoreButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (userInfo != null && userInfo.getIsVip() == 2) {
                    BuySuccessDialog buySuccessDialog = new BuySuccessDialog(CoachScoreActivity.this);
                    buySuccessDialog.show();
                } else {
                    VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("CoachScoreActivity onResume --->");
        userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null && userInfo.getIsVip() == 2) {
            mScoreButton.setText("已购买提分辅导");
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void paySuccess(String success) {
        BuySuccessDialog buySuccessDialog = new BuySuccessDialog(this);
        buySuccessDialog.show();
    }

}
