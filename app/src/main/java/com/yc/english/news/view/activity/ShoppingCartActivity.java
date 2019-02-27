package com.yc.english.news.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.ShoppingHelper;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.adapter.CartItemAdapter;
import com.yc.english.news.utils.OrderConstant;
import com.yc.english.news.view.widget.SpaceItemDecoration;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.ToastUtils;

/**
 * Created by admin on 2017/11/2.
 */

public class ShoppingCartActivity extends FullScreenActivity {

    @BindView(R.id.rv_cart_list)
    RecyclerView mCartRecyclerView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.layout_root)
    RelativeLayout mRootLayout;

    @BindView(R.id.layout_pay)
    RelativeLayout mPayLayout;

    @BindView(R.id.layout_delete)
    RelativeLayout mDeleteLayout;

    @BindView(R.id.layout_pay_now)
    LinearLayout mPayNowLayout;

    @BindView(R.id.layout_delete_all)
    LinearLayout mDeleteAllLayout;

    @BindView(R.id.tv_total_price)
    TextView mTotalPriceTextView;

    @BindView(R.id.ck_all)
    CheckBox mAllCheckBox;

    @BindView(R.id.ck_delete_all)
    CheckBox mDeleteAllCheckBox;

    LinearLayoutManager linearLayoutManager;

    CartItemAdapter mCartItemAdapter;

    private float totalPrice = 0;

    private List<CourseInfo> list;

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

        //读取数据
        list = ShoppingHelper.getCourseInfoListFromDB(UserInfoHelper.getUserInfo().getUid());
        if (list == null || list.size() == 0) {
            mLoadingStateView.showNoData(mRootLayout);
            mToolbar.setMenuTitle("");
        }

        linearLayoutManager = new LinearLayoutManager(this);
        mCartRecyclerView.setLayoutManager(linearLayoutManager);
        mCartRecyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_10)));
        mCartItemAdapter = new CartItemAdapter(list);
        setCartItemState(false);
        mAllCheckBox.setChecked(false);

        mCartRecyclerView.setAdapter(mCartItemAdapter);
        initListener();

        mCartItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.ck_cart_item) {
                    CheckBox checkBox = (CheckBox) view;
                    mCartItemAdapter.getData().get(position).setIsChecked(checkBox.isChecked());
                    if (checkBox.isChecked()) {
                        totalPrice = totalPrice + mCartItemAdapter.getData().get(position).getPayPrice();
                    } else {
                        totalPrice = totalPrice - mCartItemAdapter.getData().get(position).getPayPrice();

                        if (!checkItemSelected()) {
                            mAllCheckBox.setChecked(false);
                        }
                    }
                    mTotalPriceTextView.setText((totalPrice < 0 ? 0 : totalPrice) + "");
                }
                return false;
            }
        });

        RxView.clicks(mAllCheckBox).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mAllCheckBox.isChecked()) {
                    setCartItemState(true);
                } else {
                    setCartItemState(false);
                }
                mCartItemAdapter.notifyDataSetChanged();
            }
        });

        RxView.clicks(mDeleteAllCheckBox).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mDeleteAllCheckBox.isChecked()) {
                    setCartItemState(true);
                } else {
                    setCartItemState(false);
                }
                mCartItemAdapter.notifyDataSetChanged();
            }
        });

        RxView.clicks(mPayNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                int selectCount = 0;
                ArrayList<CourseInfo> goodsList = new ArrayList<>();
                for (int i = 0; i < mCartItemAdapter.getData().size(); i++) {
                    if (mCartItemAdapter.getData().get(i).getIsChecked()) {
                        LogUtils.e("item good--->" + (i + 1));
                        selectCount++;
                        goodsList.add(mCartItemAdapter.getData().get(i));
                    }
                }

                if (selectCount == 0) {
                    ToastUtils.showLong("请选择购买的商品");
                    return;
                }
                Intent intent = new Intent(ShoppingCartActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("total_price", totalPrice);
                intent.putParcelableArrayListExtra("goods_list", goodsList);
                startActivity(intent);
            }
        });


        RxView.clicks(mDeleteAllLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                for (int i = 0; i < mCartItemAdapter.getData().size(); i++) {
                    if (mCartItemAdapter.getData().get(i).getIsChecked()) {
                        ShoppingHelper.deleteCourseInfoByUser(UserInfoHelper.getUserInfo().getUid(), mCartItemAdapter.getData().get(i).getId());
                    }
                }
                ToastUtils.showLong("删除成功");
                list = ShoppingHelper.getCourseInfoListFromDB(UserInfoHelper.getUserInfo().getUid());
                if (list == null || list.size() == 0) {
                    mLoadingStateView.showNoData(mRootLayout);
                    mToolbar.setMenuTitle("");
                } else {
                    mCartItemAdapter.setNewData(list);
                }
            }
        });
    }

    /**
     * 检测购物车中是否有选中的项
     */
    public boolean checkItemSelected() {
        boolean flag = false;
        for (int i = 0; i < mCartItemAdapter.getData().size(); i++) {
            View view = linearLayoutManager.findViewByPosition(i);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.ck_cart_item);
            if (checkBox.isChecked()) {
                flag = true;
            }
        }
        return flag;
    }

    public void setCartItemState(boolean flag) {
        float cartPrice = 0;
        for (int i = 0; i < mCartItemAdapter.getData().size(); i++) {
            mCartItemAdapter.getData().get(i).setIsChecked(flag);
            if (flag) {
                cartPrice += mCartItemAdapter.getData().get(i).getPayPrice();
            } else {
                cartPrice = 0;
            }
        }

        totalPrice = cartPrice;
        mTotalPriceTextView.setText((totalPrice < 0 ? 0 : totalPrice) + "");
    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (mPayLayout.getVisibility() == View.VISIBLE) {
                    mPayLayout.setVisibility(View.GONE);
                    mDeleteLayout.setVisibility(View.VISIBLE);
                    mToolbar.setMenuTitle(getString(R.string.done_text));

                    setCartItemState(false);
                    mCartItemAdapter.notifyDataSetChanged();
                } else {
                    mPayLayout.setVisibility(View.VISIBLE);
                    mDeleteLayout.setVisibility(View.GONE);
                    mToolbar.setMenuTitle(getString(R.string.edit_text));
                }
            }
        });
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(OrderConstant.PAY_SUCCESS)
            }
    )
    public void paySuccess(String payOrderSn) {
        finish();
    }
}
