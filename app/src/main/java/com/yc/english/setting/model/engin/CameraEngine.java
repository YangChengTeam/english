package com.yc.english.setting.model.engin;

import android.content.Context;
import android.util.Log;

import com.yc.english.group.constant.NetConstant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.BaseEngine;
import yc.com.blankj.utilcode.util.LogUtils;


/**
 * Created by wanglin  on 2017/12/11 16:19.
 */

public class CameraEngine extends BaseEngine {

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


    /**
     * 上传图片，音频，文档
     *
     * @param
     * @param file
     * @return
     */
    public Observable<String> uploadFile(final File file) {
        return Observable.just(file).map(new Func1<File, String>() {

            @Override
            public String call(File file) {
                String result = null;
                String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
                String PREFIX = "--", LINE_END = "\r\n";
                String CONTENT_TYPE = "image/jpeg";   //内容类型

                try {
                    URL url = new URL(NetConstant.baidu_download_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10 * 1000);
                    conn.setConnectTimeout(10 * 1000);
                    conn.setUseCaches(false);  //不允许使用缓存
                    conn.setRequestMethod("POST");  //请求方式
                    conn.setRequestProperty("Charset", "UTF-8");  //设置编码
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";Content-Length=" + conn.getContentLength());
//                    conn.setDoInput(true);  //允许输入流
//                    conn.setDoOutput(true); //允许输出流
                    if (file != null) {
                        /**
                         * 当文件不为空，把文件包装并且上传
                         */
                        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                        StringBuffer sb = new StringBuffer();
                        sb.append(PREFIX);
                        sb.append(BOUNDARY);
                        sb.append(LINE_END);
                        /**
                         * 这里重点注意：
                         * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                         * filename是文件的名字，包含后缀名的   比如:abc.png
                         */

                        sb.append("Content-Disposition: form-data; name=\"upload\"; filename=\"image.jpg\"" + LINE_END);
                        sb.append("Content-Type: image/jpeg;");
                        sb.append(LINE_END);
                        dos.write(sb.toString().getBytes());
                        InputStream is = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = is.read(bytes)) != -1) {
                            dos.write(bytes, 0, len);
                        }
                        is.close();
                        dos.write(LINE_END.getBytes());
                        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                        dos.write(end_data);
                        dos.flush();
                        /**
                         * 获取响应码  200=成功
                         * 当响应成功，获取响应的流
                         */
                        int res = conn.getResponseCode();
                        Log.e("TAG", "response code:" + res);
//                if(res==200)
//                {
                        Log.e("TAG", "request success");
                        InputStream input = conn.getInputStream();
                        StringBuffer sb1 = new StringBuffer();
                        int ss;
                        while ((ss = input.read()) != -1) {
                            sb1.append((char) ss);
                        }
                        result = sb1.toString();
                        Log.e("TAG", "result : " + result);
//                }
//                else{
//                    Log.e(TAG, "request error");
//                }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }
}
