package com.yc.english.news.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.news.adapter.OrderItemAdapter;
import com.yc.english.news.view.widget.SpaceItemDecoration;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/11/2.
 */

public class ConfirmOrderActivity extends FullScreenActivity {

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.rv_order_list)
    RecyclerView mOrderRecyclerView;

    OrderItemAdapter mOrderItemAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void init() {
        mToolbar.setTitle("确认订单");
        //mToolbar.setMenuTitle(getString(R.string.edit_text));
        mToolbar.showNavigationIcon();

        List<CourseInfo> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setTitle("小学三年级英语第一单元视频课程同步辅导");
            courseInfo.setPv_num("12");
            courseInfo.setImg("");
            list.add(courseInfo);
        }

        mOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        mOrderItemAdapter = new OrderItemAdapter(list);
        mOrderItemAdapter.setFooterView(getFootView());
        mOrderRecyclerView.setAdapter(mOrderItemAdapter);
    }

    public View getFootView() {
        View footView = getLayoutInflater().inflate(R.layout.activity_order_foot, (ViewGroup) mOrderRecyclerView.getParent(), false);
        return footView;
    }

}
