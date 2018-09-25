package com.yc.english.group.activitys;

import android.content.Intent;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.WebActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.main.model.domain.UserInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2018/3/12.
 */

public class CoachScoreActivity extends FullScreenActivity {

    //    @BindView(R.id.btn_score)
//    Button mScoreButton;
    @BindView(R.id.iv_teach_soundmark)
    ImageView ivTeachSoundmark;
    @BindView(R.id.iv_download_app)
    ImageView ivDownloadApp;


    @Override
    public int getLayoutId() {
        return R.layout.activity_coach_score;
    }

    @Override
    public void init() {
        mToolbar.setTitle("音标点读");
        mToolbar.showNavigationIcon();

//        RxView.clicks(mScoreButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                if (userInfo != null && userInfo.getIsVip() == 2) {
//                    BuySuccessDialog buySuccessDialog = new BuySuccessDialog(CoachScoreActivity.this);
//                    buySuccessDialog.show();
//                } else {
//                    VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", null);
//                }
//            }
//        });

        RxView.clicks(ivTeachSoundmark).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo
                IWXAPI api = WXAPIFactory.createWXAPI(CoachScoreActivity.this, GroupConstant.appid);

                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_e46e21f44c08"; // 填小程序原始id
//                    req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
            }
        });
        RxView.clicks(ivDownloadApp).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(CoachScoreActivity.this, WebActivity.class);
                intent.putExtra("title", "小学英语音标点读");
                intent.putExtra("url", "http:\\/\\/a.app.qq.com\\/o\\/simple.jsp?pkgname=com.yc.phonogram");
                startActivity(intent);
            }
        });
    }


}
