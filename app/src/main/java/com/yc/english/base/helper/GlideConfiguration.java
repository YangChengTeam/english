package com.yc.english.base.helper;


import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class GlideConfiguration extends AppGlideModule {

    public static final int GLIDE_CATCH_SIZE = 150 * 1000 * 1000;
    public static final String GLIDE_CARCH_DIR = "glide";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GLIDE_CARCH_DIR,
                GLIDE_CATCH_SIZE));
    }

}