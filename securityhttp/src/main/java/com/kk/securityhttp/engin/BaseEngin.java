package com.kk.securityhttp.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.listeners.Callback;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.impls.OKHttpRequest;
import com.kk.utils.PathUtils;
import com.kk.utils.UIUitls;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/4/28.
 */

public abstract class BaseEngin<T> {

    private Context mContext;

    public BaseEngin(Context context) {
        this.mContext = context;
    }

    //< 同步请求get 1
    private T get(Type type, Map<String, String> params, Map<String, String> headers, boolean isEncryptResponse) {
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().get(getUrl(), params, headers, isEncryptResponse);
            resultInfo = getResultInfo(response.body, type);

        } catch (Exception e) {
            LogUtils.w("get异常->" + e);
        }
        return resultInfo;
    }

    //< 同步请求rxjava get 3
    public Observable<T> rxget(final Type type, final Map<String, String> params, final Map<String, String> headers, final boolean
            isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                return get(type, params, headers, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtils.w(throwable.getMessage());
                return null;
            }
        });
    }

    //< 同步请求rxjava get 2
    public Observable<T> rxget(final Type type, final Map<String, String> params, final boolean
            isEncryptResponse) {
        return rxget(type, params, null, isEncryptResponse);
    }

    //< 同步请求rxjava get 1
    public Observable<T> rxget(final Type type, final boolean isEncryptResponse) {
        return rxget(type, null, isEncryptResponse);
    }

    //< 同步请求post 1
    private T post(Type type, Map<String, String> params, Map<String, String> headers, boolean
            isrsa, boolean iszip, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().post(getUrl(), params, headers, isrsa, iszip, isEncryptResponse);
            resultInfo = getResultInfo(response.body, type);
            if (isrsa && publicKeyError(resultInfo, response.body)) {
                return post(type, params, headers, isrsa, iszip, isEncryptResponse);
            }
        } catch (Exception e) {
            LogUtils.w("post异常->" + e);
        }
        return resultInfo;
    }

    public T upload(Type type, byte[] data,  boolean isEncryptResponse){
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().upload(getUrl(), data, isEncryptResponse);
            resultInfo = getResultInfo(response.body, type);
        } catch (Exception e) {
            LogUtils.w("post异常->" + e);
        }
        return resultInfo;
    }

    public Observable<T> rxupload(final Type type,final byte[] data,final boolean isEncryptResponse){
        return Observable.just("").map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                return upload(type, data, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtils.w(throwable.getMessage());
                return null;
            }
        });
    }

    //< 同步请求rxjava post 2
    public Observable<T> rxpost(final Type type, final Map<String, String>
            params, final boolean isrsa, final boolean iszip, final boolean isEncryptResponse) {
        return rxpost(type, params, null, isrsa, iszip, isEncryptResponse);
    }


    //< 同步请求rxjava post 1
    public Observable<T> rxpost(final Type type, final Map<String, String>
            params, final Map<String, String>
                                        headers, final boolean isrsa, final boolean iszip, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                return post(type, params, headers, isrsa, iszip, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtils.w(throwable.getMessage());
                return null;
            }
        });
    }

    //< 同步请求rxjava post string
    public Observable<String> rxpost(final Map<String, String> header, final MediaType type, final String
            body) {
        return Observable.just("").map(new Func1<Object, String>() {
            @Override
            public String call(Object o) {
                String data = "";
                try {
                    Response response = OKHttpRequest.getImpl().post(getUrl(), header, type, body);
                    if (response != null) {
                        data = response.body;
                    }
                } catch (Exception e) {
                    LogUtils.w("rxpost异常->" + e);
                }
                return data;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                LogUtils.w(throwable.getMessage());
                return null;
            }
        });
    }

    //< 同步请求rxjava post string no header
    public Observable<String> rxpost(final MediaType type, final String
            body) {
        return rxpost(null, type, body);
    }

    //< 同步请求rxjava post json
    public Observable<String> rxpost(final Map<String, String> header, final String
            json) {
        return rxpost(header, MediaType.parse("application/json; charset=utf-8"), json);
    }

    //< 同步请求rxjava post json no header
    public Observable<String> rxpost(final String
                                             json) {
        return rxpost(null, MediaType.parse("application/json; charset=utf-8"), json);
    }




    //< 同步请求uploadFile 1
    public T uploadFile(Type type, UpFileInfo
            upFileInfo, Map<String, String>
                                params, Map<String, String> headers, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().uploadFile(getUrl(), upFileInfo, params,
                    headers, isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, type);
        } catch (Exception e) {
            LogUtils.w("uploadFile->" + e);
        }
        return resultInfo;
    }

    //< 异步请求rxuploadFile 2
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo upFileInfo, final Map<String, String>
            params, final boolean isEncryptResponse) {
        return rxuploadFile(type, upFileInfo, params, null, isEncryptResponse);
    }

    //< 异步请求rxuploadFile 1
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo upFileInfo, final Map<String, String>
            params, final Map<String, String>
                                              headers, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return uploadFile(type, upFileInfo, params, headers, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers
                .mainThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtils.w(throwable.getMessage());
                return null;
            }
        });
    }

    private void success(final Callback callback, final T lastResultInfo) {
        UIUitls.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(lastResultInfo);
            }
        });
    }

    private void failure(final Callback callback, final Response response) {
        UIUitls.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(response);
            }
        });
    }

    private void aerror(Exception e, Callback callback, Response response) {
        LogUtils.w("异常->" + e);
        e.printStackTrace();
        if (response == null) {
            response = new Response();
            response.body = e.getMessage();
            response.code = HttpConfig.SERVICE_ERROR_CODE;
        }
        if (callback != null) {
            failure(callback, response);
        }
    }

    private void aerror(Exception e, Callback callback) {
        aerror(e, callback, null);
    }


    private T getResultInfo(String body, Type type) {
        if(type.toString().equals("java.lang.String")){
            return  (T)body;
        }

        T resultInfo;
        if (type != null) {
            resultInfo = JSON.parseObject(body, type);
        } else {
            resultInfo = JSON.parseObject(body, new TypeReference<T>() {
            }); //范型已被擦除 --！
        }
        return resultInfo;
    }

    public abstract String getUrl();


    private boolean publicKeyError(T resultInfo, String body) {
        if (resultInfo instanceof ResultInfo) {
            ResultInfo tmpResultInfo = (ResultInfo) resultInfo;
            if (resultInfo != null && tmpResultInfo.code == HttpConfig.PUBLICKEY_ERROR) {
                ResultInfo<GoagalInfo> resultInfoPE = JSON.parseObject(body, new
                        TypeReference<ResultInfo<GoagalInfo>>
                                (GoagalInfo.class) {
                        });
                if (resultInfoPE.data != null && resultInfoPE.data.getPublicKey() != null) {
                    GoagalInfo.get().publicKey = GoagalInfo.get().getPublicKey(resultInfoPE.data.getPublicKey());
                    LogUtils.w("公钥出错->" + GoagalInfo.get().publicKey);
                    String name = "rsa_public_key.pem";
                    FileIOUtils.writeFileFromString(PathUtils.makeConfigDir(mContext) + "/" +name, GoagalInfo.get()
                            .publicKey);
                    return true;
                }
            }
        }
        return false;
    }
}
