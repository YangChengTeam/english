package com.yc.english.base.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.EnglishApp;
import com.yc.english.R;
import com.yc.english.base.utils.DrawableUtils;
import com.yc.english.main.model.domain.Constant;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/9/7.
 */

public class SelectGradePopupWindow extends BasePopupWindow {

    @BindView(R.id.tv_1)
    TextView m1TextView;

    @BindView(R.id.tv_2)
    TextView m2TextView;

    @BindView(R.id.tv_3)
    TextView m3TextView;

    @BindView(R.id.tv_4)
    TextView m4TextView;

    @BindView(R.id.tv_5)
    TextView m5TextView;

    @BindView(R.id.tv_6)
    TextView m6TextView;

    @BindView(R.id.tv_7)
    TextView m7TextView;

    @BindView(R.id.tv_8)
    TextView m8TextView;

    @BindView(R.id.tv_9)
    TextView m9TextView;

    @BindView(R.id.tv_0)
    TextView m0TextView;

    @BindView(R.id.btn_comein)
    Button mComeinButton;

    private int grade = -1;

    public SelectGradePopupWindow(Activity context) {
        super(context);
    }

    @Override
    public void init() {
        clear();

        final int tgrade = SPUtils.getInstance().getInt("grade", 0);
        grade = tgrade;
        switch (tgrade) {
            case 0:
                select(m0TextView);
                break;
            case 1:
                select(m1TextView);
                break;
            case 2:
                select(m2TextView);
                break;
            case 3:
                select(m3TextView);
                break;
            case 4:
                select(m4TextView);
                break;
            case 5:
                select(m5TextView);
                break;
            case 6:
                select(m6TextView);
                break;
            case 7:
                select(m7TextView);
                break;
            case 8:
                select(m8TextView);
                break;
            case 9:
                select(m9TextView);
                break;
        }

        RxView.clicks(m0TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m0TextView);
                grade = 0;
            }
        });

        RxView.clicks(m1TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m1TextView);
                grade = 1;
            }
        });

        RxView.clicks(m2TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m2TextView);
                grade = 2;
            }
        });

        RxView.clicks(m3TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m3TextView);
                grade = 3;
            }
        });

        RxView.clicks(m4TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m4TextView);
                grade = 4;
            }
        });

        RxView.clicks(m5TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m5TextView);
                grade = 5;
            }
        });

        RxView.clicks(m6TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m6TextView);
                grade = 6;
            }
        });

        RxView.clicks(m7TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m7TextView);
                grade = 7;
            }
        });

        RxView.clicks(m8TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m8TextView);
                grade = 8;
            }
        });

        RxView.clicks(m9TextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                select(m9TextView);
                grade = 9;
            }
        });

        RxView.clicks(mComeinButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(grade == -1){
                    grade = SPUtils.getInstance().getInt("grade", 0);
                }
                SPUtils.getInstance().put("grade", grade);

                String period = "-1";
                if (grade > 6) {
                    period = "1";
                } else if (grade > 0 && grade <= 6) {
                    period = "0";
                } else {
                    ToastUtils.showShort("请选择你所在年级");
                    return;
                }
                dismiss();
                SPUtils.getInstance().put("period", period);
                EnglishApp.get().setHttpDefaultParams();
                if (mRunnable != null) {
                    mRunnable.run();
                }
                RxBus.get().post(Constant.GRADE_REFRESH, "from select grade");
                RxBus.get().post(Constant.GET_UNIT, "from select grade");
            }
        });
    }

    private void clear() {
        m0TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m1TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m2TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m3TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m4TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m5TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m6TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m7TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m8TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        m9TextView.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));

        m0TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m1TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m2TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m3TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m4TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m5TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m6TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m7TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m8TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));
        m9TextView.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.gray_ddd));

    }

    private Runnable mRunnable;

    public void show(View view, Runnable runnable) {
        show(view, Gravity.CENTER);
        mRunnable = runnable;

    }

    public void select(TextView view) {
        clear();
        view.setBackground(DrawableUtils.setBg(mContext, 2, 1, R.color.group_red_fe908c));
        view.setTextColor(ContextCompat.getColor(mContext, R.color.group_red_fe908c));
    }

    @Override
    public void setContentView(View contentView) {
        if (contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.base_ppw_select_grade;
    }

    @Override
    public int getAnimationID() {
        return 0;
    }
}
