package com.yc.english.base.utils;

import android.content.Context;
import android.net.Uri;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.EnginHelper;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/8/9.
 */

public class RongIMUtil {
    public static void refreshUserInfo() {
        RongIM.getInstance().refreshUserInfoCache(getUserInfo());
    }

    public static UserInfo getUserInfo() {
        com.yc.english.main.model.domain.UserInfo userInfo = UserInfoHelper.getUserInfo();
        UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), Uri.parse(userInfo.getAvatar()));
        return ruserInfo;
    }

    public static void setUserInfo() {
        RongIM.getInstance().setCurrentUserInfo(getUserInfo());
    }


    public static Observable<ResultInfo<com.yc.english.main.model.domain.UserInfo>> refreshObservable;

    public static void refreshUserInfo(final Context context, final String uid) {
        if (refreshObservable == null) {
            refreshObservable = EnginHelper.getUserInfo(context, uid).debounce(1, TimeUnit.DAYS);
        }
        refreshObservable.subscribe(new
                                            Action1<ResultInfo<com.yc.english.main
                                                    .model.domain
                                                    .UserInfo>>() {
                                                @Override
                                                public void call(final ResultInfo<com.yc.english.main.model.domain.UserInfo> userInfoResultInfo) {
                                                    ResultInfoHelper.handleResultInfo(userInfoResultInfo, new ResultInfoHelper.Callback() {
                                                        @Override
                                                        public void resultInfoEmpty(String message) {

                                                        }

                                                        @Override
                                                        public void resultInfoNotOk(String message) {

                                                        }

                                                        @Override
                                                        public void reulstInfoOk() {
                                                            if (userInfoResultInfo.data != null) {
                                                                com.yc.english.main.model.domain.UserInfo userInfo = userInfoResultInfo.data;
                                                                UserInfo ruserInfo = new UserInfo(userInfo.getUid(), userInfo.getNickname(), Uri.parse(userInfo.getAvatar()));
                                                                RongIM.getInstance().refreshUserInfoCache(ruserInfo);
                                                            }
                                                        }
                                                    });
                                                }
                                            });
    }

    public static void disconnect() {
        RongIM.getInstance().disconnect();
    }


    public static boolean isConnect() {
        return RongIM.getInstance().getCurrentConnectionStatus().getValue() == 0;
    }


<<<<<<< HEAD
=======
    public static void reJoinUser(final Context context, final Message rmessage) {
        EngineUtils.queryGroupById(context, rmessage.getTargetId(), "").subscribe(new
                                                                                          Action1<ResultInfo<ClassInfoWarpper>>() {
                                                                                              @Override
                                                                                              public void call(final ResultInfo<ClassInfoWarpper> classInfoWarpperResultInfo) {
                                                                                                  ResultInfoHelper.handleResultInfo(classInfoWarpperResultInfo, new ResultInfoHelper.Callback() {
                                                                                                      @Override
                                                                                                      public void resultInfoEmpty(String message) {

                                                                                                      }

                                                                                                      @Override
                                                                                                      public void resultInfoNotOk(String message) {

                                                                                                      }

                                                                                                      @Override
                                                                                                      public void reulstInfoOk() {
                                                                                                          ImUtils.joinGroup(new String[]{rmessage.getSenderUserId()}, rmessage.getTargetId(),
                                                                                                                  classInfoWarpperResultInfo.data.getInfo().getClassName())
                                                                                                                  .subscribe(new
                                                                                                                                     Action1<CodeSuccessResult>() {
                                                                                                                                         @Override
                                                                                                                                         public void call(CodeSuccessResult codeSuccessResult) {
                                                                                                                                             if (codeSuccessResult.getCode() != 200) {
                                                                                                                                                 reJoinUser(context, rmessage);
                                                                                                                                             } else {
                                                                                                                                                 RongIM.getInstance().sendMessage(rmessage, null, null, new IRongCallback
                                                                                                                                                         .ISendMessageCallback() {
                                                                                                                                                     @Override
                                                                                                                                                     public void onAttached(Message message) {
                                                                                                                                                         //消息本地数据库存储成功的回调
                                                                                                                                                     }

                                                                                                                                                     @Override
                                                                                                                                                     public void onSuccess(Message message) {
                                                                                                                                                         //消息通过网络发送成功的回调
                                                                                                                                                     }

                                                                                                                                                     @Override
                                                                                                                                                     public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                                                                                                                                                         //消息发送失败的回调
                                                                                                                                                     }
                                                                                                                                                 });
                                                                                                                                             }
                                                                                                                                         }
                                                                                                                                     });
                                                                                                      }
                                                                                                  });
                                                                                              }
                                                                                          });
>>>>>>> 9d7c579f1411e0cb1d30041080c42f89bca1c650





    public static void reconnect(Context context) {
        if (!isConnect()) {
            com.yc.english.main.model.domain.UserInfo userInfo = UserInfoHelper.getUserInfo();
            UserInfoHelper.connect(context, userInfo.getUid());
            TipsHelper.tips(context, "正在重连连接");
        }
    }
}
