package com.yc.junior.english.main.hepler;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.junior.english.base.helper.EnginHelper;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.model.bean.TokenInfo;
import com.yc.junior.english.group.utils.ConnectUtils;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.main.model.domain.UserInfoWrapper;
import com.yc.junior.english.main.view.activitys.LoginActivity;

import rx.Observable;
import rx.Subscriber;
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

    public static void utils(Context context, ResultInfo<UserInfoWrapper> resultInfo){
        UserInfoHelper.saveUserInfo(resultInfo.data.getInfo());
        UserInfoHelper.connect(context, resultInfo.data.getInfo().getUid());
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
        EnginHelper.login(context, userInfo.getName(), userInfo.getPwd()).subscribe(new
                                                                                            Subscriber<ResultInfo<UserInfoWrapper>>
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
                        UserInfoHelper.connect(context, userInfoResultInfo.data.getInfo().getUid());
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
