package yc.com.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * TinyHung@outlook.com
 * 2017/3/25 15:16
 * 加载进度条
 */

public class BaseLoadingView extends Dialog {

    private TextView mTextView;
    private boolean isBack = false;
    private final AnimationDrawable mAnimationDrawable;
    private final ImageView mIvProgressBar;


    public BaseLoadingView(Context context) {
        super(context, R.style.LoadingProgressDialogStyle);
        setContentView(R.layout.dialog_progress_layout);
        mTextView = findViewById(R.id.tv_loading_message);
        mIvProgressBar = findViewById(R.id.iv_loading_icon);
        mAnimationDrawable = (AnimationDrawable) mIvProgressBar.getDrawable();

        mIvProgressBar.setVisibility(View.VISIBLE);
        if (null != mAnimationDrawable && !mAnimationDrawable.isRunning())
            mAnimationDrawable.start();

//        setCancelable(false);
//        setCanceledOnTouchOutside(false);

    }



    @Override
    public void dismiss() {
        if (null != mAnimationDrawable && mAnimationDrawable.isRunning()) mAnimationDrawable.stop();
        if (null != mIvProgressBar) mIvProgressBar.setVisibility(View.VISIBLE);

        super.dismiss();
    }

    public interface OnDialogBackListener {
        void onBack();
    }

    private OnDialogBackListener mOnDialogBackListener;

    public void setOnDialogBackListener(OnDialogBackListener onDialogBackListener) {
        mOnDialogBackListener = onDialogBackListener;
    }

    /**
     * 将用户按下返回键时间传递出去
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isBack) {
                if (mOnDialogBackListener != null) {
                    mOnDialogBackListener.onBack();
                }
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置文字
     *
     * @param message
     */
    public void setMessage(String message) {
        mTextView.setText(message);
    }

    private Handler mHandler = new Handler();

    /**
     * 设置文字，是否完成加载，自动关闭时间
     *
     * @param message
     * @param isFinlish
     * @param duration
     */
    public void setMessage(String message, boolean isFinlish, int duration) {
        mTextView.setText(message);
        if (isFinlish) {
            if (mIvProgressBar != null) {
                mIvProgressBar.setVisibility(View.GONE);
                if (null != mAnimationDrawable && mAnimationDrawable.isRunning())
                    mAnimationDrawable.stop();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, duration);
            }
        }
    }

    /**
     * 设置点击空白处是否关闭Dialog
     */
    public void onSetCancelable(boolean isClose) {
        setCancelable(isClose);
    }

    /**
     * 设置返回键是否可用
     */
    public void onSetCanceledOnTouchOutside(boolean isBack) {
        this.isBack = isBack;
        setCanceledOnTouchOutside(isBack);
    }

    /**
     * 执行完成播放动画的动作
     */
    public void setResultsCompletes(String message, int textColor, boolean isFinlish, int duration) {
        mTextView.setText(message);
        mTextView.setTextColor(Color.WHITE);
        if (isFinlish) {
            if (mIvProgressBar != null) {
                mIvProgressBar.setVisibility(View.GONE);
                if (null != mAnimationDrawable && mAnimationDrawable.isRunning())
                    mAnimationDrawable.stop();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseLoadingView.this.dismiss();
                    }
                }, duration);
            }
        }
    }
}
