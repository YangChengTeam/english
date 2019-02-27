package com.yc.english.vip.views.fragments;


import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.pay.PayConfig;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.setting.model.bean.GoodInfo;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.vip.model.bean.GoodsType;
import com.yc.english.vip.utils.VipInfoHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.SizeUtils;

/**
 * Created by wanglin  on 2017/11/27 14:46.
 */

public class BaseVipPayFragment extends BaseFragment {


    @BindView(R.id.baseItemView_ceping)
    BasePayItemView baseItemViewCeping;
    @BindView(R.id.tv_vip_three_month)
    TextView tvVipThreeMonth;
    @BindView(R.id.tv_vip_six_month)
    TextView tvVipSixMonth;
    @BindView(R.id.tv_vip_tween_month)
    TextView tvVipTweenMonth;
    @BindView(R.id.tv_vip_forever)
    TextView tvVipForever;
    @BindView(R.id.iv_vip_price)
    ImageView ivVipPrice;
    @BindView(R.id.vip_current_price)
    TextView vipCurrentPrice;
    @BindView(R.id.tv_vip_original_price)
    TextView tvVipOriginalPrice;
    @BindView(R.id.ll_vip_ali)
    LinearLayout llVipAli;
    @BindView(R.id.ll_vip_wx)
    LinearLayout llVipWx;
    @BindView(R.id.ll_first_content)
    LinearLayout llFirstContent;
    @BindView(R.id.baseItemView_union)
    BasePayItemView baseItemViewUnion;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.ll_right_container)
    LinearLayout llRightContainer;
    @BindView(R.id.ll_month_container)
    LinearLayout llMonthContainer;
    @BindView(R.id.baseItemView_weike)
    BasePayItemView baseItemViewWeike;
    @BindView(R.id.baseItemView_teach)
    BasePayItemView baseItemViewTeach;
    @BindView(R.id.baseItemView_plan)
    BasePayItemView baseItemViewPlan;
    @BindView(R.id.baseItemView_task_tutorship)
    BasePayItemView baseItemViewTaskTutorship;
    @BindView(R.id.baseItemView_video_fast)
    BasePayItemView baseItemViewVideoFast;
    @BindView(R.id.vip_price_unit)
    TextView vipPriceUnit;


    private int mType;//1.提分辅导2.VIP会员3.单次购买

    private onVipClickListener mListener;

    private List<PayWayInfo> payWayInfoList;
    private int position = 0;

    private String paywayName = PayConfig.ali_pay;

    private GoodInfo goodInfo;
    private List<View> viewList = new ArrayList<>();

//    private List<GoodInfo> sVipList;//提分辅导

    private List<GoodInfo> generalVipList;//普通会员
    //    private List<GoodInfo> dianduList;//点读
//    private List<GoodInfo> weikeList;//微课
    private CourseInfo courseInfo;

    @Override
    public void init() {
        baseItemViewCeping.setVisibility(mType == GoodsType.TYPE_GENERAL_VIP ? View.GONE : View.VISIBLE);
        baseItemViewPlan.setVisibility(mType == GoodsType.TYPE_SVIP ? View.VISIBLE : View.GONE);
        baseItemViewTaskTutorship.setVisibility(mType == GoodsType.TYPE_SVIP ? View.VISIBLE : View.GONE);
        GoodInfoWrapper goodInfoWrapper = VipInfoHelper.getGoodInfoWrapper();

        if (goodInfoWrapper != null)
//        sVipList = goodInfoWrapper.getSvip();
            generalVipList = goodInfoWrapper.getVip();
//        dianduList = goodInfoWrapper.getDiandu();
//        Collections.sort(dianduList, new Comparator<GoodInfo>() {
//            @Override
//            public int compare(GoodInfo o1, GoodInfo o2) {
//                return Integer.parseInt(o1.getUse_time_limit()) - Integer.parseInt(o2.getUse_time_limit());
//            }
//        });
//
//        weikeList = goodInfoWrapper.getWvip();

        if (mType == GoodsType.TYPE_SVIP) {
            tvVipThreeMonth.setText("1个月");
            tvVipSixMonth.setText("3个月");
            tvVipTweenMonth.setText("6个月");
            tvVipForever.setVisibility(View.INVISIBLE);
//            setGoodInfo(position, sVipList);


        } else if (mType == GoodsType.TYPE_GENERAL_VIP) {
            tvVipThreeMonth.setVisibility(View.GONE);
            tvVipSixMonth.setVisibility(View.GONE);
            tvVipTweenMonth.setVisibility(View.GONE);
            tvVipForever.setText("永久会员");
            setTextStyle(tvVipForever);
            baseItemViewWeike.setContentAndIcon("微课免费看", 0);
            baseItemViewTeach.setContentAndIcon("智能测评", R.mipmap.vip_ceping);
            baseItemViewVideoFast.setContentAndIcon("个人学习计划", R.mipmap.vip_plan);
            setGoodInfo(position, generalVipList);
        } else {
            llFirstContent.setVisibility(View.GONE);
            baseItemViewUnion.setVisibility(View.GONE);
            tvVipTweenMonth.setText("永久会员");
            if (mType == GoodsType.TYPE_SINGLE_WEIKE) {
                baseItemViewCeping.setContentAndIcon("同步微课", 0);
                tvVipForever.setText("单次微课");
//                setGoodInfo(position, weikeList);
            } else {

                if (mType == GoodsType.TYPE_SINGLE_DIANDU) {
                    tvVipThreeMonth.setText("1个月");
                    tvVipSixMonth.setText("3个月");
                    tvVipTweenMonth.setText("6个月");
                    tvVipForever.setText("永久会员");
                    baseItemViewCeping.setContentAndIcon("教材点读", 0);
//                    setGoodInfo(position, dianduList);
                } else if (mType == GoodsType.TYPE_SINGLE_INDIVIDUALITY_PLAN) {
                    tvVipForever.setVisibility(View.GONE);
                    baseItemViewCeping.setContentAndIcon("个性学习计划", 0);
                }
            }
            LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) llRightContainer.getLayoutParams();
            layoutParams.topMargin = SizeUtils.dp2px(15);

            llRightContainer.setLayoutParams(layoutParams);
            rootView.setGravity(Gravity.TOP);

//            if (getCourserInfo() != null) {
//                vipCurrentPrice.setText(String.valueOf(getCourserInfo().getMPrice()));
//                tvVipOriginalPrice.setText(String.format(getString(R.string.original_price), String.valueOf(getCourserInfo().getPrice())));
//            }

        }

//        setTextStyle(tvVipThreeMonth);

        tvVipOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        viewList.add(llVipWx);
        viewList.add(llVipAli);
        setPayWayInfo(0);
        initListener();

    }

    private void initListener() {

        click(tvVipThreeMonth, 0);
        click(tvVipSixMonth, 1);
        click(tvVipTweenMonth, 2);
        click(tvVipForever, 3);
        click(llVipWx, 0);
        click(llVipAli, 1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_vip_right_new;
    }


    private void restoreTextViewBg() {

        tvVipThreeMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipSixMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipTweenMonth.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipForever.setBackgroundResource(R.drawable.vip_item_unselect_time);
        tvVipThreeMonth.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
        tvVipSixMonth.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
        tvVipTweenMonth.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
        tvVipForever.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_333));
    }

    public void setType(int type) {
        this.mType = type;
    }

    private void setTextStyle(TextView tv) {
        restoreTextViewBg();
        tv.setBackgroundResource(R.drawable.vip_item_select_time);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.group_blue_21b5f8));
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }


    public interface onVipClickListener {
        void onVipClick(GoodInfo goodInfo, String payWayName, int type);
    }

    public void setOnVipClickListener(onVipClickListener mListener) {
        this.mListener = mListener;
    }


    private void setGoodInfo(int position, List<GoodInfo> goodInfoList) {
        if (mType == GoodsType.TYPE_SINGLE_WEIKE) {
            if (position == 3) {
                if (courseInfo != null) {
                    vipCurrentPrice.setText(String.valueOf(courseInfo.getMPrice()));
                    tvVipOriginalPrice.setText(String.format(getString(R.string.original_price), String.valueOf(courseInfo.getPrice())));
                    goodInfo = new GoodInfo();
                    goodInfo.setId(courseInfo.getGoodId());
                    goodInfo.setM_price(String.valueOf(courseInfo.getMPrice()));
                    goodInfo.setPay_price(String.valueOf(courseInfo.getPayPrice()));
                    goodInfo.setType_id(courseInfo.getType_id());
                    goodInfo.setName(courseInfo.getTitle());

                }
            } else {
                setGoodVipInfo(position, goodInfoList);
            }
            return;
        }
        setGoodVipInfo(position, goodInfoList);
    }

    private void setGoodVipInfo(int position, List<GoodInfo> goodInfoList) {
        if (goodInfoList != null && position < goodInfoList.size()) {

            goodInfo = goodInfoList.get(position);
            String payPrice = goodInfo.getPay_price();
            int realPrice = (int) (Float.parseFloat(payPrice));
            vipCurrentPrice.setText(payPrice);
            tvVipOriginalPrice.setText(String.format(getString(R.string.original_price), goodInfo.getPrice()));
        }
    }

    private void setPayWayInfo(int position) {
        for (View view : viewList) {
            view.setBackgroundResource(R.drawable.vip_item_unselect_time);
        }

        payWayInfoList = new ArrayList<>();
        payWayInfoList.add(new PayWayInfo(PayConfig.wx_pay));
        payWayInfoList.add(new PayWayInfo(PayConfig.ali_pay));

        viewList.get(position).setBackgroundResource(R.drawable.vip_item_select_time);
        paywayName = payWayInfoList.get(position).getPay_way_name();
    }


    private void click(final View view, final int position) {
        RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (view instanceof TextView) {
                    setTextStyle(((TextView) view));
                    if (mType == GoodsType.TYPE_SVIP) {
//                        setGoodInfo(position, sVipList);
                    } else if (mType == GoodsType.TYPE_GENERAL_VIP) {
                        setGoodInfo(position, generalVipList);
                    } else if (mType == GoodsType.TYPE_SINGLE_WEIKE) {
//                        setGoodInfo(position, weikeList);
                    } else if (mType == GoodsType.TYPE_SINGLE_DIANDU) {
//                        setGoodInfo(position, dianduList);
                    }

                }
                if (view instanceof LinearLayout) {
                    setPayWayInfo(position);
                }
                if (mListener != null) {
                    mListener.onVipClick(goodInfo, paywayName, mType);
                }
            }
        });
    }


}
