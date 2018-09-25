package com.yc.english.speak.view.activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.speak.contract.ListenEnglishContract;
import com.yc.english.speak.contract.ListenPlayContract;
import com.yc.english.speak.model.bean.ListenEnglishBean;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;
import com.yc.english.speak.presenter.ListenEnglishPresenter;
import com.yc.english.speak.presenter.LyricViewPresenter;
import com.yc.english.speak.utils.NotificationUtil;
import com.yc.english.speak.view.wdigets.LyricView;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/19.
 * view
 *
 */

public class ListenEnglishActivity extends FullScreenActivity<ListenEnglishPresenter> implements ListenEnglishContract.View, ListenPlayContract.View, View.OnClickListener, SeekBar.OnSeekBarChangeListener, LyricView.OnPlayerClickListener {


    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.linear_layout_music_cover)
    FrameLayout linearLayoutMusicCover;

    @BindView(R.id.custom_lyric_view)
    LyricView mLyricView;

    @BindView(R.id.end_time)
    TextView mEndTime;

    @BindView(R.id.start_time)
    TextView mStartTime;

    @BindView(R.id.music_seek_bar)
    SeekBar mSeekBar;

    @BindView(R.id.btn_prev)
    ImageView mPrev;

    @BindView(R.id.btn_play_pause)
    ImageView mPlayPause;

    @BindView(R.id.btn_next)
    ImageView mNext;

    private LyricViewPresenter mLyricViewPresenter;


    private String currentAudioUrl;//播放的MP3音频地址.mp3

    private String currentAudioLrcUrl;//音频歌词文件地址.lrc

    private boolean isDownSuccess;

    private List<SpeakAndReadInfo> listenList;

    private SpeakAndReadItemInfo currentItemInfo;

    private int outDataPosition;

    private int innerDataPosition;

    private boolean isPrevOver;//是否有上一个

    private boolean isNextOver;//是否有下一个


    private ListenEnglishBean listenEnglishBean;
    private NotificationPlayerReceiver receiver;

    @Override
    public int getLayoutId() {
        return R.layout.speak_listen_english_activity;
    }

    @Override
    public void setPresenter(@NonNull ListenPlayContract.Presenter presenter) {
    }

    @Override
    public void init() {

        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.white));

        Intent intent = getIntent();
        if (null != intent) {
            currentItemInfo = intent.getParcelableExtra("itemInfo");
            listenList = intent.getParcelableArrayListExtra("infoList");


            outDataPosition = currentItemInfo.getOutPos();
            innerDataPosition = currentItemInfo.getInnerPos();

            mToolbar.setTitle(currentItemInfo.getTitle());
            setListener();

            mPresenter = new ListenEnglishPresenter(this, this);
            mPresenter.getListenEnglishDetail(currentItemInfo.getId());
            mLyricViewPresenter = new LyricViewPresenter(this, this);
            mLyricViewPresenter.startService();
            listenEnglishBean = new ListenEnglishBean();

        }
        registerReciver();
    }

    public boolean getPrevInfo() {

        LogUtils.e("outDataPosition getPrevInfo: " + outDataPosition + "--innerDataPosition--" + innerDataPosition);
        if (!isPrevOver) {
            innerDataPosition--;
            if (innerDataPosition < 0) {//
                outDataPosition--;
                if (outDataPosition < 0) {
                    outDataPosition = 0;
                    innerDataPosition = 0;
                    isPrevOver = true;
                    isNextOver = false;
                } else {
                    if (listenList != null && listenList.get(outDataPosition).getData() != null) {
                        innerDataPosition = listenList.get(outDataPosition).getData().size() - 1;
                        currentItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);

                        mToolbar.setTitle(currentItemInfo.getTitle());

                        currentAudioUrl = currentItemInfo.getMp3();
                        currentAudioLrcUrl = currentItemInfo.getWord_file();

                        setListenBean(currentItemInfo);
                    } else {
                        isPrevOver = true;
                        isNextOver = false;
                    }
                }
            } else {
                if (outDataPosition > -1 && outDataPosition < listenList.size() && innerDataPosition > -1 && innerDataPosition < listenList.get(outDataPosition).getData().size()) {
                    currentItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
                    mToolbar.setTitle(currentItemInfo.getTitle());

                    currentAudioUrl = currentItemInfo.getMp3();
                    currentAudioLrcUrl = currentItemInfo.getWord_file();

                    setListenBean(currentItemInfo);

                    isNextOver = false;
                } else {
                    isPrevOver = true;
                    isNextOver = false;
                }
            }
        }
        return isPrevOver;
    }


    private void setListenBean(SpeakAndReadItemInfo speakAndReadItemInfo) {
        if (listenEnglishBean == null) listenEnglishBean = new ListenEnglishBean();
        listenEnglishBean.setMp3(speakAndReadItemInfo.getMp3());
        listenEnglishBean.setWordFile(speakAndReadItemInfo.getWord_file());
        listenEnglishBean.setId(speakAndReadItemInfo.getId());
    }

    public boolean getNextInfo() {

        LogUtils.e("outDataPosition getNextInfo: " + outDataPosition + "--innerDataPosition--" + innerDataPosition);
        if (!isNextOver) {
            innerDataPosition++;
            if (outDataPosition > -1 && innerDataPosition >= listenList.get(outDataPosition).getData().size()) {
                outDataPosition++;
                if (outDataPosition >= listenList.size()) {
                    outDataPosition = listenList.size() - 1;
                    innerDataPosition = listenList.get(outDataPosition).getData().size() - 1;
                    isNextOver = true;
                    isPrevOver = false;
                } else {
                    if (listenList.get(outDataPosition).getData() != null) {
                        innerDataPosition = 0;

                        currentItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);

                        mToolbar.setTitle(currentItemInfo.getTitle());

                        currentAudioUrl = currentItemInfo.getMp3();
                        currentAudioLrcUrl = currentItemInfo.getWord_file();

                        setListenBean(currentItemInfo);
                    } else {
                        isNextOver = true;
                        isPrevOver = false;
                    }
                }
            } else {
                if (outDataPosition > -1 && outDataPosition < listenList.size() && innerDataPosition > -1 && innerDataPosition < listenList.get(outDataPosition).getData().size()) {
                    currentItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
                    mToolbar.setTitle(currentItemInfo.getTitle());

                    currentAudioUrl = currentItemInfo.getMp3();
                    currentAudioLrcUrl = currentItemInfo.getWord_file();
                    setListenBean(currentItemInfo);
                    isPrevOver = false;
                } else {
                    isNextOver = true;
                    isPrevOver = false;
                }
            }
        }
        return isNextOver;
    }


    @Override
    public void updateTitle(String title) {

    }

    @Override
    public void updateArtist(String artist) {
    }

    @Override
    public void setEndTime(String time) {
        if (mEndTime == null) {
            return;
        }
        mEndTime.setText(time);
    }

    @Override
    public void resetSeekBar(int max) {
        if (mSeekBar == null) {
            return;
        }
        mSeekBar.setMax(max);
        mSeekBar.setProgress(0);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btn_play_pause:

                if (isDownSuccess) {
                    mLyricViewPresenter.onBtnPlayPausePressed();
                } else {
                    if (!URLIsValidate(currentAudioUrl, "mp3") || !URLIsValidate(currentAudioLrcUrl, "lrc")) {
                        ToastUtils.showLong("资源文件下载有误，请重试");
                        return;
                    }
                    ToastUtils.showLong("资源文件下载中");
                }
                break;
            case R.id.btn_prev:
                pre();
                break;
            case R.id.btn_next:
                next();
                break;
            default:
                break;
        }

    }

    public boolean URLIsValidate(String url, String type) {

        if (StringUtils.isEmpty(url)) {
            return false;
        }

        if (url.substring(url.lastIndexOf("."), url.length()).toLowerCase().endsWith(type)) {
            return true;
        }

        return false;
    }

    @Override
    public void setListener() {
        mPrev.setOnClickListener(ListenEnglishActivity.this);
        mPlayPause.setOnClickListener(ListenEnglishActivity.this);
        mNext.setOnClickListener(ListenEnglishActivity.this);
        mSeekBar.setOnSeekBarChangeListener(ListenEnglishActivity.this);
        mLyricView.setOnPlayerClickListener(ListenEnglishActivity.this);
    }


    @Override
    public void initLrcView(File lrcFile) {
        mLyricView.setLyricFile(lrcFile);
        setNotifyFlag(false, true);

    }


    @Override
    public void updateLrcView(int progress) {
        mLyricView.setCurrentTimeMillis(progress);
    }

    @Override
    public void updateCoverGauss(Bitmap bitmap) {
    }

    @Override
    public void updateCover(Bitmap bitmap) {
        bitmap = null;
    }

    @Override
    public void updateCoverMirror(Bitmap bitmap) {
        bitmap = null;
    }

    @Override
    public void updatePlayButton(boolean setPlayImage) {
        if (mPlayPause == null) {
            return;
        }
        if (setPlayImage) {
            mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.btn_play_selector));

            setNotifyFlag(false, false);
        } else {
            mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause_selector));
            setNotifyFlag(false, true);
        }
    }

    @Override
    public void updateSeekBar(int progress) {
        if (mSeekBar == null || mStartTime == null) {
            return;
        }
        mStartTime.setText(TimeUtils.duration2String(progress));
        mSeekBar.setProgress(progress);
    }

    @Override
    public void updateDone(boolean isDone) {
        isDownSuccess = isDone;
    }

    @Override
    public void onCompletion() {
        next();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mLyricViewPresenter.onProgressChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        mLyricViewPresenter.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mLyricViewPresenter.play();
    }

    @Override
    public void onPlayerClicked(long progress, String content) {

        mLyricViewPresenter.play();
        mLyricViewPresenter.onProgressChanged((int) progress);
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(linearLayoutMusicCover, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getListenEnglishDetail(currentItemInfo.getId());
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(linearLayoutMusicCover);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(linearLayoutMusicCover);
    }

    @Override
    public void showListenEnglishDetail(ListenEnglishBean listenEnglishBean) {

        currentAudioUrl = listenEnglishBean.getMp3();
        currentAudioLrcUrl = listenEnglishBean.getWordFile();
        LogUtils.e("file url--->" + currentAudioUrl + "  ---lrc url --->" + currentAudioLrcUrl);

        mLyricViewPresenter.downAudioFile(listenEnglishBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLyricViewPresenter != null) {
            mLyricViewPresenter.destroy();
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
        setNotifyFlag(true, false);

    }

    //上一首
    private void pre() {
        if (!getPrevInfo()) {
            mLyricViewPresenter.downAudioFile(listenEnglishBean);
        } else {
            ToastUtils.showLong("暂无更多歌曲");
        }
    }

    //下一首
    private void next() {
        if (!getNextInfo()) {
            mLyricViewPresenter.downAudioFile(listenEnglishBean);
        } else {
            ToastUtils.showLong("暂无更多歌曲");
        }
    }

    private void setNotifyFlag(boolean isClear, boolean isPlay) {
        int flag = Notification.FLAG_NO_CLEAR;
        if (isClear) {
            flag = Notification.FLAG_AUTO_CANCEL;
        }
        NotificationUtil.showNotify(this, currentItemInfo.getTitle(), isPlay, flag);
    }

    private void registerReciver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NOTIFY_NEXT);
        intentFilter.addAction(Constant.NOTIFY_PRE);
        intentFilter.addAction(Constant.NOTIFY_PLAY_PAUSE);
        intentFilter.addAction(Constant.NOTIFY_CANCEL);
        receiver = new NotificationPlayerReceiver();
        this.registerReceiver(receiver, intentFilter);
    }

    public class NotificationPlayerReceiver extends BroadcastReceiver {

        private static final String TAG = "NotificationPlayerRecei";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);
            if (!TextUtils.isEmpty(action)) {
                switch (action) {
                    case Constant.NOTIFY_NEXT:
                        next();
                        break;
                    case Constant.NOTIFY_PRE:
                        pre();
                        break;
                    case Constant.NOTIFY_PLAY_PAUSE:
                        mLyricViewPresenter.onBtnPlayPausePressed();
                        break;
                    case Constant.NOTIFY_CANCEL:
                        NotificationUtil.clear(ListenEnglishActivity.this);
                        break;
                }
            }
        }
    }


}
