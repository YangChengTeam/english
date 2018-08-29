package com.yc.english.main.hepler;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.UIUitls;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.LogUtil;
import com.yc.english.EnglishApp;
import com.yc.english.base.helper.EnginHelper;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.group.utils.ConnectUtils;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.group.view.provider.PublishTaskMessageProvider;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.domain.UserInfoWrapper;
import com.yc.english.main.view.activitys.LoginActivity;
import com.yc.english.setting.model.bean.ShareStateInfo;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class UserInfoHelper {
    private static UserInfo mUserInfo;

    public static UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        String userinfoStr = SPUtils.getInstance().getString(Constant.USER_INFO);
        UserInfo userInfo = null;
        try {
            userInfo = JSON.parseObject(userinfoStr, UserInfo.class);
        } catch (Exception e) {
            LogUtils.e("error->" + e);
        }
        mUserInfo = userInfo;
        return userInfo;
    }

    public static void saveUserInfo(UserInfo userInfo) {
        try {
            String userinfoStr = toJsonStr(userInfo);
            SPUtils.getInstance().put(Constant.USER_INFO, userinfoStr);
            mUserInfo = userInfo;
        } catch (Exception e) {
            LogUtils.e("error->" + e);
        }
    }

    public static boolean isLogin() {
        return getUserInfo() != null;
    }

    public static void clearUserInfo() {
        SPUtils.getInstance().remove(Constant.USER_INFO);
        mUserInfo = null;
    }

    public static void utils(Context context, ResultInfo<UserInfoWrapper> resultInfo) {
        UserInfoHelper.saveUserInfo(resultInfo.data.getInfo());
//        UserInfoHelper.connect(context, resultInfo.data.getInfo().getUid());
        RxBus.get().post(Constant.USER_INFO, resultInfo.data.getInfo());
        RxBus.get().post(BusAction.GROUP_LIST, "from getUserInfo");
        SPUtils.getInstance().put(Constant.PHONE, resultInfo.data.getInfo().getMobile());
//        UserInfoHelper.connect(context, resultInfo.data.getInfo().getUid());
    }

    public static void connect(final Context context, final String uid) {
        String token = SPUtils.getInstance().getString(ConnectUtils.TOKEN);
        if (!StringUtils.isEmpty(token)) {
            ConnectUtils.contact(context, token);
        }
        EnginHelper.getTokenInfo(context, uid).subscribe(new
                                                                 Subscriber<ResultInfo<TokenInfo>>() {
                                                                     @Override
                                                                     public void onCompleted() {

                                                                     }

                                                                     @Override
                                                                     public void onError(Throwable e) {
                                                                         connect(context, uid);
                                                                     }

                                                                     @Override
                                                                     public void onNext(final ResultInfo<TokenInfo> resultInfo) {
                                                                         ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                                                                             @Override
                                                                             public void resultInfoEmpty(String message) {
                                                                                 connect(context, uid);
                                                                             }

                                                                             @Override
                                                                             public void resultInfoNotOk(String message) {
                                                                                 connect(context, uid);
                                                                             }

                                                                             @Override
                                                                             public void reulstInfoOk() {
                                                                                 ConnectUtils.contact(context,
                                                                                         resultInfo.data.getToken());
                                                                             }
                                                                         });
                                                                     }
                                                                 });
    }

    public static void login(final Context context) {
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo == null) {
            return;
        }
        EnginHelper.login(context, userInfo.getName(), userInfo.getPwd()).subscribe(new Subscriber<ResultInfo<UserInfoWrapper>>
                () {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                UIUitls.postDelayed(1500, new Runnable() {
                    @Override
                    public void run() {
                        login(context);
                    }
                });
            }

            @Override
            public void onNext(final ResultInfo<UserInfoWrapper> userInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(userInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        UIUitls.postDelayed(1500, new Runnable() {
                            @Override
                            public void run() {
                                login(context);
                            }
                        });
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        clearUserInfo();
                    }

                    @Override
                    public void reulstInfoOk() {
                        UserInfoHelper.saveUserInfo(userInfoResultInfo.data.getInfo());
//                        getOpenShareVip(context, userInfoResultInfo.data.getInfo().getUid());
//                        UserInfoHelper.connect(context, userInfoResultInfo.data.getInfo().getUid());
                    }
                });
            }
        });

    }

    /**
     * 分享是否开启体验VIP
     */
    private static void getOpenShareVip(Context context, String usr_id) {

        LogUtil.msg("login success  " + usr_id);
        EngineUtils.getShareVipState(context, usr_id).subscribe(new Subscriber<ResultInfo<ShareStateInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ShareStateInfo> shareResult) {
                ResultInfoHelper.handleResultInfo(shareResult, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (shareResult.data != null) {
                            if (shareResult.data.getStatus() == 1) {
                                EnglishApp.isOpenShareVip = true;
                                EnglishApp.trialDays = shareResult.data.getDays();
                            }
                        }
                    }
                });

            }
        });
    }

    public static boolean isGotoLogin(Context context) {
        if (!isLogin()) {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public interface Callback {
        void showUserInfo(UserInfo userInfo);

        void showNoLogin();
    }

    public static void getUserInfoDo(final Callback callback) {
        Observable.just("").map(new Func1<String, UserInfo>() {
            @Override
            public UserInfo call(String s) {
                return UserInfoHelper.getUserInfo();
            }
        }).subscribeOn(Schedulers.io()).filter(new Func1<UserInfo, Boolean>() {
            @Override
            public Boolean call(UserInfo userInfo) {
                boolean flag = !EmptyUtils.isEmpty(userInfo);
                if (!flag) {
                    UIUitls.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.showNoLogin();
                        }
                    });
                }
                return flag;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UserInfo>() {
            @Override
            public void call(UserInfo userInfo) {
                callback.showUserInfo(userInfo);
            }
        });
    }




    private static String toJsonStr(UserInfo userInfo) {
        return "{\"mobile\":\"" + userInfo.getMobile() + "\", \"nick_name\":\"" + userInfo.getNickname() + "\", \"name\":\""
                + userInfo.getName() + "\", \"face\":\""
                + userInfo.getAvatar() + "\", \"school\":\""
                + userInfo.getSchool() + "\", \"pwd\":\""
                + userInfo.getPwd() + "\", \"id\":\""
                + userInfo.getUid() + "\", \"isLogin\":\""
                + userInfo.isLogin() + "\"}";
    }
}
