package com.yc.junior.english.community.view.activitys;

import android.os.Bundle;

import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.community.view.adapter.GalleryViewPager;
import com.yc.english.community.view.adapter.UrlPagerAdapter;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseActivity;
import com.yc.junior.english.community.view.adapter.GalleryViewPager;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/8/30.
 */

public class CommunityImageShowActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    GalleryViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.community_show_image_list;
    }

    @Override
    public void init() {
        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.getSerializable("images") != null) {
            List<String> items = (List<String>) bundle.getSerializable("images");
            if (items != null && items.size() > 0) {
                UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(CommunityImageShowActivity.this, items);
                viewPager.setOffscreenPageLimit(3);
                viewPager.setAdapter(pagerAdapter);
                viewPager.setCurrentItem(bundle.getInt("current_position", 0));
            }
        } else {
            TipsHelper.tips(this, "图片地址有误，请稍后重试");
        }
    }
}
