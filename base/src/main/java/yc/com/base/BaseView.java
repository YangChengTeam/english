package yc.com.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;


import com.kk.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/2/27 11:45.
 */

public abstract class BaseView extends FrameLayout implements IView {

    protected Context mContext;

    public BaseView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(getLayoutId(), this);

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            LogUtil.msg("初始化失败-->" + e.getMessage());
        }
        init();

    }

    public BaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutInflater.from(context).inflate(getLayoutId(), this);

        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            LogUtil.msg("初始化失败-->" + e.getMessage());
        }
        init();
    }


    @Override
    public void init() {

    }
}
