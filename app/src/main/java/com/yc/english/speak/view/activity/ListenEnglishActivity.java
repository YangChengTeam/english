package com.yc.english.speak.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.speak.contract.ListenEnglishContract;
import com.yc.english.speak.contract.ListenPlayContract;
import com.yc.english.speak.model.bean.ListenEnglishBean;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;
import com.yc.english.speak.presenter.ListenEnglishPresenter;
import com.yc.english.speak.presenter.LyricViewPresenter;
import com.yc.english.speak.service.MusicPlayService;
import com.yc.english.speak.view.wdigets.LyricView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/10/19.
 */

public class ListenEnglishActivity extends FullScreenActivity<ListenEnglishPresenter> implements ListenEnglishContract.View, ListenPlayContract.View, View.OnClickListener, SeekBar.OnSeekBarChangeListener, LyricView.OnPlayerClickListener {

    private static final String TAG = "MainFragment";

    private ListenPlayContract.Presenter mPlayPresenter;

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
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    private LyricViewPresenter mLyricViewPresenter;

    private Intent mPlayService;

    private int progresses = 0;

    private int total = 0;

    private File audioFile;

    private File lrcFile;

    private boolean isDownSuccess;

    private boolean isURLValidate;

    private String fileName = "";//ID

    private String currentAudioUrl;

    private String currentAudioLrcUrl;

    private List<SpeakAndReadInfo> listenList;

    private SpeakAndReadItemInfo currentItemInfo;

    private int outDataPosition;

    private int innerDataPosition;

    private boolean isPrevOver;

    private boolean isNextOver;

    @Override
    public int getLayoutId() {
        return R.layout.speak_listen_english_activity;
    }

    @Override
    public void setPresenter(@NonNull ListenPlayContract.Presenter presenter) {
        mPlayPresenter = presenter;
    }

    @Override
    public void init() {
        mToolbar.setTitle("听英语");
        mToolbar.showNavigationIcon();
        mToolbar.setTitleColor(ContextCompat.getColor(this, R.color.white));

        Intent intent = getIntent();
        currentItemInfo = (SpeakAndReadItemInfo) intent.getParcelableExtra("itemInfo");
        listenList = (ArrayList) intent.getParcelableArrayListExtra("infoList");
        fileName = currentItemInfo.getId();

        setListener();
        FileDownloader.setup(this);
        mPresenter = new ListenEnglishPresenter(this, this);
        mPresenter.getListenEnglishDetail(currentItemInfo.getId());
    }

    public boolean getPrevInfo() {
        innerDataPosition--;
        if (innerDataPosition < 0) {
            outDataPosition--;
            if (outDataPosition < 0) {
                isPrevOver = true;
            } else {
                if (listenList.get(outDataPosition).getData() != null) {
                    innerDataPosition = listenList.get(outDataPosition).getData().size() - 1;

                    SpeakAndReadItemInfo speakAndReadItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
                    fileName = speakAndReadItemInfo.getId();
                    currentAudioUrl = speakAndReadItemInfo.getMp3();
                    currentAudioLrcUrl = speakAndReadItemInfo.getWord_file();
                } else {
                    isPrevOver = true;
                }
            }
        } else {
            SpeakAndReadItemInfo speakAndReadItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
            fileName = speakAndReadItemInfo.getId();
            currentAudioUrl = speakAndReadItemInfo.getMp3();
            currentAudioLrcUrl = speakAndReadItemInfo.getWord_file();
        }
        return isPrevOver;
    }

    public boolean getNextInfo() {
        innerDataPosition++;
        if (innerDataPosition >= listenList.get(outDataPosition).getData().size()) {
            outDataPosition++;
            if (outDataPosition >= listenList.size()) {
                isNextOver = true;
            } else {
                if (listenList.get(outDataPosition).getData() != null) {
                    innerDataPosition = 0;

                    SpeakAndReadItemInfo speakAndReadItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
                    fileName = speakAndReadItemInfo.getId();
                    currentAudioUrl = speakAndReadItemInfo.getMp3();
                    currentAudioLrcUrl = speakAndReadItemInfo.getWord_file();
                } else {
                    isNextOver = true;
                }
            }
        } else {
            SpeakAndReadItemInfo speakAndReadItemInfo = listenList.get(outDataPosition).getData().get(innerDataPosition);
            fileName = speakAndReadItemInfo.getId();
            currentAudioUrl = speakAndReadItemInfo.getMp3();
            currentAudioLrcUrl = speakAndReadItemInfo.getWord_file();
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
                    mPlayPresenter.onBtnPlayPausePressed();
                } else {
                    if (!isURLValidate) {
                        ToastUtils.showLong("资源文件下载有误，请重试");
                        return;
                    }
                    ToastUtils.showLong("资源文件下载中");
                }
                break;
            case R.id.btn_prev:
                if (!getPrevInfo()) {
                    downAudioFile();
                } else {
                    ToastUtils.showLong("暂无更多信息");
                }
                break;
            case R.id.btn_next:
                if (!getNextInfo()) {
                    downAudioFile();
                } else {
                    ToastUtils.showLong("暂无更多信息");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setListener() {
        mPrev.setOnClickListener(ListenEnglishActivity.this);
        mPlayPause.setOnClickListener(ListenEnglishActivity.this);
        mNext.setOnClickListener(ListenEnglishActivity.this);
        mSeekBar.setOnSeekBarChangeListener(ListenEnglishActivity.this);
        mLyricView.setOnPlayerClickListener(ListenEnglishActivity.this);
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

    public void downAudioFile() {
        progresses = 0;
        total = 0;

        File fileDir = new File(SDCardUtils.getSDCardPath() + "/yc_english");
        if (FileUtils.createOrExistsDir(fileDir)) {

            String audioPath = fileDir + "/" + fileName + ".mp3";
            audioFile = new File(audioPath);
            if (!audioFile.exists()) {
                isURLValidate = URLIsValidate(currentAudioUrl, "mp3");
                if (isURLValidate) {
                    FileDownloader.getImpl().create(currentAudioUrl).setPath(audioPath, false).setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                            ToastUtils.showLong("开始下载资源文件");
                            if (ActivityUtils.isValidContext(ListenEnglishActivity.this)) {
                                Glide.with(ListenEnglishActivity.this).load(R.mipmap.base_loading)
                                        .apply(new RequestOptions().override(SizeUtils.dp2px(1080 / 3), SizeUtils.dp2px(408 / 3))).into(mIvLoading);
                            }
                            total = totalBytes / 1024 / 1024;
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            total = totalBytes / 1024 / 1024;
                            progresses = soFarBytes / 1024 / 1024;
                            Log.e("progress total", "progress total--->" + total + "progress--->" + progresses);
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            progresses = total;
                            Log.e("completed progress", " completed progress--->" + progresses);
//                            ToastUtils.showLong("资源文件下载完成");
                            downAudioLrcFile();//继续下载资源词句文件
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                        }

                    }).start();
                } else {
                    ToastUtils.showLong("资源文件下载有误，请重试");
                }
            } else {
                downAudioLrcFile();//继续下载资源词句文件
            }
        }
    }

    public void downAudioLrcFile() {
        progresses = 0;
        total = 0;

        File fileDir = new File(SDCardUtils.getSDCardPath() + "/yc_english");

        if (FileUtils.createOrExistsDir(fileDir)) {

            String lrcPath = fileDir + "/" + fileName + ".lrc";
            lrcFile = new File(lrcPath);
            if (!lrcFile.exists()) {
                isURLValidate = URLIsValidate(currentAudioLrcUrl, "lrc");
                if (isURLValidate) {
                    FileDownloader.getImpl().create(currentAudioLrcUrl).setPath(lrcPath, false).setListener(new FileDownloadListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                            ToastUtils.showLong("开始下载资源文件");
                            total = totalBytes / 1024 / 1024;
                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            total = totalBytes / 1024 / 1024;
                            progresses = soFarBytes / 1024 / 1024;
                            Log.e("progress total", "progress total--->" + total + "progress--->" + progresses);
                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            progresses = total;
                            Log.e("completed progress", " completed progress--->" + progresses);

                            isDownSuccess = true;
                            if (ActivityUtils.isValidContext(ListenEnglishActivity.this)) {
                                Glide.with(ListenEnglishActivity.this).clear(mIvLoading);
                            }
                            mIvLoading.setVisibility(View.GONE);

                            mLyricViewPresenter = new LyricViewPresenter(ListenEnglishActivity.this, ListenEnglishActivity.this, audioFile.getAbsolutePath());
                            startService(mPlayService);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {

                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                        }

                    }).start();
                } else {
                    ToastUtils.showLong("资源文件下载有误，请重试");
                }
            } else {
                isDownSuccess = true;

                startService(mPlayService);
                mLyricViewPresenter = new LyricViewPresenter(ListenEnglishActivity.this, ListenEnglishActivity.this, audioFile.getAbsolutePath());
            }
        }
    }

    @Override
    public void initLrcView(File lrcFile) {
        mLyricView.setLyricFile(lrcFile);
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
        } else {
            mPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.btn_pause_selector));
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mPlayPresenter.onProgressChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPlayPresenter.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPlayPresenter.play();
    }

    @Override
    public void onPlayerClicked(long progress, String content) {
        mPlayPresenter.play();
        mPlayPresenter.onProgressChanged((int) progress);
    }


    @Override
    public void hideStateView() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showListenEnglishDetail(ListenEnglishBean listenEnglishBean) {
        fileName = currentItemInfo.getId();
        currentAudioUrl = listenEnglishBean.getMp3();
        currentAudioLrcUrl = listenEnglishBean.getWordFile();
        LogUtils.e("file url--->" + currentAudioUrl + "---lrc url --->" + currentAudioLrcUrl);
        mPlayService = new Intent(ListenEnglishActivity.this, MusicPlayService.class);
        downAudioFile();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mPlayPresenter != null) {
            mPlayPresenter.destroy();
        }
        if (mPlayService != null) {
            stopService(mPlayService);
        }
    }

}
