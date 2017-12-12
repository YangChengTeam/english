package com.yc.english.setting.model.engin;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by wanglin  on 2017/12/11 16:19.
 */

public class CameraEngine extends BaseEngin {

    public CameraEngine(Context context) {
        super(context);
    }

    public Observable<String> pictureDiscern(String filePath) {

        return Observable.just(filePath).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                InputStream inputStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(NetConstant.baidu_url + s);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(10 * 1000);
                    urlConnection.setReadTimeout(10 * 1000);

                    if (urlConnection.getResponseCode() == 200) {

                        StringBuilder sb = new StringBuilder();

                        inputStream = urlConnection.getInputStream();

                        reader = new BufferedReader(new InputStreamReader(inputStream));

                        while (reader.readLine() != null) {
                            sb.append(reader.readLine()).append("\n");
                        }

                        LogUtils.e(sb.toString());
                        return sb.toString();

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
