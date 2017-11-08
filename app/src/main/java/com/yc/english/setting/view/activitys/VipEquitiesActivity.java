package com.yc.english.setting.view.activitys;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/11/8 11:19.
 */

public class VipEquitiesActivity extends FullScreenActivity {

    @BindView(R.id.tv_open_first_day)
    TextView tvOpenFirstDay;
    @BindView(R.id.tv_open_two_day)
    TextView tvOpenTwoDay;
    @BindView(R.id.tv_open_three_day)
    TextView tvOpenThreeDay;
    @BindView(R.id.ll_open_days)
    LinearLayout llOpenDays;
    @BindView(R.id.tv_residue_first_day)
    TextView tvResidueFirstDay;
    @BindView(R.id.tv_residue_two_day)
    TextView tvResidueTwoDay;
    @BindView(R.id.tv_residue_three_day)
    TextView tvResidueThreeDay;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.vip_equities));
        mToolbar.showNavigationIcon();
       UserInfo userInfo=  UserInfoHelper.getUserInfo();
       long openTime= userInfo.getVip_end_time()- userInfo.getVip_start_time();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_equities;
    }


}
