package com.yc.junior.english.weixin.views.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.ShoppingHelper;
import com.yc.junior.english.base.utils.StatusBarCompat;
import com.yc.junior.english.base.view.BaseActivity;
import com.yc.junior.english.base.view.BaseFragment;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.news.contract.OrderContract;
import com.yc.junior.english.news.presenter.OrderPresenter;
import com.yc.junior.english.news.view.activity.ShoppingCartActivity;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends BaseFragment<OrderPresenter> implements OrderContract.View {


    @BindView(R.id.iv_shopping_cart)
    ImageView mShoppingImageView;

    @BindView(R.id.layout_num)
    LinearLayout mNumLayout;

    @BindView(R.id.tv_cart_num)
    TextView mCartNumTextView;

    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;

    @BindView(R.id.toolbarWarpper)
    FrameLayout mToolbarWarpper;

    @Override
    public void init() {
        mPresenter = new OrderPresenter(getActivity(), this);

        RxView.clicks(mShoppingImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (UserInfoHelper.getUserInfo() != null) {
                    Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                    startActivity(intent);
                } else {
                    UserInfoHelper.isGotoLogin(getActivity());
                }
            }
        });
        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolbar, R.mipmap.base_actionbar);
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_type;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoHelper.getUserInfo() != null) {
            List<CourseInfo> list = ShoppingHelper.getCourseInfoListFromDB(UserInfoHelper.getUserInfo().getUid());
            if (list != null) {
                if (list.size() > 10) {
                    mNumLayout.setBackgroundResource(R.mipmap.more_num_icon);
                } else {
                    mNumLayout.setBackgroundResource(R.mipmap.single_num_icon);
                    if (list.size() == 0) {
                        mNumLayout.setVisibility(View.GONE);
                    } else {
                        mNumLayout.setVisibility(View.GONE);
                    }
                }
                mCartNumTextView.setText(list.size() + "");
            }
        } else {
            mCartNumTextView.setText("0");
            mNumLayout.setBackgroundResource(R.mipmap.single_num_icon);
            mNumLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void showLoadingDialog(String msg) {
        ((BaseActivity) getActivity()).showLoadingDialog(msg);
    }

    @Override
    public void dismissLoadingDialog() {
        ((BaseActivity) getActivity()).dismissLoadingDialog();
    }


    @Override
    public void showOrderInfo(OrderInfo orderInfo) {

    }

    @Override
    public void showOrderPayResult(ResultInfo resultInfo) {
        LogUtils.e("订单支付成功--->");
    }

    @Override
    public void isBuy() {

    }
}
