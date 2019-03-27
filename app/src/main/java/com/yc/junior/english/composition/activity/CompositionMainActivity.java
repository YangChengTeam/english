package com.yc.junior.english.composition.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.WebActivity;
import com.yc.junior.english.composition.adapter.CompositionMainAdapter;
import com.yc.junior.english.composition.contract.EssayContract;
import com.yc.junior.english.composition.fragment.EssayFragment;
import com.yc.junior.english.composition.fragment.FodderFragment;
import com.yc.junior.english.composition.model.bean.CompositionInfo;
import com.yc.junior.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.junior.english.composition.presenter.EssayPresenter;
import com.yc.junior.english.composition.utils.ReflexUtils;
import com.yc.junior.english.composition.utils.TabLayout;
import com.yc.junior.english.composition.widget.VerticalTextView;
import com.yc.junior.english.main.hepler.BannerImageLoader;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.news.utils.SmallProcedureUtils;
import com.yc.junior.english.weixin.model.domain.CourseInfo;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.EmptyUtils;
import yc.com.base.StatusBarCompat;

/**
 * Created by wanglin  on 2019/3/22 15:13.
 */
public class CompositionMainActivity extends BaseActivity<EssayPresenter> implements EssayContract.View {
    @BindView(R.id.tabLayout_composition)
    TabLayout tabLayoutComposition;
    @BindView(R.id.viewPager_composition)
    ViewPager viewPagerComposition;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.verticalTv)
    VerticalTextView verticalTv;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private List<Fragment> fragmentList;

    private List<String> titleList;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_composition_main;
    }

    @Override
    public void init() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            root.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            nestedScrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        }

        mPresenter = new EssayPresenter(this, this);
        fragmentList = new ArrayList<>();
        fragmentList.add(new EssayFragment());
        fragmentList.add(new FodderFragment());
        mPresenter.getCompositionIndexInfo();
        StatusBarCompat.light(CompositionMainActivity.this);
        CompositionMainAdapter compositionMainAdapter = new CompositionMainAdapter(getSupportFragmentManager(), this, fragmentList);
        viewPagerComposition.setAdapter(compositionMainAdapter);
        tabLayoutComposition.setupWithViewPager(viewPagerComposition);
        tabLayoutComposition.setTabMode(TabLayout.MODE_SCROLLABLE);
        ReflexUtils.INSTANCE.reflex(tabLayoutComposition);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                int toolbarHeight = toolbar.getHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPagerComposition.getLayoutParams();
                layoutParams.height = ScreenUtil.getHeight(CompositionMainActivity.this) - toolbarHeight - tabLayoutComposition.getHeight();
                viewPagerComposition.setLayoutParams(layoutParams);
            }
        });

        initListener();
        initVerticalView();
    }

    private void initListener() {
        RxView.clicks(llSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(CompositionMainActivity.this, CompositionSearchActivity.class));

            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                //友盟统计各个幻灯点击数
                MobclickAgent.onEvent(CompositionMainActivity.this, slideInfo.getStatistics());
                if (slideInfo.getType().equals("0")) {
                    if (EmptyUtils.isEmpty(slideInfo.getTypeValue())) {
                        return;
                    }
                    Intent intent = new Intent(CompositionMainActivity.this, WebActivity.class);
                    intent.putExtra("title", slideInfo.getTitle());
                    intent.putExtra("url", slideInfo.getTypeValue());
                    startActivity(intent);
                } else if (slideInfo.getType().equals("1")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] split = typeValue.split("\\|");
                        Class clazz = Class.forName(split[0]);
                        Intent intent = new Intent(CompositionMainActivity.this, clazz);
                        if (split.length == 2) {
                            CourseInfo courseInfo = new CourseInfo();
                            courseInfo.setId(split[1]);
                            intent.putExtra("info", courseInfo);
                        }
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else if (slideInfo.getType().equals("2")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] strs = typeValue.split("\\|");
//                        LogUtil.msg("tag: " + strs[0] + "---" + strs[1]);
                        if (strs.length > 1) {
                            // 填应用AppId
                            String appId = strs[1];
                            String originId = strs[0];
                            SmallProcedureUtils.switchSmallProcedure(CompositionMainActivity.this, originId, appId);
                        }
                    } catch (Exception e) {
                        LogUtil.msg("e :" + e.getMessage());
                        ToastUtil.toast(CompositionMainActivity.this, "");
                    }
                }
            }
        });
    }


    private void initVerticalView() {
        titleList = new ArrayList<>();
        titleList.add("搜题");
        titleList.add("作文");
        titleList.add("素材");
        titleList.add("范文");
        titleList.add("记事");
        titleList.add("爱好");

        verticalTv.setTextList(titleList);//加入显示内容,集合类型
        verticalTv.setText(13, 0, Color.parseColor("#cdcdcd"));//设置属性,具体跟踪源码
        verticalTv.setTextStillTime(3000);//设置停留时长间隔
        verticalTv.setAnimTime(400);//设置进入和退出的时间间隔
        //对单条文字的点击监听
        verticalTv.setOnItemClickListener(new VerticalTextView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TO DO
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        verticalTv.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        verticalTv.stopAutoScroll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void showCompositionInfos(List<CompositionInfo> compositionInfos) {

    }

    @Override
    public void showCompositionIndexInfo(CompositionInfoWrapper data) {

    }

    @Override
    public void showBanner(List<String> images) {
        mBanner.isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }

    @Override
    public void hide() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoNet() {

    }

}
