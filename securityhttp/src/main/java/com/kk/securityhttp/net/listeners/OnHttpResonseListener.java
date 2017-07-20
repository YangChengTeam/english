package com.kk.securityhttp.net.listeners;


import com.kk.securityhttp.net.entry.Response;

/**
 * Created by zhangkai on 16/8/30.
 */
public interface OnHttpResonseListener {
    void onSuccess(Response response);
    void onFailure(Response response);
}
