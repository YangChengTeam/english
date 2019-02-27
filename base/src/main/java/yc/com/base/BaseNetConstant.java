package yc.com.base;

/**
 * Created by wanglin  on 2018/3/7 14:34.
 */

public class BaseNetConstant {

    private static boolean IS_DEBUG = false;

    private static String DEBUG_URL = "http://";

    private static String BASE_URL = "https://answer.bshu.com/v1/";

    public static String getBaseUrl() {
        return IS_DEBUG ? DEBUG_URL : BASE_URL;
    }


}
