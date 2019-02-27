package yc.com.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.kk.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/3/6 10:14.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog {


    protected P mPresenter;
    protected BaseLoadingView baseLoadingView;
    protected Handler mHandler;
    private MyRunnable taskRunnable;
    private int statusHeight;

    public int getStatusHeight() {
        return statusHeight;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        RxBus.get().register(this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            LogUtil.msg("-->: 初始化失败 " + e.getMessage());
        }
        statusHeight = StatusBarUtil.getStatusBarHeight(this);

        baseLoadingView = new BaseLoadingView(this);
        mHandler = new Handler();
        //顶部透明

//        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
        init();
//        setStatusBar();

        if (isStatusBarMateria())
            setStatusBarMateria();
    }

    protected void setStatusBar() {

        StatusBarUtil.setTranslucentForImageView(this, null);
    }

    public abstract boolean isStatusBarMateria();

    protected void setStatusBarMateria() {
        StatusBarUtil.setMaterialStatus(this);
    }

    public void setToolbarTopMargin(View view) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
        mHandler.removeCallbacks(taskRunnable);
        taskRunnable = null;
        mHandler = null;
        RxBus.get().unregister(this);
    }

    @Override
    public void showLoadingDialog(String mess) {
        if (!this.isFinishing()) {
            if (null != baseLoadingView) {
                baseLoadingView.setMessage(mess);
                baseLoadingView.show();
            }
        }
    }

    @Override
    public void dismissDialog() {
        try {
            if (!this.isFinishing()) {
                if (null != baseLoadingView && baseLoadingView.isShowing()) {
                    baseLoadingView.dismiss();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 改变获取验证码按钮状态
     */
    public void showGetCodeDisplay(TextView textView) {
        taskRunnable = new MyRunnable(textView);
        if (null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
            totalTime = 60;
            textView.setClickable(false);
//            textView.setTextColor(ContextCompat.getColor(R.color.coment_color));
//            textView.setBackgroundResource(R.drawable.bg_btn_get_code);
            if (null != mHandler) mHandler.postDelayed(taskRunnable, 0);
        }
    }

    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime = 60;


    private class MyRunnable implements Runnable {
        TextView mTv;

        public MyRunnable(TextView textView) {
            this.mTv = textView;
        }

        @Override
        public void run() {
            mTv.setText(totalTime + "秒后重试");
            totalTime--;
            if (totalTime < 0) {
                //还原
                initGetCodeBtn(mTv);
                return;
            }
            if (null != mHandler) mHandler.postDelayed(this, 1000);
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn(TextView textView) {
        totalTime = 0;
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
        }
        textView.setText("重新获取");
        textView.setClickable(true);
//        textView.setTextColor(CommonUtils.getColor(R.color.white));
//        textView.setBackgroundResource(R.drawable.bg_btn_get_code_true);
    }

}
