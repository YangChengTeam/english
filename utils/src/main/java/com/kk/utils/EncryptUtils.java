package com.kk.utils;


import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhangkai on 16/9/19.
 */
public class EncryptUtils {

    ///< rsa 分段加密
    public static String rsa(String publickey, String jsonStr) {
        String key = publickey;
        LogUtils.i("publickey->" + com.blankj.utilcode.util.EncryptUtils.encryptMD5(key.getBytes()));
        String result = null;
        String[] strs = sectionStr(jsonStr);
        if (strs == null) {
            result = RsaUtils.encrypt2(jsonStr, key);
            LogUtils.i("客户端请求加密数据->" + result);
        } else {
            try {
                List<byte[]> bytes = new ArrayList<>();
                int len = 0;
                for (int i = 0; i < strs.length; i++) {
                    LogUtils.i(i + "." + strs[i]);
                    byte[] output = RsaUtils.encrypt(strs[i], key);
                    len += output.length;
                    bytes.add(output);
                }
                byte[] dest = new byte[len];
                for (int i = 0, start = 0; i < bytes.size(); i++) {
                    byte[] tmp = bytes.get(i);
                    System.arraycopy(tmp, 0, dest, start, tmp.length);
                    start += tmp.length;
                }
                result = EncodeUtils.base64Encode2String(dest);
                LogUtils.i("客户端请求加密数据->" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    ///< rsa字符分段
    @SuppressWarnings("null")
    private static String[] sectionStr(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] strs = null;
        int length = str.length();
        if (length > 128) {
            int len = length / 128 + (length % 128 > 0 ? 1 : 0);
            strs = new String[len];
            for (int i = 0, j = 0; i < length; i += 1) {
                int start = i * 128;
                int last = (i + 1) * 128;
                if (last > length) {
                    last = length;
                }
                if (j >= len) {
                    break;
                }
                strs[j++] = str.substring(start, last);
            }
        }
        return strs;
    }

    ///< 数据压缩
    public static byte[] compress(String data) {
        try {
            byte[] bytes = data.getBytes();
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            compress(bais, baos);
            byte[] output = baos.toByteArray();

            baos.flush();
            baos.close();
            bais.close();

            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///< 数据压缩
    private static void compress(InputStream is, OutputStream os)
            throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        int count;
        byte data[] = new byte[1024];
        while ((count = is.read(data, 0, data.length)) != -1) {
            gos.write(data, 0, count);
        }
        gos.finish();
        gos.close();
    }

    ///< 数据解压
    public static String unzip(InputStream in) {
        // Open the compressed stream
        GZIPInputStream gin;
        try {
            if (in == null) {
                LogUtils.i("服务器没有返回数据->null");
                return null;
            }
            gin = new GZIPInputStream(new BufferedInputStream(in));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // Transfer bytes from the compressed stream to the output stream
            byte[] buf = new byte[1024];
            int len;
            while ((len = gin.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            gin.close();
            out.close();
            String result = new String(out.toByteArray());
            LogUtils.i("服务器返回数据->" + result);
            return result;
        } catch (IOException e) {
            LogUtils.w("服务器返回数据解压异常:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static char[] k = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '*', '!'};


    public static String encode(String str) {
        if (StringUtils.isEmpty(str)) return "";

        str = EncodeUtils.base64Encode2String(str.getBytes());
        StringBuffer sb = new StringBuffer();
        char[] array = str.toCharArray();
        sb.append("x");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i] + k[(i % k.length)]);
            sb.append("_");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("y");
        return sb.toString();
    }

    public static String decode(String str) {
        if (StringUtils.isEmpty(str)) return "";

        if ((str.startsWith("x")) && (str.endsWith("y"))) {
            StringBuffer sb = new StringBuffer(str);
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
            str = sb.toString();
            String[] strs = str.split("_");
            sb = new StringBuffer();
            for (int i = 0; i < strs.length; i++) {
                sb.append((char) (Integer.parseInt(strs[i]) - k[(i % k.length)]));
            }
            return new String(EncodeUtils.base64Decode(sb.toString()));
        }
        return "";
    }

}
