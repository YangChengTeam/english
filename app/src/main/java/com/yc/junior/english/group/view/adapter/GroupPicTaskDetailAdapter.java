package com.yc.junior.english.group.view.adapter;


import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.junior.english.R;
import com.yc.junior.english.group.view.widget.Rotate3dAnimation;
import com.yc.junior.english.group.view.widget.ZoomImageView;

import java.util.List;


/**
 * Created by wanglin  on 2017/7/24 17:37.
 * 显示发布图片作业详情的信息
 */

public class GroupPicTaskDetailAdapter extends PagerAdapter {

    private AppCompatActivity mActivity;
    private List<String> mList;

    public GroupPicTaskDetailAdapter(AppCompatActivity activity, List<String> list) {
        this.mActivity = activity;
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(container.getContext(), R.layout.group_task_picture_item, null);

        ZoomImageView imageView = view.findViewById(R.id.iv_picture_detail);
//        RelativeLayout mContainer = (RelativeLayout) view.findViewById(R.id.container);
//        applyRotation(0, 90, mContainer);
        container.addView(view);
        String path = mList.get(position);
        Glide.with(mActivity).setDefaultRequestOptions(RequestOptions.errorOf(R.mipmap.default_avatar)
                .placeholder(R.mipmap.pic_loading)).load(path).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });


        return view;
    }

    /**
     * Setup a new 3D rotation on the container view.
     *
     * @param start the start angle at which the rotation must begin
     * @param end   the end angle of the rotation
     */
    private void applyRotation(float start, float end, View view) {
        // Find the center of the container
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Rotate3dAnimation rotation =
                new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(3000);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(view));

        view.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private View mView;

        public DisplayNextView(View view) {
            mView = view;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mView.post(new SwapViews(mView));
//            animation.cancel();


        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class SwapViews implements Runnable {
        private View mView;

        public SwapViews(View view) {
            mView = view;
        }

        public void run() {
            final float centerX = mView.getWidth() / 2.0f;
            final float centerY = mView.getHeight() / 2.0f;
            Rotate3dAnimation rotation;

            rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            rotation.setDuration(3000);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mView.startAnimation(rotation);
        }
    }
}
