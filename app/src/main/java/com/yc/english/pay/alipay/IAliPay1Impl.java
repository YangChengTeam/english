package com.yc.english.pay.alipay;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import com.kk.securityhttp.domain.GoagalInfo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by zhangkai on 2017/3/18.
 */

public class IAliPay1Impl extends IPayImpl {
    //默认参数配置
    private static String APPID = "2017110409723362"; //申请接入支付宝APPID
    private static String PARTNERID = "2088821411331682"; //商户UID
    private static String EMAIL = "3258186647@qq.com"; //邮箱
    private static String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCP6cHHXB3x6W9UwOz07KlmU/zSksiwF9TOp5Jv3HftnJvZTJSHlOsrnvAbVFtzBK0lNEfNV4TXmS8Qonq62MuHaTJ3VC1p6FQsFnUltE8SRfpmz6iBObH7wCMaoY89Fi511tun86xE0u9HhGli231N99grbuuDrR9Qt2FmrLE04bQh/wcGS3AnIwpPttxn760Fbam89kvj8GMLDHvWmn5QLn//dIKBqm01Qo4PJLbcWaZIEDYQSH5ls1EqgzaMYirC7Qj7IgWyCpRbWhVGD+HXBl1IXyw+WuhyOM2G+0WR1AMXShRI3AR+7QEv27RU5qhO+79YdsVi01OB4Cl9MS7DAgMBAAECggEAO+MCiHuE2o5Rjetar/Fr7PE2XEpIyT6hh/2jqnkMTwzErgB4LpOB6X0SXc0U4SApDTpcRs8MsMtGEp4KhIaC666TGaUl4NSVcmNGDJKj9O657N6tOFlR/3lNIl/ow3rfipoGfjWgkmNUv2YSlNjRpAhnJGvcBcedKHGnTpq+g0ppXChytOAwSXExozBl4oYlke64Y+cT5/OQEy+Rj4BIaJnKyCv3NOiPRXNQS9EqCRAh1o8pJ5jEDV4t6jeYIMDZeqG7eBUMazQQP0P5EHiC01cKzT0BrvpqUaVnFAsrXqw3o9HgcoVcJfSq1LyY8d0J151Rvf0myiGQxMfaajZ/AQKBgQDGkR6EbyRsvn5/06bam/hEMiOmsKI5etwe7Khv61xf1J2+U9TG8ErC1BBAmGCCX3alHk4GuR73nNAz46rd40W9A02NB6S1LUwcNIqUyvqvJw6qgT3ZTh+512z5wY+eNJN/IuOJwSaZ3htoy7bEU/IOTPM0MyM8l3kjOsLYg3/wcwKBgQC5ic0SMqXNBkECvwdJL1rVtnzBKjip9kNs+3mN6uzKqwFK7R5somwqz7Zqm+zG0+bh3FNsB3fREk84iFoDkwYavEnksSzrlol+p1WLQLgMgLcPB6OMpjlK4KMxoHPYO+nfC4QDd6iprVoceIAgAQysIyiFe4vPnbDlf5STQ+DEcQKBgDEhx0NNnN2rZPGMFUUSQqPdJCUin4FJfR3JwQOwYPL1UPK/G27+FgGPJ1ZUXczkgh7pGLVhKOBr1LiCJM0yZxcVsiKrOX3671IrTf7zGoQsTdyyjfMu+XqqazSBSGAE8loK7/It8Lcx1eZgQaDihIo1UBgibx2W/UpSR9P69bUxAoGAW7Y1CxjEAitkOUJKDK/+u4Mf+a+wILtdKuLHfBIzCB8tXWcGUfabdzIDXoFCsimOh/iLt+udG1hslmo37GphaxfFgujdZnqb7mIyk8ni8DMzyZciDeUgjtWpdV91w94hxaIAmGIumejZkRczZh5+sBwU7J5cRr0Q8vB0dbLoyNECgYBRWkEcKdhMWM2DqqLtT9LdTnW/Ngrt50BQF2eX9yolBpro07pZjgRKDS+CdCUZZ3rkEAKAxkuyRT1qI741Vao8F8h80QX/O2uzwSD+tMCwKEQ4wy9SkBDFg4rQGcpfAGt/uZQ4BhLIe5ltm2Q1GwXi2VlYopjA7WdYg9TOlvVH4A==";
    private static String NOTIFY_URL = "http://u.xcjz8.com/api/index/notify_url/alipay"; //回调地址


    public IAliPay1Impl(Activity context) {
        super(context);
    }
    @Override
    public void pay(OrderInfo orderInfo, IPayCallback iPayCallback) {
        if (orderInfo.getPayInfo() != null) {
            APPID = get(orderInfo.getPayInfo().getAppid(), APPID);
            PARTNERID = get(orderInfo.getPayInfo().getPartnerid(), PARTNERID);
            EMAIL = get(orderInfo.getPayInfo().getEmail(), EMAIL);
            PRIVATE_KEY = get(orderInfo.getPayInfo().getPrivatekey(), PRIVATE_KEY);
            NOTIFY_URL = get(orderInfo.getPayInfo().getNotify_url(), NOTIFY_URL);
        }
        alipay(orderInfo, orderInfo.getMoney() + "", orderInfo.getOrder_sn(), orderInfo.getName(), orderInfo.getName(),
                iPayCallback);
    }

    /**
     * 支付宝支付
     *
     * @param money
     * @param ordeID
     * @param theOrderName
     * @param theOrderDetail
     */
    private void alipay(OrderInfo orderInfo, String money, String ordeID, String theOrderName, String theOrderDetail,
                        IPayCallback
                                iPayCallback) {
        String privatekey = GoagalInfo.get().getPublicKey(PRIVATE_KEY);
        Map<String, String> params = buildOrderParamMap(money, theOrderName, theOrderDetail, ordeID);
        String orderParam = buildOrderParam(params);//对订单地址排序
        String sign = getSign(params, privatekey);
        try {
            if (!TextUtils.isEmpty(sign)) {
                // 完整的符合支付宝参数规范的订单信息
                final String payInfo = orderParam + "&" + sign;
                //调用新线程支付
                new Thread(new AlipayRunnable(orderInfo, payInfo, iPayCallback)).start();
            } else {
                new IllegalThreadStateException("签名错误");
            }
        } catch (Exception e) {
        }
    }


    /**
     * 支付宝支付
     */

    private class AlipayRunnable implements Runnable {
        private OrderInfo orderInfo;
        private String mPayInfo;
        private IPayCallback iPayCallback;

        public AlipayRunnable(OrderInfo orderInfo, String payInfo, IPayCallback iPayCallback) {
            this.orderInfo = orderInfo;
            this.mPayInfo = payInfo;
            this.iPayCallback = iPayCallback;
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(mContext);

            // 调用支付接口，获取支付结果
            Map<String, String> result = alipay.payV2(mPayInfo, false);
            PayResult payResult = new PayResult(result);
            final String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();
            if (TextUtils.equals(resultStatus, "9000")) {
                orderInfo.setMessage("支付成功");
                iPayCallback.onSuccess(orderInfo);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("支付成功");
                    }
                });

                checkOrder(orderInfo);
            } else if (TextUtils.equals(resultStatus, "6001")) {
                orderInfo.setMessage("支付取消");
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("支付取消");
                    }
                });

                iPayCallback.onFailure(orderInfo);
            } else {

                orderInfo.setMessage("支付失败");
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("支付失败");
                    }
                });

                iPayCallback.onFailure(orderInfo);
            }
        }
    }

    private void checkOrder(final OrderInfo orderInfo) {


    }

    /**
     * 对订单签名
     *
     * @param map
     * @param rsaKey
     * @return
     */
    public static String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<>(map.keySet());
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

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey, true);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 生成订单信息
     *
     * @param money
     * @param theOrderName
     * @param theOrderDetail
     * @param ordeID
     * @return
     */

    public static Map<String, String> buildOrderParamMap(String money, String theOrderName, String theOrderDetail, String ordeID) {

        Map<String, String> keyValues = new HashMap<>();
        keyValues.put("app_id", APPID);
        keyValues.put("partner", PARTNERID);
        keyValues.put("seller_id", EMAIL);
        keyValues.put("notify_url", NOTIFY_URL);
        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + money + "\",\"subject\":\"" + theOrderName + "\",\"body\":\"" + theOrderDetail + "\",\"out_trade_no\":\"" + ordeID + "\"}");
        keyValues.put("charset", "utf-8");
        keyValues.put("method", "alipay.trade.app.pay");
        keyValues.put("sign_type", "RSA2" );
        keyValues.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        keyValues.put("payment_type", "1");
        keyValues.put("version","1.0");

        return keyValues;
    }


    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));
        return sb.toString();
    }

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


    private class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }
}
