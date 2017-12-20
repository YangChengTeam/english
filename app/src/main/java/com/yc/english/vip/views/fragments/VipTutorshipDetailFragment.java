package com.yc.english.vip.views.fragments;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/11/28 15:08.
 */

public class VipTutorshipDetailFragment extends BaseFragment {
    @BindView(R.id.iv_tutorship_detail)
    ImageView ivTutorshipDetail;
    @BindView(R.id.iv_advantage)
    ImageView ivAdvantage;
    @BindView(R.id.tv_tip)
    TextView tvTip;



    private boolean isVip;

    @Override
    public void init() {
        setBottomMargin(isVip);
        Glide.with(this).load(R.mipmap.vip_tutorship_advantage).into(ivAdvantage);
        Glide.with(this).load(R.mipmap.vip_tutorship_advantage_detail).into(ivTutorshipDetail);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_tutorship_detail;
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
        ivTutorshipDetail.invalidate();
    }

    private void setBottomMargin(boolean flag) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)tvTip.getLayoutParams();
        if (flag) {
            layoutParams.bottomMargin = 0;
            tvTip.setLayoutParams(layoutParams);
        }
    }




}
