package com.kk.securityhttp.domain;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.kk.utils.PathUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by zhangkai on 16/9/19.
 */
public class GoagalInfo {

//    @JSONField(name = "pub_key")
//    public String publicKey = "-----BEGIN PUBLIC KEY-----" +
//            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApcW/nMMsLOo8jg6PoY0F"
//            + "YBOlFdST4sqRx8cMOYIbdXDDdjsxIMI2lN7skoielHELQB+LzaI8kURg2bHfLxOr"
//            + "wGJAVKxEt7+GwNe1ZxEeSL7SxFCgvVYI088W/ElSXHiq57p9SFpVccwY+JmjyL/U"
//            + "gX1OSoemNAKkys64NhEMm9C8Lrs/+N4nilzW2A6hSlgVtjbjMry8M6lSjB63xvQg"
//            + "wu+u6GfWpx0/TM69gg5o0ytBxl6BEtcFXujeQoR6JY6MrPggLy4/FLIxxivHTX6s"
//            + "Ae1W0/Go7bORbhELNfUP0OsYbDD3d4AF/E0rV9J1Nj/wXvTxn7mQQD9n8S+zrKP6"
//            + "CJ4jirfEzlFxda1Wtk0Orxy+mMlT4WPaj/aYgHwZ/QeId00zoxwrJoCbxSjqhTjg"
//            + "rVHdsoX5J+pspEENB9CHDu1AKGRwXn2525HqUVAwZoTL5q2Al0LoKA1NeEwEE8So"
//            + "W9Mr/RdvQg9W674wc9hKNBZFJx6scei7Pq4JB62jOiEi7nCmIYNQUEMI1MtSsLJl"
//            + "Un6E2pIu4pohlUm/vGghBrgZvT9fZhtRtBRCBCht+mrkG7IqhLVq3ZAcO9UQnGrW"
//            + "bMQWqbDut1KV8Vh4B5tiaOoiswTZB5Nk4pMF2cCXWPDvNNjCRXkr0NYSK6vasZFp"
//            + "WYe9pakq6ocysaPXT6+Gbi0CAwEAAQ=="
//            + "-----END PUBLIC KEY-----";


    @JSONField(name = "pub_key")
    public String publicKey = "-----BEGIN PUBLIC KEY-----" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAy/M1AxLZjZOyJToExpn1"
            + "hudAWySRzS+aGwNVdX9QX6vK38O7WUA7h/bYqBu+6tTRC6RnL9BksMrIf5m6D3rt"
            + "faYYmxfe/FI4ZD5ybIhFuRUi95e/J2vQVElsSNqSz7ewXquZpKZAqlzH4hGgOqmO"
            + "THlrwiQwX66bS7x7kDmvxMd5ZRGhTvz62kpKb/orcnWQ1KElNc/bIzTtv3jsrMgH"
            + "FVdFZfev91ew4Kf1YJbqGBGKslBsIoGsgTxI94T6d6XEFxSzdvrRwKhOobXIaOhZ"
            + "o3GBCZIA/1ZOwLK6RyrWdprz+60xifcYIkILdZ7yPazSfHCVHFY6o/fQjK4dxQDW"
            + "Gw0fxN9QX+v3+48nW7QIBx4KNYNIW/eetGhXpOwV4PjNt15fcwJkKsx2W3VQuh93"
            + "jdYB4xMyDUnRwb9np/QR1rmbzSm5ySGkmD7NAj03V+O82Nx4uxsdg2H7EQdVcY7e"
            + "6dEdpLYp2p+VkDd9t/5y1D8KtC35yDwraaxXveTMfLk8SeI/Yz4QaX6dolZEuUWa"
            + "tLaye2uA0w25Ee35irmaNDLhDr804B7U7M4kkbwY7ijvvhnfb1NwFY5lw/2/dZqJ"
            + "x2gH3lXVs6AM4MTDLs4BfCXiq2WO15H8/4Gg/2iEk8QhOWZvWe/vE8/ciB2ABMEM"
            + "vvSb829OOi6npw9i9pJ8CwMCAwEAAQ=="
            + "-----END PUBLIC KEY-----";

    public ChannelInfo channelInfo = null;
    public AppUtils.AppInfo appInfo = null;

    public String uuid = "";
    public String channel = "default";

    public String configPath = "";


    private static GoagalInfo goagalInfo = new GoagalInfo();

    public static GoagalInfo get() {
        return goagalInfo;
    }

    public void init(Context context) {

        configPath = PathUtils.makeConfigDir(context);

        String result1 = null;
        String result2 = null;
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/gamechannel.json");
            InputStream in1 = zf.getInputStream(ze1);
            result1 = FileIOUtils.readFile2String(in1);
            LogUtils.i("渠道->" + result1);

            ZipEntry ze2 = zf.getEntry("META-INF/rsa_public_key.pem");
            InputStream in2 = zf.getInputStream(ze2);
            result2 = FileIOUtils.readFile2String(in2);
            LogUtils.i("公钥->" + result2);
        } catch (Exception e) {
            LogUtils.w("apk中gamechannel或rsa_public_key文件不存在");
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }

        String name = gamechannelFilename;
        String path = configPath + "/" + name;
        if (result1 != null) {
            FileIOUtils.writeFileFromString(path, result1);
        } else {
            result1 = FileIOUtils.readFile2String(path);
        }

        if (result1 != null) {
            channel = result1;
        }

        name = rasPublickeylFilename;
        path = configPath + "/" + name;
        if (result2 != null) {
            publicKey = getPublicKey(result2);
            FileIOUtils.writeFileFromString(path, result2);
        } else {
            result2 = FileIOUtils.readFile2String(path);
            if (result2 != null) {
                publicKey = getPublicKey(result2);
            }
        }

        channelInfo = getChannelInfo();  //渠道信息
        uuid = getUid(context);  //唯一标识
        appInfo = AppUtils.getAppInfo();  //app信息

    }

    private String rasPublickeylFilename = "rsa_public_key.pem";
    private String gamechannelFilename = "gamechannel.json";

    public GoagalInfo setRasPublickeylFilename(String rasPublickeylFilename) {
        this.rasPublickeylFilename = rasPublickeylFilename;
        return this;
    }

    public GoagalInfo setGamechannelFilename(String gamechannelFilename) {
        this.gamechannelFilename = gamechannelFilename;
        return this;
    }

    private ChannelInfo getChannelInfo() {
        try {
            ChannelInfo channelInfo = JSON.parseObject(channel, ChannelInfo.class);
            return channelInfo;
        } catch (Exception e) {
            LogUtils.w("渠道信息解析错误->" + e.getMessage());
        }
        return null;
    }


    public String getPublicKey() {
        publicKey = getPublicKey(publicKey);
        return publicKey;
    }

    public String getPublicKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r", "")
                .replace("\n", "");
    }

    public String getUid(Context context) {
        String uid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (StringUtils.isEmpty(uid) || uid.equals("02:00:00:00:00:00")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            uid = wInfo.getMacAddress();
        }

        if (StringUtils.isEmpty(uid)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uid = telephonyManager.getDeviceId();
        }

        if (uid == null) {
            uid = "";
        }

        return uid;
    }
}
