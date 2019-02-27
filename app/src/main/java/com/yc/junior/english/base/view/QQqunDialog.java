package com.yc.junior.english.base.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/8.
 */

public class QQqunDialog extends BaseDialog {

    @BindView(R.id.layout_xiao_xue)
    LinearLayout mXiaoXueLayout;

    @BindView(R.id.layout_zhong_xue)
    LinearLayout mZhongXueLayout;

    public QQqunDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        RxView.clicks(mXiaoXueLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (qqunClick != null) {
                    qqunClick.xiaoxueClick();
                }
                dismiss();
            }
        });

        RxView.clicks(mZhongXueLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (qqunClick != null) {
                    qqunClick.zhongxueClick();
                }
                dismiss();
            }
        });
    }

    public interface QQqunClick {

        void xiaoxueClick();

        void zhongxueClick();
    }

    public QQqunClick qqunClick;

    public void setQqunClick(QQqunClick qqunClick) {
        this.qqunClick = qqunClick;
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_qq_dialog;
    }
}
