package cn.jzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by wanglin  on 2017/11/24 10:24.
 */

public class JZVideoPlayerRevise extends JZVideoPlayerStandard implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;

    public JZVideoPlayerRevise(Context context) {
        super(context);
    }

    public JZVideoPlayerRevise(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jz_layout_revise;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        surfaceView.setOnTouchListener(this);
        surfaceView.setOnClickListener(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 准备prepare();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放资源并销毁releaseWithoutStop();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.surfaceView) {

        }
    }
}
