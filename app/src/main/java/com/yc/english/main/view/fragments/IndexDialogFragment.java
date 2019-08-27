package com.yc.english.main.view.fragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.kk.utils.ToastUtil;
import com.yc.english.R;
import com.yc.soundmark.base.constant.SpConstant;

import java.util.concurrent.TimeUnit;

import yc.com.blankj.utilcode.util.SPUtils;


/**
 * Created by wanglin  on 2019/4/12 14:40.
 */
public class IndexDialogFragment extends DialogFragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Window window = getDialog().getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(R.layout.fragment_index_dialog, container, false);
//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        initView();


        return rootView;

    }


    public View getView(int resId) {
        return rootView.findViewById(resId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处

            WindowManager.LayoutParams layoutParams = window.getAttributes();

            layoutParams.width = (int) (ScreenUtil.getWidth(getActivity()) * getWidth());
            layoutParams.height = getHeight();
            window.setAttributes(layoutParams);
        }

    }

    private boolean mChecked;

    protected void initView() {


        CheckBox cb = (CheckBox) getView(R.id.cb_privacy);

        final TextView tvEnterApp = (TextView) getView(R.id.tv_enter_app);
        mChecked = cb.isChecked();

        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mChecked = isChecked;
            if (isChecked) {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.primary));
            } else {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray_ddd));
            }
        });


        RxView.clicks(tvEnterApp).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (mChecked) {
                dismiss();
//                SharePreferenceUtils.getInstance().putBoolean(Config.index_dialog, true);
                SPUtils.getInstance().put(SpConstant.INDEX_DIALOG, true);

                if (onShowListener != null) {
                    onShowListener.onShow();
                }

            } else {
                ToastUtil.toast2(getActivity(), "请先同意用户协议");
            }
        });


//        RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());
//
//        RxView.clicks(rootView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
//            String str = PreferenceUtil.getImpl(getActivity()).getString(Config.ADV_INFO, "");
//            final AdvInfo advInfo = JSON.parseObject(str, AdvInfo.class);
//            if (null != advInfo) {
//                Intent intent = new Intent(getActivity(), AdvInfoActivity.class);
//                intent.putExtra("url", advInfo.getUrl());
//                intent.putExtra("title", advInfo.getButton_txt());
//                startActivity(intent);
//            }
//            dismiss();
//        });
    }

    protected float getWidth() {
        return 0.8f;
    }


    protected int getAnimationId() {
        return R.style.share_anim;
    }

    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 3 / 5;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }


    private onShowListener onShowListener;

    public void setOnShowListener(IndexDialogFragment.onShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }

    public interface onShowListener {
        void onShow();
    }
}
