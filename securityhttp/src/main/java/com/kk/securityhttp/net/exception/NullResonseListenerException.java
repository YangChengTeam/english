package com.kk.securityhttp.net.exception;

/**
 * Created by zhangkai on 16/9/9.
 */
public class NullResonseListenerException extends RuntimeException {
    public NullResonseListenerException(){
        super("ResonseListener is a null pointer!");
    }
}
