package com.yc.english.main.view.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.main.model.domain.ArticleInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class AritleAdapter extends BaseQuickAdapter<ArticleInfo, BaseViewHolder> {

    private int mType;  // 0 文章  1 微课

    public AritleAdapter(List<ArticleInfo> data, int type) {
        super(R.layout.index_aritle_item, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticleInfo item) {
        int position = helper.getAdapterPosition();
        helper.setVisible(R.id.iv_microclass_type, false);
        if (mType == 1) {
            helper.setVisible(R.id.iv_microclass_type, true);
            if (getData().size() - 1 == position) {
                helper.setVisible(R.id.line, false);
            }
        }
    }
}
