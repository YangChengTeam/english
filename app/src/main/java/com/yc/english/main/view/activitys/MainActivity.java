package com.yc.english.main.view.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.intelligent.view.activitys.IntelligentTypeStartBgActivity;
import com.yc.english.intelligent.view.fragments.IntelligentFragment;
import com.yc.english.intelligent.view.fragments.IntelligentTypeFragment;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.presenter.MainPresenter;
import com.yc.english.main.view.fragments.IndexFragment;
import com.yc.english.main.view.wdigets.TabBar;
import com.yc.english.setting.view.fragments.MyFragment;
import com.yc.english.weixin.views.fragments.CourseTypeFragment;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tabbar)
    TabBar mTabBar;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private FragmentAdapter mFragmentAdapter;
    private int mCurrentIndex;


    public static String BGKEY = "bgkey";

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_main;
    }

    @Override
    public void init() {
        mainActivity = this;

        StatusBarCompat.light(this);


        mPresenter = new MainPresenter(this, this);
        mTabBar.setOnTabSelectedListener(new TabBar.OnTabSelectedListener() {
            @Override
            public void onSelected(int idx) {
                if (idx == 2 && SPUtils.getInstance().getString(BGKEY, "").isEmpty()) {
                    startActivity(new Intent(MainActivity.this, IntelligentTypeStartBgActivity.class));
                    return;
                }
                mViewPager.setCurrentItem(idx, false);
            }
        });
        mTabBar.tab(mCurrentIndex);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mCurrentIndex == position) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (position == 0) {
                        StatusBarCompat.light(MainActivity.this);
                    } else {
                        StatusBarCompat.black(MainActivity.this);
                    }
                }
                mCurrentIndex = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2 && SPUtils.getInstance().getString(BGKEY, "").isEmpty()) {
                    startActivity(new Intent(MainActivity.this, IntelligentTypeStartBgActivity.class));
                    return;
                }
                mTabBar.tab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void goToTask() {
        mTabBar.tab(1);
    }

    public void goToMy() {
        mTabBar.tab(3);
    }

    public void goToIntelligent() {
        mTabBar.tab(2);
    }


    private IndexFragment mIndexFragment;
    private CourseTypeFragment mClassMainFragment;


    private IntelligentTypeFragment mIntelligentFragment;

    private MyFragment mMyFragment;

    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (mIndexFragment == null) {
                    mIndexFragment = new IndexFragment();
                }
                return mIndexFragment;
            } else if (position == 1) {
                if (mClassMainFragment == null) {
                    mClassMainFragment = new CourseTypeFragment();
                }
                return mClassMainFragment;
            } else if (position == 2) {
                if (mIntelligentFragment == null) {
                    mIntelligentFragment = new IntelligentTypeFragment();
                }
                return mIntelligentFragment;
            } else if (position == 3) {
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                }
                return mMyFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setDesc("确认退出说说英语？");
        alertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                System.exit(0);
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("appraisal", false)) {
            goToIntelligent();
        }
        if (intent.getBooleanExtra("weike", false)) {
            goToTask();
        }
    }


}
