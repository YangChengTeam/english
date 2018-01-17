package com.yc.junior.english.base.helper;

import android.content.Context;

import com.blankj.utilcode.util.EmptyUtils;
import com.kk.utils.PathUtils;
import com.umeng.socialize.sina.helper.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/8/14.
 */

public class RxUtils {
    public static Observable<File> getFile(final Context context, final String urlStr, final Callback callback) {
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
                        name += urlStr.substring(urlStr.lastIndexOf('/') + 1);
                    }

                    file = new File(PathUtils.makeDir(context, "files"), name);
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    long length = urlConnection.getContentLength();
                    if (file.exists() && file.length() == length) {
                        return file;
                    }

                    InputStream inputStream = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    long tmpLength = 0;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        tmpLength += bufferLength;
                        if (callback != null) {
                            callback.process(tmpLength / (float) length);
                        }
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

    public static Observable<File> getFile(final Context context, final String urlStr) {
        return getFile(context, urlStr, null);
    }

    public interface Callback {
        void process(float precent);
    }


}
