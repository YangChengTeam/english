package com.yc.english.group.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.blankj.subutil.util.ThreadPoolUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/7 10:42.
 */

public class GroupTaskPublishEngine extends BaseEngin {
    public GroupTaskPublishEngine(Context context) {
        super(context);
    }

    /**
     * 发布作业
     *
     * @param class_ids 班群ID,多个，逗号隔开
     * @param publisher 群主/发布人ID
     * @param desp
     * @return
     */
    public Observable<ResultInfo<String>> publishTask(String class_ids, String publisher, String desp, byte[] imges, byte[] voices, byte[] docs) {
        Map<String, Object> params = new HashMap<>();
        params.put("class_ids", class_ids);
        params.put("publisher", publisher);
        params.put("desp", desp);
        if (EmptyUtils.isNotEmpty(imges)) {
            params.put("imgs", imges);
        }
        if (EmptyUtils.isNotEmpty(voices)) {
            params.put("voices", voices);
        }
        if (EmptyUtils.isNotEmpty(docs)) {
            params.put("docs", docs);
        }

        return HttpCoreEngin.get(mContext).rxpost(NetConstan.publish_task, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, false, true, true);

    }

    public Observable<String> uploadFile(File file) {
        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.filename = "1.jpg";
        upFileInfo.file=file;
        upFileInfo.name = "file";

        return HttpCoreEngin.get(mContext).rxuploadFile(NetConstan.upload_richFile, String.class,upFileInfo,null,true);

    }

}
