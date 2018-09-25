package com.yc.english.speak.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yc.english.speak.contract.ListenPlayContract;
import com.yc.english.speak.model.bean.ListenEnglishBean;
import com.yc.english.speak.service.MusicPlayService;

/**
 * 只负责处理页面和service之间交互调度
 * controller
 */


public class LyricViewPresenter implements ListenPlayContract.Presenter {


    private ListenPlayContract.View mMainView;

    private Activity mContext;

    private Intent service;
    private MusicPlayService.LocalBinder binder;


    public LyricViewPresenter(ListenPlayContract.View mainView, Activity context) {
        mMainView = mainView;
        mContext = context;
        mainView.setPresenter(this);
    }


    /**
     * 开启服务
     */
    public void startService() {

        service = new Intent(mContext, MusicPlayService.class);
        mContext.bindService(service, connection, Context.BIND_AUTO_CREATE);

    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicPlayService.LocalBinder) service;
            binder.init(mMainView);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 关闭服务
     */
    public void stopService() {
        if (null != service) {
            mContext.unbindService(connection);
            mContext.stopService(service);
        }
    }


    @Override
    public void start() {

    }

    @Override
    public void pause() {
        if (isServiceConnected()) {
            binder.pause();
        } else {
            throw new NullPointerException("暂停之前需先调用bind方法");
        }


    }

    @Override
    public void play() {
        if (isServiceConnected()) {
            binder.play();
        } else {
            throw new NullPointerException("播放之前需先调用bind方法");
        }
    }

    @Override
    public void destroy() {
        mMainView = null;
        binder.destroy();
        stopService();
    }


    @Override
    public void onBtnPlayPausePressed() {
        if (isServiceConnected()) {
            binder.onBtnPlayPausePressed();
        } else {
            throw new NullPointerException("暂停或播放之前需先调用bind方法");
        }

    }

    @Override
    public void onProgressChanged(int progress) {

        if (isServiceConnected()) {
            binder.onProgressChanged(progress);
        } else {
            throw new NullPointerException("拖动之前需先调用bind方法");
        }
    }


    @Override
    public void prev() {
        if (isServiceConnected()) {
            binder.pre();
        } else {
            throw new NullPointerException("开始上一首之前需先调用bind方法");
        }

    }

    @Override
    public void next() {
        if (isServiceConnected()) {
            binder.next();
        } else {
            throw new RuntimeException("开始下一首之前需先调用bind方法");
        }
    }


    private boolean isServiceConnected() {
        return null != binder;
    }


    //下载MP3文件
    public void downAudioFile(ListenEnglishBean listenEnglishBean) {
        if (isServiceConnected()) {
            binder.downAudioFile(listenEnglishBean);
        } else {
            throw new NullPointerException("下载之前需先调用bind方法");
        }
    }

}
