package com.yc.soundmark.index.utils;

import com.alibaba.fastjson.JSON;
import com.kk.utils.LogUtil;
import com.yc.soundmark.base.constant.SpConstant;
import com.yc.soundmark.index.model.domain.ShareInfo;

import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2018/10/29 09:48.
 */
public class ShareInfoHelper {

    private static final String TAG = "ShareInfoHelper";
    private static ShareInfo mShareInfo;

    public static ShareInfo getShareInfo() {
        if (mShareInfo != null) {
            return mShareInfo;
        }
        ShareInfo shareInfo = null;
        try {
            String str = SPUtils.getInstance().getString(SpConstant.SHARE_INFO);

            shareInfo = JSON.parseObject(str, ShareInfo.class);

        } catch (Exception e) {

            LogUtil.msg(TAG + "  json parse error->" + e.getMessage());

        }
        mShareInfo = shareInfo;

        return mShareInfo;
    }

    public static void saveShareInfo(ShareInfo shareInfo) {
        ShareInfoHelper.mShareInfo = shareInfo;
        try {
            String str = JSON.toJSONString(shareInfo);

            SPUtils.getInstance().put(SpConstant.SHARE_INFO, str);
        } catch (Exception e) {
            LogUtil.msg(TAG + "  to json error->" + e.getMessage());
        }


    }
}
