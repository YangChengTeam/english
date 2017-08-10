package com.kk.securityhttp.net.utils;

import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.utils.EncryptUtils;


import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhangkai on 16/9/9.
 */
public final class OKHttpUtil {

    //< 获取OkHttpClient.Builder
    public static OkHttpClient.Builder getHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        return builder;
    }

    //< 设置响应返回信息
    public static Response setResponse(int code, String body) {
        Response response = new Response();
        response.code = code;
        response.body = body;
        return response;
    }


    //< 添加http头信息
    public static void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                builder.addHeader(pair.getKey() + "", pair.getValue() + "");
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
    }

    //< 根据Map构建url信息
    public static String buildUrl(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            boolean hasParams = false;
            if (url.contains("?")) {
                hasParams = true;
            }
            Iterator it = params.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (i == 0 && !hasParams) {
                    urlBuilder.append("?" + pair.getKey() + "=" + pair.getValue());
                } else {
                    urlBuilder.append("&" + pair.getKey() + "=" + pair.getValue());
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        return urlBuilder.toString();
    }

    //< 获取Request.Builder
    public static Request.Builder getRequestBuilder(String url) {
        LogUtils.i("客户端请求url->" + url);
        Request.Builder builder = new Request.Builder()
                .tag(url)
                .url(url);
        return builder;
    }


    //< 设置请求参数FormBody.Builder
    public static FormBody.Builder setBuilder(Map<String, String> params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.add(key, value);
            }
        }
        return builder;
    }

    //< 设置请求参数MultipartBody.Builder
    public static MultipartBody.Builder setBuilder(UpFileInfo upFileInfo, Map<String, String> params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addFormDataPart(key, value);
            }
        }
        if(upFileInfo.file != null) {
            builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                            ("multipart/form-data"),
                    upFileInfo.file));
        }else if(upFileInfo.buffer != null){
            builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                            ("multipart/form-data"),
                    upFileInfo.buffer));
        }
        return builder;
    }


    //< 获取RequestBody
    public static RequestBody getRequestBody(Map<String, String> params, boolean isrsa, boolean
            isEncryptResponse) {
        byte[] data = OKHttpUtil.encodeParams(params, isrsa, isEncryptResponse);
        RequestBody requestBody = RequestBody.create(HttpConfig.MEDIA_TYPE, data);
        return requestBody;
    }

    //< 获取RequestBody
    public static RequestBody getRequestBody(byte[] data) {
        RequestBody requestBody = RequestBody.create(HttpConfig.MEDIA_TYPE, data);
        return requestBody;
    }

    //< 获取Request 0
    public static Request getRequest(String url, byte[] data) {
        Request.Builder builder = OKHttpUtil.getRequestBuilder(url);
        Request request = builder.post(OKHttpUtil.getRequestBody(data)).build();
        return request;
    }

    //< 获取Request 1
    public static Request getRequest(String url, Map<String, String> params, Map<String, String> headers, boolean isrsa,
                                     boolean iszip, boolean isEncryptResponse) {
        Request.Builder builder = OKHttpUtil.getRequestBuilder(url);
        OKHttpUtil.addHeaders(builder, headers);
        Request request;
        if (isrsa) {
            iszip = true;
        }
        if (iszip) {
            request = builder.post(OKHttpUtil.getRequestBody(params, isrsa, iszip)).build();
        } else {
            request = builder.post(OKHttpUtil.setBuilder(params, isEncryptResponse).build()).build();
        }
        return request;
    }

    //< 获取Request 2
    public static Request getRequest(String url, Map<String, String> params, Map<String, String> headers, UpFileInfo
            upFileInfo, boolean isEncryptResponse) {
        Request.Builder builder = OKHttpUtil.getRequestBuilder(url);
        OKHttpUtil.addHeaders(builder, headers);
        MultipartBody requestBody = OKHttpUtil.setBuilder(upFileInfo, params, isEncryptResponse).build();
        return builder.post(requestBody).build();
    }

    //< 获取Request 3
    public static Request getRequest(String url,  Map<String, String> headers, MediaType type, String body) {
        RequestBody requestBody = RequestBody.create(type, body);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        OKHttpUtil.addHeaders(builder, headers);
        return builder.build();
    }

    //<  加密正文
    public static byte[] encodeParams(Map params, boolean isrsa, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtils.i("客户端请求数据->" + jsonStr);
        if (isrsa) {
            LogUtils.i("当前公钥->" + GoagalInfo.get().getPublicKey());
            jsonStr = EncryptUtils.rsa(GoagalInfo.get().getPublicKey(), jsonStr);
        }
        return EncryptUtils.compress(jsonStr);
    }

    //< 不加密参数 正常请求正文
    public static Map<String, String> encodeParams(Map params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap();
        }
        setDefaultParams(params, isEncryptResponse);
        return params;
    }

    ///< 解密返回值
    public static String decodeBody(InputStream in) {
        return EncryptUtils.decode(EncryptUtils.unzip(in));
    }


    //设置默认参数
    private static void setDefaultParams(Map<String, String> params, boolean encryptResponse) {
        if (encryptResponse) {
            params.put("encrypt_response", "true");
        }

        if (defaultParams != null) {
            params.putAll(defaultParams);
        }
    }




    public static void setDefaultParams(Map<String, String> params) {
        OKHttpUtil.defaultParams = params;
    }

    private static Map<String, String> defaultParams;

}
