package com.yc.junior.english.read.view.fragment;

import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.kk.utils.ScreenUtil;
import com.yc.junior.english.R;
import com.yc.soundmark.base.constant.SpConstant;

import java.math.BigDecimal;

import butterknife.BindView;
import yc.com.base.BaseDialogFragment;
import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2019/4/4 10:05.
 */
public class SpeedSettingFragment extends BaseDialogFragment {
    @BindView(R.id.speak_seekBar)
    AppCompatSeekBar speakSeekBar;

    private int seekUnit;

    @Override
    public int getLayoutId() {
        return R.layout.layout_play_speed_view;
    }

    @Override
    public void init() {

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        speakSeekBar.setProgress(SPUtils.getInstance().getInt(SpConstant.PLAY_SPEED, 40));

        speakSeekBar.setPadding(ScreenUtil.dip2px(getActivity(), 6), 0, ScreenUtil.dip2px(getActivity(), 6), 0);
        final int max = speakSeekBar.getMax();
        seekUnit = max / 8;

        speakSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                float rate = progress / (100 * 1.0f);
                BigDecimal bd = new BigDecimal(rate);

                rate = bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                seekBar.setProgress((int) (rate * seekUnit * 10));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        int progress = speakSeekBar.getProgress();

//        LogUtil.msg("progress:  " + progress);
        SPUtils.getInstance().put(SpConstant.PLAY_SPEED, progress);
        if (listener != null) {
            listener.onDissmiss();
        }
    }


    private onDissmissListener listener;

    public void setOnDissmissListener(onDissmissListener listener) {
        this.listener= listener;
    }

    public interface onDissmissListener {
        void onDissmiss();
    }

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }
}
