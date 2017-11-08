package com.yc.english.weixin.views.fragments;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.helper.ShoppingHelper;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.news.view.activity.ShoppingCartActivity;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.views.utils.TabsUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fiv_indicator)
    FixedIndicatorView mFixedIndicatorView;

    @BindView(R.id.iv_shopping_cart)
    ImageView mShoppingImageView;

    @BindView(R.id.layout_num)
    LinearLayout mNumLayout;

    @BindView(R.id.tv_cart_num)
    TextView mCartNumTextView;

    private final String[] titles = new String[]{"音频微课", "视频微课"};

    @Override
    public void init() {

        mFixedIndicatorView.setAdapter(new TabsUtils.MyAdapter(getActivity(), titles));
        mFixedIndicatorView.setScrollBar(new ColorBar(getActivity(), ContextCompat.getColor(getActivity(), R.color
                .primary), 6));
        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(getActivity(), R.color.primary);
        int unSelectColor = ContextCompat.getColor(getActivity(), R.color.black_333);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        mFixedIndicatorView.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
        mFixedIndicatorView.setCurrentItem(0, true);

        TabsUtils.MyFragmentAdapter mFragmentAdapter = new TabsUtils.MyFragmentAdapter(getChildFragmentManager(),
                new String[]{"7", "8"});
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFixedIndicatorView.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        RxView.clicks(mShoppingImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_type;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<CourseInfo> list = ShoppingHelper.getCourseInfoListFromDB();
        if (list != null) {
            if (list.size() > 10) {
                mNumLayout.setBackgroundResource(R.mipmap.more_num_icon);
            } else {
                mNumLayout.setBackgroundResource(R.mipmap.single_num_icon);
            }
            mCartNumTextView.setText(list.size() + "");
        }
    }
}
