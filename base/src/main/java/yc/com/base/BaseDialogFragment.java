package yc.com.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hwangjr.rxbus.RxBus;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import yc.com.blankj.utilcode.util.ScreenUtils;
import yc.com.blankj.utilcode.util.SizeUtils;

/**
 * Created by wanglin  on 2018/3/6 11:14.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements IView {

    protected P mPresenter;
    protected View rootView;
    protected BaseLoadingView loadingView;
    protected BaseActivity mContext;
    protected Window window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RxBus.get().register(this);
        window = getDialog().getWindow();


        if (rootView == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootView = inflater.inflate(getLayoutId(), container, false);

            loadingView = new BaseLoadingView(getActivity());
//            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(getAnimationId());
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        try {
            ButterKnife.bind(this, rootView);
        } catch (Exception e) {
            LogUtil.msg("--> " + "初始化失败");
        }
        initView();

        init();


        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (BaseActivity) context;
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


    protected void initView() {
    }

    protected float getWidth() {
        return 1.0f;
    }


    protected int getAnimationId() {
        return R.style.vip_style;
    }

    public int getHeight() {
        return ScreenUtils.getScreenHeight() * 3 / 5 + SizeUtils.dp2px(50);
    }

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EmptyUtils.isNotEmpty(mPresenter))
            mPresenter.unsubscribe();
        RxBus.get().unregister(this);
        Runtime.getRuntime().gc();
    }
}
