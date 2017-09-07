package com.yc.english.community.view.activitys;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.fragments.CommunityFragment;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityActivity extends FullScreenActivity<CommunityInfoPresenter> {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.note_indicator)
    FixedIndicatorView mFixedIndicatorView;

    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;

    @BindView(R.id.ground_view)
    View groundView;

    @BindView(R.id.tv_english_circle)
    TextView mEnglishCircleTextView;

    @BindView(R.id.tv_friends_circle)
    TextView mFriendsCircleTextView;

    private FragmentAdapter mFragmentAdapter;

    private int[] location;

    private float maxSize = 0;

    //子菜单打开，关闭时的动画
    private Animation qq_friend_in, take_photo_in, qq_friend_out, take_photo_out;

    float lastSize = 0;

    public boolean isShow = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_community;
    }

    private int currentPosition;

    @Override
    public void init() {

        location = new int[]{1000, 0};
        maxSize = 2.5f * 15;

        qq_friend_in = AnimationUtils.loadAnimation(this, R.anim.english_in);
        take_photo_in = AnimationUtils.loadAnimation(this, R.anim.friends_in);
        qq_friend_out = AnimationUtils.loadAnimation(this, R.anim.english_out);
        take_photo_out = AnimationUtils.loadAnimation(this, R.anim.friends_out);

        mToolbar.setTitle(getString(R.string.community_name));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuIcon(R.mipmap.add_note_icon);
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (menuLayout.getVisibility() == View.INVISIBLE) {
                    menuLayout.setVisibility(View.VISIBLE);
                    menuLayout.setFocusable(true);
                    menuLayout.setClickable(true);

                    animationOpen.setDuration(600);
                    animationOpen.setInterpolator(new DecelerateInterpolator());
                    animationOpen.setFillAfter(true);
                    groundView.startAnimation(animationOpen);
                    mFriendsCircleTextView.startAnimation(qq_friend_in);
                    mEnglishCircleTextView.startAnimation(take_photo_in);

                    isShow = true;
                    //showAddIv.setClickable(false);
                } else {
                    closeMenu();
                }
            }
        });

        mFixedIndicatorView.setAdapter(new MyAdapter(3));

        mFixedIndicatorView.setScrollBar(new ColorBar(this, ContextCompat.getColor(this, R.color
                .primary), 6));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(this, R.color.primary);
        int unSelectColor = ContextCompat.getColor(this, R.color.black_333);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        mFixedIndicatorView.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
        mFixedIndicatorView.setCurrentItem(0, true);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
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

        RxView.clicks(menuLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                closeMenu();
            }
        });

        //英粉圈type:1
        RxView.clicks(mEnglishCircleTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                toAddNoteActivity("1");
            }
        });

        //学习圈type:2
        RxView.clicks(mFriendsCircleTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                toAddNoteActivity("2");
            }
        });

    }

    public void toAddNoteActivity(String type) {
        if (UserInfoHelper.getUserInfo() != null) {
            Intent intent = new Intent(this, CommunityAddActivity.class);
            intent.putExtra("note_type", type);
            startActivity(intent);
            closeMenu();
        } else {
            closeMenu();
            UserInfoHelper.isGotoLogin(this);
        }
    }

    private CommunityFragment courseFragment1;
    private CommunityFragment courseFragment2;
    private CommunityFragment courseFragment3;

    class FragmentAdapter extends FragmentStatePagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            currentPosition = position;
            Bundle bundle = new Bundle();
            if (position == 0) {
                if (courseFragment1 == null) {
                    bundle.putInt("type", 3);
                    courseFragment1 = new CommunityFragment();
                    courseFragment1.setArguments(bundle);
                }
                return courseFragment1;
            } else if (position == 1) {
                if (courseFragment2 == null) {
                    bundle.putInt("type", 1);
                    courseFragment2 = new CommunityFragment();
                    courseFragment2.setArguments(bundle);
                }
                return courseFragment2;
            } else if (position == 2) {
                if (courseFragment3 == null) {
                    bundle.putInt("type", 2);
                    courseFragment3 = new CommunityFragment();
                    courseFragment3.setArguments(bundle);
                }
                return courseFragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {
        private final String[] titles = new String[]{"热门", "英粉圈", "学习圈"};

        private final int count;

        public MyAdapter(int count) {
            super();
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.weixin_tab, parent, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(titles[position]);
            return convertView;
        }
    }


    private Animation animationOpen = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float size = (maxSize - 1) * interpolatedTime + 1;
            lastSize = size;
            Matrix matrix = t.getMatrix();

            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {

            }
        }
    };

    private Animation animationClose = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            float size = lastSize - (maxSize - 1) * interpolatedTime + 1;
            Matrix matrix = t.getMatrix();
            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {
                menuLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mEnglishCircleTextView.startAnimation(qq_friend_out);
        mFriendsCircleTextView.startAnimation(take_photo_out);

        animationClose.setDuration(500);
        animationClose.setInterpolator(new DecelerateInterpolator());
        animationClose.setFillAfter(true);
        groundView.startAnimation(animationClose);
        isShow = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
