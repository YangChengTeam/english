package com.yc.soundmark.pay;

import android.util.Log;

import com.kk.utils.security.Md5;

import org.apache.http.params.CoreConnectionPNames;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by wanglin  on 2018/11/26 15:44.
 */
public class HttpUtils {
    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, String params, String encode) {

        byte[] data = params.getBytes();//获得请求体
        try {

            //String urlPath = "http://192.168.1.9:80/JJKSms/RecSms.php";
            URL url = new URL(strUrlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return "-1";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {

            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    //统一接口地址
    private static final String REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String APPID = "wx39c1238171bf9430";
    private static final String MCH_ID = "1517963871";//
    private static final String API_KEY = "23728fcb7447f3ba8591029a8bda52ab";
    private static final String WECHAT_NOTIFY_URL = "http://en.qqtn.com/api/notify/alipay";

    /**
     * Summary
     * step 1  加密单号等信息   调用统一下单接口
     * step 2  加密数据回发给app 发起支付
     * author bxh
     */

    public static void step1() {
//        init(wechatParamMap(APPID));
        // String respData=requestHttp(REQUEST_URL, init(wechatParamMap(APPID)));
        // Map<String,String> appParamMap = new HashMap<String,String>();
        //parseXml2Map(respData,appParamMap);
        //step2(appParamMap);
    }

    public static void step2(Map<String, String> appParamMap) {
        //init(appParamMap(APPID));
        init(appParamMap);
    }

    public static String init(Map<String, String> wechatParams) {
        //step 1.
        //Map<String, String> wechatParams = wechatParamMap(APPID);

        //step 2.
        //Map<String, String> wechatParams  = appParamMap(APPID);

        String sign = getSign(wechatParams);
        wechatParams.put("sign", sign);
        String orderParam = buildXmlOrderParam(wechatParams);
        Log.e("bxh", "--" + sign);


        return orderParam;
    }

    private static String buildXmlOrderParam(Map<String, String> param) {
        StringBuffer xmlStr = new StringBuffer("<xml>");
        for (String key : param.keySet()) {
            xmlStr.append("<").append(key).append(">");
            xmlStr.append(param.get(key));
            xmlStr.append("</").append(key).append(">\n\r");
        }
        xmlStr.append("</xml>");
        Log.i("bxh", "->" + xmlStr.toString());
        return xmlStr.toString();
    }

    public static Map<String, String> wechatParamMap(String appid,String name,String money) {
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("appid", appid);
        //商户号
        keyValues.put("mch_id", MCH_ID);
        //随机字符串
        keyValues.put("nonce_str", "E10ADC3949BA59ABBE56E057F20F883E");
        //签名
        //keyValues.put("sign", "");
        //商品描述
        keyValues.put("body", name);
        //商户订单号
        keyValues.put("out_trade_no", getOutTradeNo());
        //总金额
        keyValues.put("total_fee", "1");
        //终端IP
        keyValues.put("spbill_create_ip", "139.159.220.125");
        //通知地址
        keyValues.put("notify_url", WECHAT_NOTIFY_URL);
        //交易类型
        keyValues.put("trade_type", "APP");


        return keyValues;
    }


    /**
     *
     private static Map<String, String> appParamMap(String app_id) {
     Map<String, String> keyValues = new HashMap<String, String>();
     keyValues.put("appid", app_id);
     keyValues.put("partnerid", "");
     keyValues.put("prepayid", "");
     keyValues.put("package", "Sign=WXPay");
     keyValues.put("noncestr", SignUtils.signMD5("123456"));
     keyValues.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
     return keyValues;
     } */

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    private static String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));
        authInfo.append("&key=").append(API_KEY);
        String oriSign = Md5.md5(authInfo.toString());
        Log.i("bxh", "-> " + authInfo);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedSign.toUpperCase();
    }


}
