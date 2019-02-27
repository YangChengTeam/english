package com.yc.soundmark.study.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2018/11/5 10:46.
 */
public class AVMediaManager {

    private static AVMediaManager instance;
    private List<PpAudioManager> ppAudioManagers;

    private AVMediaManager() {
        ppAudioManagers = new ArrayList<>();
    }

    public static AVMediaManager getInstance() {
        synchronized (AVMediaManager.class) {
            if (instance == null) {
                synchronized (AVMediaManager.class) {
                    instance = new AVMediaManager();
                }

            }
        }
        return instance;

    }


    public void addAudioManager(PpAudioManager ppAudioManager) {
        ppAudioManagers.add(ppAudioManager);
    }


    public void releaseAudioManager() {
        for (PpAudioManager ppAudioManager : ppAudioManagers) {
            ppAudioManager.destroy();
        }
    }
}
