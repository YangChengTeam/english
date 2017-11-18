package com.yc.english.group.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.kk.guide.GuideView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.GroupInfoHelper;

import io.rong.imkit.InputBar;
import io.rong.imkit.RongExtension;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CustomServiceMode;

/**
 * Created by wanglin  on 2017/8/31 16:19.
 */

public class ConversationExFragment extends ConversationFragment {

    private static final String TAG = "ConversationExFragment";
    private ImageView iv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            RongExtension rongExtension = (RongExtension) view.findViewById(io.rong.imkit.R.id.rc_extension);
            iv = (ImageView) rongExtension.findViewById(io.rong.imkit.R.id.rc_plugin_toggle);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showGuide();
    }

    private void showGuide() {
        GuidePopupWindow.Builder builder = new GuidePopupWindow.Builder();
        final GuidePopupWindow guidePopupWindow = builder.setType(GuideView.GuideType.CIRCLE).setDelay(1f).setTargetView(iv).setGuideCallback(new GuideCallback() {
            @Override
            public void onClick(GuidePopupWindow guidePopupWindow) {
                guidePopupWindow.dismiss();
            }
        })
                .build(getActivity());
        guidePopupWindow.addCustomView(R.layout.group_guide_plugin_extension, R.id.btn_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guidePopupWindow.dismiss();
            }
        });
//        guidePopupWindow.setDebug(true);
        guidePopupWindow.show("plguin");
    }

    @Override
    public void onVoiceInputToggleTouch(View v, MotionEvent event) {

        if (GroupInfoHelper.getClassInfo().getIs_allow_talk() == 0) {
            TipsHelper.tips(getActivity(), GroupConstant.FORBID_CONTENT);
            return;
        }
        super.onVoiceInputToggleTouch(v, event);
    }


    @Override
    public void onSendToggleClick(View v, String text) {
        if (GroupInfoHelper.getClassInfo().getIs_allow_talk() == 0) {
            TipsHelper.tips(getActivity(), GroupConstant.FORBID_CONTENT);
            return;
        }
        super.onSendToggleClick(v, text);
    }
}
