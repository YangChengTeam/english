package com.yc.soundmark.study.activity;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yc.junior.english.R;
import com.yc.soundmark.study.widget.ZoomImageView;

import yc.com.base.BaseActivity;

/**
 * Created by wanglin  on 2018/11/5 16:03.
 */
public class PreviewActivity extends BaseActivity {

    private ZoomImageView zoomImageView;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_picture;
    }

    @Override
    public void init() {
        zoomImageView = findViewById(R.id.zoomImageView);
        String img = getIntent().getStringExtra("img");

        Glide.with(this).asBitmap().load(img).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into(zoomImageView);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
