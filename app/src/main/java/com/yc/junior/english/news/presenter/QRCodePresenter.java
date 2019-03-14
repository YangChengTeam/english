package com.yc.junior.english.news.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yc.junior.english.news.contract.QRCodeContract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import yc.com.base.BaseEngine;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.UIUitls;


/**
 * Created by wanglin  on 2018/10/18 15:58.
 */
public class QRCodePresenter extends BasePresenter<BaseEngine, QRCodeContract.View> implements QRCodeContract.Presenter {
    public QRCodePresenter(Context context, QRCodeContract.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }


    public void getBitmap(final String imgpath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                ByteArrayOutputStream outputStream = null;
                try {
                    URL url = new URL(imgpath);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        inputStream = conn.getInputStream();
                        outputStream = new ByteArrayOutputStream();
                        byte buffer[] = new byte[1024];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        byte[] bu = outputStream.toByteArray();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bu, 0, bu.length);
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                mView.showBitmap(bitmap);
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
}
