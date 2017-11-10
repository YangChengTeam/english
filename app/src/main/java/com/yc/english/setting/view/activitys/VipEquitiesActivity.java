package com.yc.english.setting.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.model.bean.Config;
import com.yc.english.setting.view.adapter.VIPEquitiesAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/11/8 11:19.
 */

public class VipEquitiesActivity extends FullScreenActivity {


    @BindView(R.id.recyclerView_open_vip)
    RecyclerView recyclerViewOpenVip;
    @BindView(R.id.recyclerView_surplus_days)
    RecyclerView recyclerViewSurplusDays;


    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.vip_equities));
        mToolbar.showNavigationIcon();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        long startTime = userInfo.getVip_end_time() - userInfo.getVip_start_time();
        Date date = new Date();
        long endTime = userInfo.getVip_end_time() - (date.getTime());
        int openDays = (int) (startTime / Config.MS_IN_A_DAY);
        int surplusDays = (int) (endTime / Config.MS_IN_A_DAY);
        if (Math.abs(surplusDays) > Math.abs(openDays)) {
            surplusDays = openDays;
        }

        recyclerViewOpenVip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        VIPEquitiesAdapter vipEquitiesAdapter = new VIPEquitiesAdapter(translateData(String.valueOf(Math.abs(openDays))));
        recyclerViewOpenVip.setAdapter(vipEquitiesAdapter);

        recyclerViewSurplusDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        VIPEquitiesAdapter vipEquitiesAdapter2 = new VIPEquitiesAdapter(translateData(String.valueOf(Math.abs(surplusDays))));
        recyclerViewSurplusDays.setAdapter(vipEquitiesAdapter2);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_equities;
    }


    private List<Integer> translateData(String openDays) {

        List<Integer> integerList = new ArrayList<>();

        for (int i = 0; i < openDays.length(); i++) {
            char d = openDays.charAt(i);
            integerList.add(Integer.parseInt(String.valueOf(d)));
        }
        return integerList;

    }


}
