package com.yc.english.main.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideCircleTransformation;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.group.view.activitys.GroupListJoinActivity;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.BannerImageLoader;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.presenter.IndexPresenter;
import com.yc.english.main.view.activitys.MainActivity;
import com.yc.english.main.view.wdigets.IndexMenuView;
import com.yc.english.read.view.activitys.BookActivity;
import com.yc.english.base.view.StateView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View {
    @BindView(R.id.sv_content)
    ScrollView mContextScrollView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.tv_student_number)
    TextView mStudentNumberTextView;

    @BindView(R.id.tv_teacher_number)
    TextView mTeacherNumberTextView;

    @BindView(R.id.iv_avatar)
    ImageView mAvatarImageView;

    @BindView(R.id.ll_share)
    LinearLayout mShareLinearLayout;

    @BindView(R.id.im_read)
    IndexMenuView mReadMenuView;

    @BindView(R.id.im_task)
    IndexMenuView mTaskMenuView;

    @BindView(R.id.im_word)
    IndexMenuView mWordMenuView;

    @BindView(R.id.im_game)
    IndexMenuView mGameMenuView;

    @BindView(R.id.banner)
    Banner mBanner;

    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);

        mStudentNumberTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        mTeacherNumberTextView.setTypeface(null, Typeface.BOLD_ITALIC);

        RxView.clicks(mReadMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra("tag", "read");
                intent.putExtra("view_type",1);
                startActivity(intent);
            }
        });

        RxView.clicks(mWordMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra("tag", "word");
                intent.putExtra("view_type",2);
                startActivity(intent);
            }
        });

        RxView.clicks(mGameMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), GroupListJoinActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mTaskMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.goToTask();
            }
        });

        RxView.clicks(mAvatarImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.goToMy();
            }
        });

        RxView.clicks(mShareLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity());
                sharePopupWindow.show(mRootView);
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ToastUtils.showLong("点击了第" + position + "图片");
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_fragment_index;
    }

    @Override
    public void showLoading() {
        mLoadingStateView.showNoNet(mContextScrollView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipsHelper.tips(getContext(), "网络不给力");
            }
        });
    }

    @Override
    public void hideLoading() {
        mLoadingStateView.hide();
    }

    @Override
    public void showBanner(List<String> images) {
        mBanner.isAutoPlay(true)
                .setDelayTime(1500)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }


    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.USER_INFO)
            }
    )
    @Override
    public void showAvatar(UserInfo userInfo) {
        RequestOptions options = new RequestOptions();
        options.centerCrop().placeholder(R.mipmap.default_avatar).transform(new GlideCircleTransformation(getActivity(), 1,
                Color.parseColor("#dbdbe0")));
        Glide.with(this).load(userInfo.getAvatar()).apply(options).into(mAvatarImageView);
    }
}
