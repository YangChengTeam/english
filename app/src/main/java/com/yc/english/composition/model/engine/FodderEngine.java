package com.yc.english.composition.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.R;
import com.yc.english.composition.model.bean.CompositionInfo;
import com.yc.english.composition.model.bean.FodderInfo;
import com.yc.english.composition.model.bean.FodderInfoWrapper;
import com.yc.english.main.model.domain.URLConfig;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.BaseEngine;

/**
 * Created by wanglin  on 2019/3/25 09:04.
 */
public class FodderEngine extends BaseEngine {
    public FodderEngine(Context context) {
        super(context);
    }

//    public Observable<List<FodderInfo>> getFodderInfos() {
//        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, List<FodderInfo>>() {
//            @Override
//            public List<FodderInfo> call(String s) {
//                List<FodderInfo> fodderInfos = new ArrayList<>();
//                FodderInfo fodderInfo1 = new FodderInfo();
//                fodderInfo1.setTitle("普通素材");
//                List<CompositionInfo> compositionInfos = new ArrayList<>();
//                compositionInfos.add(new CompositionInfo("1", R.mipmap.essay_depicicting_person_icon, "写人", "怎么写好写人的作文呢？这里有最新的技巧"));
//                compositionInfos.add(new CompositionInfo("2", R.mipmap.essay_record_event_icon, "记事", "多积累,可以帮助我们增强运用"));
//                compositionInfos.add(new CompositionInfo("3", R.mipmap.essay_depiction_scenery_icon, "写景", "语言优美是写景的关键，积累尤为重要"));
//                compositionInfos.add(new CompositionInfo("4", R.mipmap.essay_mesh_icon, "状物", "写状物语言要精准、严谨，这里有全新的。。"));
//                fodderInfo1.setCompositionInfos(compositionInfos);
//
//                fodderInfos.add(fodderInfo1);
//
//                FodderInfo fodderInfo2 = new FodderInfo();
//                fodderInfo2.setTitle("结构素材");
//                List<CompositionInfo> compositionInfos1 = new ArrayList<>();
//                compositionInfos1.add(new CompositionInfo("1", R.mipmap.essay_front_icon, "开头", "写好作文开头，是作文成功的第一步"));
//                compositionInfos1.add(new CompositionInfo("2", R.mipmap.essay_transition_icon, "过渡", "过渡句是文章的桥梁，可以使。。"));
//                compositionInfos1.add(new CompositionInfo("3", R.mipmap.essay_ending_icon, "结尾", "耐人寻味的文章结尾，给人留下。。"));
//                fodderInfo2.setCompositionInfos(compositionInfos1);
//
//                fodderInfos.add(fodderInfo2);
//
//                FodderInfo fodderInfo3 = new FodderInfo();
//                fodderInfo3.setTitle("拓展素材");
//                List<CompositionInfo> compositionInfos2 = new ArrayList<>();
//                compositionInfos2.add(new CompositionInfo("1", R.mipmap.essay_common_proverbs_icon, "常用谚语", ""));
//                compositionInfos2.add(new CompositionInfo("2", R.mipmap.essay_pearls_wisdom_icon, "名言警句", ""));
//                compositionInfos2.add(new CompositionInfo("3", R.mipmap.essential_sentence_icon, "必备句型", ""));
//                compositionInfos2.add(new CompositionInfo("4", R.mipmap.essay_famous_masterpiece_icon, "名家名篇", ""));
//                fodderInfo3.setCompositionInfos(compositionInfos2);
//
//                fodderInfos.add(fodderInfo3);
//
//
//                return fodderInfos;
//            }
//        }).observeOn(AndroidSchedulers.mainThread());
//    }

    public Observable<ResultInfo<FodderInfoWrapper>> getFodderIndexInfo() {
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.ZW_SUCAINAV_URL, new TypeReference<ResultInfo<FodderInfoWrapper>>() {
        }.getType(), null, true, true, true);

    }
}
