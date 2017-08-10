package com.kk.securityhttp.engin;

import android.content.Context;

import com.kk.securityhttp.net.entry.UpFileInfo;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.MediaType;
import rx.Observable;

/**
 * Created by zhangkai on 2017/4/28.
 */

public class HttpCoreEngin<T> extends BaseEngin<T> {

    public static HttpCoreEngin httpCoreEngin = null;

    public HttpCoreEngin(Context context) {
        super(context);
    }

    public static HttpCoreEngin get(Context context) {
        return new HttpCoreEngin(context);
    }

    private String url;

    public Observable<T> rxget(String url, Type type, boolean isEncryptResponse) {
        return rxget(url, type, null, null, isEncryptResponse);
    }


    public Observable<T> rxget(String url, Type type, Map<String, String> params, boolean isEncryptResponse) {
        return rxget(url, type, params, null, isEncryptResponse);
    }

    public Observable<T> rxget(String url, Type type, Map<String, String> params, Map<String, String> headers,
                               boolean isEncryptResponse) {
        this.url = url;
        return super.rxget(type, params, headers, isEncryptResponse);
    }

    public T upload(String url, Type type, byte[] data, boolean isEncryptResponse) {
        this.url = url;
        return upload(type, data, isEncryptResponse);
    }


    public Observable<T> rxupload(String url, Type type, byte[] data, boolean isEncryptResponse) {
        this.url = url;
        return rxupload(type, data, isEncryptResponse);
    }


    public Observable<T> rxpost(String url, Type type, Map params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse) {
        return rxpost(url, type, params, null, isrsa, iszip, isEncryptResponse);
    }


    public Observable<T> rxpost(String url, Type type, Map<String, String> params, Map<String, String> headers,
                                boolean isrsa, boolean iszip, boolean
                                        isEncryptResponse) {
        this.url = url;
        return super.rxpost(type, params, headers, isrsa, iszip, isEncryptResponse);
    }


    //< 同步请求rxjava post string
    public Observable<String> rxpost(String url, final Map<String, String> header, final MediaType type, final String
            body) {
        this.url = url;
        return super.rxpost(header, type, body);
    }

    //< 同步请求rxjava post string no header
    public Observable<String> rxpost(String url, final MediaType type, final String
            body) {
        return rxpost(url, null, type, body);
    }

    //< 同步请求rxjava post json
    public Observable<String> rxpost(String url, final Map<String, String> header, final String
            json) {
        return rxpost(url, header, MediaType.parse("application/json; charset=utf-8"), json);
    }

    //< 同步请求rxjava post json no header
    public Observable<String> rxpost(String url, final String
            json) {
        return rxpost(url, null, MediaType.parse("application/json; charset=utf-8"), json);
    }


    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params,
                                      boolean
                                              isEncryptResponse) {
        return rxuploadFile(url, type, upFileInfo, params, null, isEncryptResponse);
    }


    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params,
                                      Map<String, String> headers, boolean
                                              isEncryptResponse) {
        this.url = url;
        return super.rxuploadFile(type, upFileInfo, params, headers, isEncryptResponse);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
