package com.yc.soundmark.study.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.soundmark.study.model.domain.WordInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/11/1 09:12.
 */
public class StudyVowelAdapter extends BaseQuickAdapter<WordInfo, BaseViewHolder> {
    public StudyVowelAdapter(List<WordInfo> data) {
        super(R.layout.study_vowel_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WordInfo item) {
        helper.setText(R.id.tv_vowel, item.getName());
        setItemState(helper, item);


    }

    private void setItemState(BaseViewHolder helper, WordInfo item) {

        if (UserInfoHelper.isYbVip()){
            helper.setVisible(R.id.iv_cover_layer, false);
            helper.setVisible(R.id.iv_lock, false);
        }else {
            helper.setVisible(R.id.iv_cover_layer,item.getIs_vip()==1);
            helper.setVisible(R.id.iv_lock, item.getIs_vip()==1);
        }


    }

}
