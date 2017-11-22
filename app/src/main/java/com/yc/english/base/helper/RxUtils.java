package com.yc.english.base.helper;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.subutil.util.ThreadPoolUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.kk.utils.PathUtils;
import com.kk.utils.UIUitls;
import com.umeng.socialize.sina.helper.MD5;
import com.yc.english.main.model.domain.IndexInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.xml.parsers.SAXParser;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/8/14.
 */

public class RxUtils {
    public static Observable<File> getFile(final Context context, final String urlStr) {
        return Observable.just(urlStr).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !EmptyUtils.isEmpty(urlStr);
            }
        }).map(new Func1<String, File>() {
            @Override
            public File call(String s) {
                File file = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();


                    String name = MD5.hexdigest(urlStr);
                    if (urlStr.lastIndexOf('/') != -1) {
                        name = urlStr.substring(urlStr.lastIndexOf('/'));
                    }

                    file = new File(PathUtils.makeDir(context, "files"), name);
                    FileOutputStream fileOutput = new FileOutputStream(file);

                    if (file.exists() && file.length() == urlConnection.getContentLength()) {
                        return file;
                    }

                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                    }

                    fileOutput.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }
        }).subscribeOn(Schedulers.io());
    }




}
