package com.yc.junior.english.intelligent.view.wdigets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yc.junior.english.R;

import butterknife.BindView;
import yc.com.base.BaseView;


/**
 * Created by wanglin  on 2018/4/17 08:48.
 */

public class IntelligentRingView extends BaseView {
    @BindView(R.id.ringView)
    RingView ringView;
    @BindView(R.id.tv_correct_answer)
    TextView tvCorrectAnswer;
    @BindView(R.id.tv_error_answer)
    TextView tvErrorAnswer;

    private String mRing;
    private int mCorrect;
    private int mError;

    public IntelligentRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tvCorrectAnswer.setText(String.format(mContext.getString(R.string.intell_correct_answer), mCorrect));
        tvErrorAnswer.setText(String.format(mContext.getString(R.string.intell_error_answer), mError));
    }

    @Override
    public int getLayoutId() {
        return R.layout.intelligent_ring_view;
    }

//    @Override
//    public void init() {
//        super.init();
////        ringView.setText(mRing);
//        tvCorrectAnswer.setText(String.format(mContext.getString(R.string.intell_correct_answer), mCorrect));
//        tvErrorAnswer.setText(String.format(mContext.getString(R.string.intell_error_answer), mError));
//    }

    public void setData(String ring, int correct, int error) {
        this.mRing = ring;
        this.mCorrect = correct;
        this.mError = error;
        invalidate();
    }

    public String getmRing() {
        return mRing;
    }

    public int getmCorrect() {
        return mCorrect;
    }

    public int getmError() {
        return mError;
    }
}
