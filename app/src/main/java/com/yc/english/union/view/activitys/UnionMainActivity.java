package com.yc.english.union.view.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.view.activitys.teacher.GroupVerifyActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.contract.UnionListContract;
import com.yc.english.union.view.activitys.student.UnionJoinActivity;
import com.yc.english.union.view.activitys.teacher.UnionCreateActivity;
import com.yc.english.union.view.fragment.UnionFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/24 17:59.
 */

public class UnionMainActivity extends FullScreenActivity<UnionListPresenter> implements UnionListContract.View {
    private static final String TAG = "UnionMainActivity";
    @BindView(R.id.sView_loading)
    StateView sViewLoading;
    @BindView(R.id.btn_create_class)
    Button btnCreateClass;
    @BindView(R.id.btn_join_class)
    Button btnJoinClass;
    @BindView(R.id.ll_empty_container)
    LinearLayout llEmptyContainer;
    @BindView(R.id.fiv_indicator)
    FixedIndicatorView fivIndicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_create_class1)
    Button btnCreateClass1;
    @BindView(R.id.btn_join_class1)
    Button btnJoinClass1;
    @BindView(R.id.ll_data_container)
    LinearLayout llDataContainer;
    @BindView(R.id.content_view)
    FrameLayout contentView;
    @BindView(R.id.rootView)
    LinearLayout rootView;


    private GuidePopupWindow guidePopupWindow;
    private int page = 1;

    private final String[] titles = new String[]{"所有的", "我创建的", "我参与的"};


    @Override
    public void init() {

        mPresenter = new UnionListPresenter(this, this);
        mToolbar.setTitle(getString(R.string.english_union));

        mToolbar.showNavigationIcon();
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                startActivity(new Intent(UnionMainActivity.this, GroupVerifyActivity.class));
            }
        });


        initViewPager();
    }

<<<<<<< HEAD
=======


>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99

    private void showCreateGuide() {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        guidePopupWindow = builder.setDelay(1f).setTargetView(btnCreateClass).setCorner(5).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {
                goToActivity(UnionCreateActivity.class);
            }
        }).build(this);
        guidePopupWindow.addCustomView(R.layout.guide_create_union_view, R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guidePopupWindow.dismiss();
            }
        });

        guidePopupWindow.setDebug(true);
        guidePopupWindow.show(rootView, "create_union");

    }


    @Override
    public int getLayoutId() {
        return R.layout.group_union_list_main;
    }

    @OnClick({R.id.btn_create_class, R.id.btn_create_class1, R.id.btn_join_class, R.id.btn_join_class1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_class:
            case R.id.btn_create_class1:
                goToActivity(UnionCreateActivity.class);

                break;
            case R.id.btn_join_class:
            case R.id.btn_join_class1:
                goToActivity(UnionJoinActivity.class);
                break;
        }

    }

    private void goToActivity(Class activity) {
        if (!UserInfoHelper.isGotoLogin(this)) {
            startActivity(new Intent(this, activity));
        }

    }

<<<<<<< HEAD
    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.GROUP_LIST)
            }
    )
    public void getList(String group) {
        mPresenter.loadData(true);
    }
=======
>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99


    @Override
    public void showMemberList(List<StudentInfo> count) {

        if (count != null && count.size() > 0) {
            mToolbar.setMenuIcon(R.mipmap.group65);
        } else {
            mToolbar.setMenuIcon(R.mipmap.group66);
        }
        invalidateOptionsMenu();
    }


<<<<<<< HEAD
=======

>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99
    @Override
    public void showUnionList1(List<ClassInfo> classInfos) {
        if (classInfos != null && classInfos.size() > 0) {

            if (guidePopupWindow != null && guidePopupWindow.isShowing()) {
                guidePopupWindow.dismiss();
            }
            llDataContainer.setVisibility(View.VISIBLE);
            llEmptyContainer.setVisibility(View.GONE);
            initViewPager();
        } else {
            llDataContainer.setVisibility(View.GONE);
            llEmptyContainer.setVisibility(View.VISIBLE);
            if (ActivityUtils.isValidContext(this)) {
                showCreateGuide();
            }
        }
    }

    private void initViewPager() {
        fivIndicator.setAdapter(new MyAdapter());
        fivIndicator.setScrollBar(new ColorBar(this, ContextCompat.getColor(this, R.color
                .primary), 6));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(this, R.color.primary);
        int unSelectColor = ContextCompat.getColor(this, R.color.black_333);
        fivIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        fivIndicator.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                viewpager.setCurrentItem(position);
                return false;
            }
        });
        fivIndicator.setCurrentItem(0, true);

        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(3);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
<<<<<<< HEAD

            @Override
            public void onPageSelected(int i) {
                fivIndicator.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private UnionFragment unionCreateFragment;
    private UnionFragment unionJoinFragment;
    private UnionFragment unionAllFragment;


    class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);

        }
=======

            @Override
            public void onPageSelected(int i) {
                fivIndicator.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (unionAllFragment == null) {
                    unionAllFragment = new UnionFragment();
                    unionAllFragment.setType(-1);
                }
                return unionAllFragment;
            } else if (position == 1) {
                if (unionJoinFragment == null) {

                    unionJoinFragment = new UnionFragment();
                    unionJoinFragment.setType(2);
                }
                return unionJoinFragment;
            } else if (position == 2) {

<<<<<<< HEAD

                if (unionCreateFragment == null) {
                    unionCreateFragment = new UnionFragment();
                    unionCreateFragment.setType(0);
                }
                return unionCreateFragment;
=======
    @Override
    public void showNoNet() {
        sViewLoading.showNoNet(contentView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true);

>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {


<<<<<<< HEAD
        public MyAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.weixin_tab, parent, false);
            }
=======
    private UnionFragment unionCreateFragment;
    private UnionFragment unionJoinFragment;
    private UnionFragment unionAllFragment;


    class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (unionAllFragment == null) {
                    unionAllFragment = new UnionFragment();
                    unionAllFragment.setType(-1);
                }
                return unionAllFragment;
            } else if (position == 1) {
                if (unionJoinFragment == null) {

                    unionJoinFragment = new UnionFragment();
                    unionJoinFragment.setType(2);
                }
                return unionJoinFragment;
            } else if (position == 2) {


                if (unionCreateFragment == null) {
                    unionCreateFragment = new UnionFragment();
                    unionCreateFragment.setType(0);
                }
                return unionCreateFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {


        public MyAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.weixin_tab, parent, false);
            }
>>>>>>> f2baf4ead17799bf25026dbd15a7bb9a2a5d4d99
            TextView textView = (TextView) convertView;
            textView.setText(titles[position]);
            return convertView;
        }
    }

}
