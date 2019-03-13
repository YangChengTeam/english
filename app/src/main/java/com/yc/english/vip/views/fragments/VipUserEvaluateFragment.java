package com.yc.english.vip.views.fragments;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2017/11/28 15:08.
 */

public class VipUserEvaluateFragment extends BaseFragment {


    @BindView(R.id.iv_user_evaluate)
    ImageView ivUserEvaluate;
    private boolean isVip;


    @Override
    public void init() {
        setBottomMargin(isVip);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_user_evaluate;
    }


    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.USER_INFO)
            }
    )
    public void getUserInfo(UserInfo userInfo) {
        isVip = userInfo.getIsVip() != 0;
        setBottomMargin(isVip);
        ivUserEvaluate.invalidate();
    }

    private void setBottomMargin(boolean flag) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivUserEvaluate.getLayoutParams();
        if (flag) {
            layoutParams.bottomMargin = 0;
            ivUserEvaluate.setLayoutParams(layoutParams);
        }
    }
}
