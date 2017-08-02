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


    @JSONField(name = "public_key")
    public String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAunGsieRPZ+jSZaK0hFjC\n" +
            "EwbeoIv6D7sQR8IVCJKzkGklBEAw8hENPz/hyBcvaBjGt6ladFvNMWGdMRMr2QwM\n" +
            "EJN9fE32Cij5WgEvAgEHEAdb5pFoQ1gRpyRT5bu/xPWW/xVef9yB/mpXJGlqD5kq\n" +
            "6rHkDrsaW+dfr3jhYlvc64FwFVE2KKNc2BKTYc84gBza0yxjZsK1PTvjLovdxpFD\n" +
            "l7TXLS7zN6B+KQsrd6z2IZjMbuA6R0PaFhNL4IAYoBLIci51AdoRg6T6jLHU6SI2\n" +
            "evCm3G43Qihkj8flOW9wnoa7dHayWjOciTnMjzypWgGEr4Da7WVb2pcPHWU9Cqux\n" +
            "o5aPe+N0gZzd8pV8/VosCYsDwSkKbR12BK/eXmHAtyuRwaeDqY/ttoSES3oePtWV\n" +
            "jf2CdMfAk1Q8KWNzt3P5+5OMsgI532WrHn4RCXB0jwUTN7VGTp+FZQfavN3vsy+X\n" +
            "txd7DgXJUjs2B2DVPC3nu5+fj21b0C56Yksuq02bbb5LeIIqn2h6pYPYTDgO9olC\n" +
            "w34vJGFeBEZJLZKjbR0edR6kPAjjZghCL8q5auJF0B2sJIOE3Yc7AB8BAS5Yxca/\n" +
            "TVHjxxLAN8sWy7le8vkWSNPwxmm9aJtjbvXuSYzohQvB4M95IVFaM4AKxf9HU0x8\n" +
            "E5MXnkjxSEogMwjH0r/Fqm8CAwEAAQ==\n" +
            "-----END PUBLIC KEY-----";


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
