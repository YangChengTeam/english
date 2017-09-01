package com.yc.english.group.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.kk.guide.GuideCallback;
import com.kk.guide.GuidePopupWindow;
import com.kk.guide.GuideView;
import com.yc.english.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by wanglin  on 2017/8/31 16:19.
 */

public class ConversationExFragment extends ConversationFragment {

    private ImageView iv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        RongExtension rongExtension = (RongExtension) view.findViewById(io.rong.imkit.R.id.rc_extension);
        iv = (ImageView) rongExtension.findViewById(io.rong.imkit.R.id.rc_plugin_toggle);


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
    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
        super.onPluginToggleClick(v, extensionBoard);

        RelativeLayout relativeLayout = (RelativeLayout) extensionBoard.getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) relativeLayout.getChildAt(0);
        LinearLayout layout = (LinearLayout) linearLayout.getChildAt(2);

        for (int i = 0; i < layout.getChildCount(); i++) {
            View childAt = layout.getChildAt(i);
            LogUtils.e(childAt);
        }

    }
}
