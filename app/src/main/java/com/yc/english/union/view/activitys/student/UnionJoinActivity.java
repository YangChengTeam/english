package com.yc.english.union.view.activitys.student;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupApplyJoinContract;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupApplyJoinPresenter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class UnionJoinActivity extends FullScreenActivity<GroupApplyJoinPresenter> implements GroupApplyJoinContract.View {
    private static final String TAG = "UnionJoinActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.ll_class_name)
    LinearLayout llClassName;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.roundView)
    ImageView roundView;
    @BindView(R.id.m_tv_tint)
    TextView mTvTint;

    private String vali_type;

    @Override
    public void init() {
        mPresenter = new GroupApplyJoinPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.join_union));
        etClassGroup.setHint("请填写会主提供的群号");

        RxTextView.textChanges(etClassGroup)
                .debounce(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        showOrHideView(charSequence.toString());
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_join_class;
    }

    @OnClick({R.id.ib_delete})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ib_delete:
                etClassGroup.setText("");
                break;
        }

    }


    public void showOrHideView(String s) {
        if (!TextUtils.isEmpty(s)) {
            ibDelete.setVisibility(View.VISIBLE);
            mPresenter.queryGroupById(this, "", s);
        } else {
            ibDelete.setVisibility(View.GONE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
            mTvTint.setVisibility(View.GONE);
        }
    }

    private void applyJoinGroup(ClassInfo classInfo) {

        KeyboardUtils.hideSoftInput(this);
        int valiType = Integer.parseInt(vali_type);

        if (GroupConstant.CONDITION_ALL_FORBID == valiType) {//禁止所有人加入
            TipsHelper.tips(this, String.format(getString(R.string.forbid_join), "公会"));
        } else {
            mPresenter.applyJoinGroup(classInfo);
        }
    }

    /**
     * 根据群号查询班群
     *
     * @param
     */
    @Override
    public void showGroup(final ClassInfo classInfo) {
        if (classInfo != null) {

            vali_type = classInfo.getVali_type();
            String className = classInfo.getClassName();
            tvClassName.setText(className);
            llClassName.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
            mTvTint.setVisibility(View.GONE);
            GlideHelper.circleImageView(this, roundView, classInfo.getImageUrl(), R.mipmap.default_avatar);
            RxView.clicks(btnJoin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    applyJoinGroup(classInfo);
                }
            });


        } else {
            mTvTint.setVisibility(View.VISIBLE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMemberList(List<UserInfo> list) {

    }
}
