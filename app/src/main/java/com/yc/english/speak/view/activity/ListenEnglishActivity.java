package com.yc.english.speak.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.speak.contract.MainPlayContract;
import com.yc.english.speak.utils.MusicPlayService;
import com.yc.english.speak.utils.TextUtils;
import com.yc.english.speak.view.presenter.MainPresenter;

import java.io.File;

import butterknife.BindView;
import me.zhengken.lyricview.LyricView;

/**
 * Created by admin on 2017/10/19.
 */

public class ListenEnglishActivity extends FullScreenActivity implements MainPlayContract.View,View.OnClickListener, SeekBar.OnSeekBarChangeListener, LyricView.OnPlayerClickListener{

    private static final String TAG = "MainFragment";

    private MainPlayContract.Presenter mPresenter;

    private boolean displayLrc = false;

    @BindView(R.id.background_blur)
    ImageView mCoverGauss;

    @BindView(R.id.linear_layout_music_cover)
    LinearLayout mDisplayLrc;

    @BindView(R.id.custom_lyric_view)
    LyricView mLyricView;

    @BindView(R.id.cover)
    ImageView mCover;

    @BindView(R.id.cover_mirror)
    ImageView mCoverMirror;

    @BindView(R.id.music_title)
    TextView mTitle;

    @BindView(R.id.music_artist)
    TextView mArtist;

    @BindView(R.id.end_time)
    TextView mEndTime;

    @BindView(R.id.start_time)
    TextView mStartTime;

    @BindView(R.id.music_seek_bar)
    SeekBar mSeekBar;

    @BindView(R.id.btn_mode)
    ImageView mPlayMode;

    @BindView(R.id.btn_prev)
    ImageView mPrev;

    @BindView(R.id.btn_play_pause)
    ImageView mPlayPause;

    @BindView(R.id.btn_next)
    ImageView mNext;

    private MainPresenter mainPresenter;

    private Intent mPlayService;

    @Override
    public int getLayoutId() {
        return R.layout.speak_listen_english_activity;
    }

    @Override
    public void setPresenter(@NonNull MainPlayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void init() {

        mPlayService = new Intent(this, MusicPlayService.class);
        startService(mPlayService);
        mainPresenter = new MainPresenter(this, this);

        setListener();
    }
    @Override
    public void updateTitle(String title) {
        if (mTitle == null) {
            return;
        }
        mTitle.setText(title);
    }

    @Override
    public void updateArtist(String artist) {
        if (mArtist == null) {
            return;
        }
        mArtist.setText(artist);
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
                mPresenter.onBtnPlayPausePressed();
                break;
            case R.id.linear_layout_music_cover:
                if (displayLrc = !displayLrc) {
                    mLyricView.setVisibility(View.VISIBLE);
                    mCover.setVisibility(View.GONE);
                    mCoverMirror.setVisibility(View.GONE);
                } else {
                    mLyricView.setVisibility(View.GONE);
                    mCover.setVisibility(View.VISIBLE);
                    mCoverMirror.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void setListener() {
        mDisplayLrc.setOnClickListener(ListenEnglishActivity.this);
        mPlayMode.setOnClickListener(ListenEnglishActivity.this);
        mPrev.setOnClickListener(ListenEnglishActivity.this);
        mPlayPause.setOnClickListener(ListenEnglishActivity.this);
        mNext.setOnClickListener(ListenEnglishActivity.this);
        mSeekBar.setOnSeekBarChangeListener(ListenEnglishActivity.this);
        mLyricView.setOnPlayerClickListener(ListenEnglishActivity.this);
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
        bitmap = null;
        if (bitmap != null) {
            mCoverGauss.setImageBitmap(bitmap);
        } else {
            mCoverGauss.setImageDrawable(getResources().getDrawable(R.drawable.default_cover_blur));
        }
    }

    @Override
    public void updateCover(Bitmap bitmap) {
        bitmap = null;
        if (bitmap != null) {
            mCover.setImageBitmap(bitmap);
        } else {
            mCover.setImageDrawable(getResources().getDrawable(R.drawable.default_cover));
        }
    }

    @Override
    public void updateCoverMirror(Bitmap bitmap) {
        bitmap = null;
        if (bitmap != null) {
            mCoverMirror.setImageBitmap(bitmap);
        } else {
            mCover.setImageDrawable(getResources().getDrawable(R.drawable.default_cover_mirror));
        }
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
        mStartTime.setText(TextUtils.duration2String(progress));
        mSeekBar.setProgress(progress);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mPresenter.onProgressChanged(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mPresenter.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mPresenter.play();
    }

    @Override
    public void onPlayerClicked(long progress, String content) {
        mPresenter.play();
        mPresenter.onProgressChanged((int) progress);
    }
}
