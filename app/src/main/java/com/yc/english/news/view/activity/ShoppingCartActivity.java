package com.yc.english.news.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.news.adapter.CartItemAdapter;
import com.yc.english.news.view.widget.SpaceItemDecoration;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/11/2.
 */

public class ShoppingCartActivity extends FullScreenActivity {

    @BindView(R.id.rv_cart_list)
    RecyclerView mCartRecyclerView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.layout_pay)
    RelativeLayout mPayLayout;

    @BindView(R.id.layout_delete)
    RelativeLayout mDeleteLayout;

    @BindView(R.id.layout_pay_now)
    LinearLayout mPayNowLayout;

    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTextView;

    CartItemAdapter mCartItemAdapter;

    private float totalPrice = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void init() {
        mToolbar.setTitle("我的购物车");
        mToolbar.setMenuTitle(getString(R.string.edit_text));
        mToolbar.showNavigationIcon();
        mToolbar.setBackgroundResource(R.mipmap.base_actionbar);

        List<CourseInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setTitle("小学三年级英语第一单元视频课程同步辅导");
            courseInfo.setPv_num("12");
            courseInfo.setImg("");
            list.add(courseInfo);
        }
        mCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        mCartItemAdapter = new CartItemAdapter(list);
        mCartRecyclerView.setAdapter(mCartItemAdapter);
        initListener();

        mCartItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ck_cart_item) {
                    CheckBox checkBox = (CheckBox) view;
                    mCartItemAdapter.getData().get(position).setChecked(checkBox.isChecked());
                    if (checkBox.isChecked()) {
                        totalPrice = totalPrice + 10;//此处暂无加个字段，待定
                    } else {
                        totalPrice = totalPrice - 10;
                    }
                    mTotalPriceTextView.setText((totalPrice < 0 ? 0 : totalPrice) + "");
                }
                return false;
            }
        });

        RxView.clicks(mPayNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                int selectCount = 0;
                for (int i = 0; i < mCartItemAdapter.getData().size(); i++) {
                    if (mCartItemAdapter.getData().get(i).isChecked()) {
                        LogUtils.e("item good--->" + (i + 1));
                        selectCount++;
                    }
                }

                if (selectCount == 0) {
                    ToastUtils.showLong("请选择购买的商品");
                    return;
                }
                Intent intent = new Intent(ShoppingCartActivity.this,ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (mPayLayout.getVisibility() == View.VISIBLE) {
                    mPayLayout.setVisibility(View.GONE);
                    mDeleteLayout.setVisibility(View.VISIBLE);
                    mToolbar.setMenuTitle(getString(R.string.done_text));
                } else {
                    mPayLayout.setVisibility(View.VISIBLE);
                    mDeleteLayout.setVisibility(View.GONE);
                    mToolbar.setMenuTitle(getString(R.string.edit_text));
                }
            }
        });
    }
}
