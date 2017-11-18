package com.yc.english.speak.utils;

import android.media.MediaMetadataRetriever;

import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;


public class EnglishLyricBean {

    public EnglishLyricBean(String songPath) {
        mSongPath = songPath;
    }

    private String mTitle;

    private String mArtist;

    private String mLrcPath;

    private String mSongPath;

    private int mDuration;

    public String getmSongPath() {
        return mSongPath;
    }

    public void setLrcPath() {
        String lrcPath = mSongPath.substring(0, mSongPath.length() - 3) + "lrc";
        LogUtils.e("setLrcPath--->" + lrcPath);
        mLrcPath = lrcPath;
    }

    public String getLrcPath() {
        if (mLrcPath == null) {
            setLrcPath();
        }

        if (isLrcExist()) {
            return mLrcPath;
        }
        return null;
    }

    public boolean isLrcExist() {
        File lrcFile = new File(mLrcPath);
        if (lrcFile.exists()) {
            return true;
        }
        return false;
    }

    public void setmTitle() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mSongPath);
        mTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    public String getmTitle() {
        if (mTitle == null)
            setmTitle();
        return mTitle;
    }

    private void setArtist() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mSongPath);
        mArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    public String getArtist() {
        if (mArtist == null) {
            setArtist();
        }
        return mArtist;
    }

    public void setDuration() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mSongPath);
        mDuration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public int getDuration() {
        if (mDuration == 0) {
            setDuration();
        }
        return mDuration;
    }

    public String getFormatDuration() {
        long time = getDuration();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        double minute = calendar.get(Calendar.MINUTE);
        double second = calendar.get(Calendar.SECOND);

        DecimalFormat format = new DecimalFormat("00");
        return format.format(minute) + ":" + format.format(second);
    }

}
