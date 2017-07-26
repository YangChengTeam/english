package com.yc.english.main.view.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.BannerImageLoader;
import com.yc.english.main.presenter.IndexPresenter;
import com.yc.english.main.view.activitys.MainActivity;
import com.yc.english.main.view.wdigets.IndexMenuView;
import com.yc.english.read.view.activitys.BookActivity;
import com.youth.banner.Banner;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View {

    @BindView(R.id.tv_student_number)
    TextView mStudentNumberTextView;

    @BindView(R.id.tv_teacher_number)
    TextView mTeacherNumberTextView;

    @BindView(R.id.iv_avatar)
    ImageView mAvatarImageView;

    @BindView(R.id.iv_share)
    ImageView mShareImageView;

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

        Bitmap bitmap = ((BitmapDrawable)mAvatarImageView.getDrawable()).getBitmap();
        ImageUtils.toRound(ImageUtils.addFrame(bitmap, 1, Color.BLUE));

        RxView.clicks(mReadMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), BookActivity.class));
            }
        });

        RxView.clicks(mWordMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), BookActivity.class));
            }
        });

        RxView.clicks(mGameMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), BookActivity.class));
            }
        });

        RxView.clicks(mTaskMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.goToTask();
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
    public int getLayoutID() {
        return R.layout.main_fragment_index;
    }

    @Override
    public void showBanner(List<String> images) {
        mBanner.isAutoPlay(true)
                .setDelayTime(1500)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }
}
