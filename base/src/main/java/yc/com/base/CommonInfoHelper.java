package yc.com.base;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


import java.lang.reflect.Type;

/**
 * Created by wanglin  on 2018/2/9 11:34.
 */

public class CommonInfoHelper {

    public static <T> void getO(Context context, String key, final Type type, final onParseListener<T> listener) {

        try {
            CacheUtils.readCache(context, key, new CacheUtils.SubmitRunable() {
                @Override
                public void run() {
                    final String json = this.getJson();
                    UIUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            T t = parseData(json, type);

                            if (listener != null) {
                                listener.onParse(t);
                            }
                        }
                    });

                }
            }, new CacheUtils.SubmitRunable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onFail(getJson());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(CommonInfoHelper.class.getClass().getName(), "error:->>" + e.getMessage());
        }

    }

    public static <T> void setO(Context context, T t, String key) {
        try {
            CacheUtils.writeCache(context, key, JSON.toJSONString(t));
        } catch (Exception e) {
            Log.e(CommonInfoHelper.class.getClass().getName(), "error:->>" + e.getMessage());
        }

    }


    private static <T> T parseData(String result, Type type) {

        if (type.toString().equals("java.lang.String")) {
            return (T) result;
        }
        T resultInfo;
        if (type != null) {
            resultInfo = JSON.parseObject(result, type);
        } else {
            resultInfo = JSON.parseObject(result, new TypeReference<T>() {
            }.getType());
        }

        return resultInfo;
    }


    public interface onParseListener<T> {
        void onParse(T o);

        void onFail(String json);
    }
}
