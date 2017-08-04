package com.yc.english.group.view.activitys.student;

import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupApplyJoinContract;
import com.yc.english.group.contract.GroupJoinContract;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupApplyJoinPresenter;
import com.yc.english.group.presenter.GroupJoinPresenter;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.main.hepler.UserInfoHelper;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class GroupJoinActivityNew extends FullScreenActivity<GroupApplyJoinPresenter> implements GroupApplyJoinContract.View {
    private static final String TAG = "GroupJoinActivity";
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
    private String class_id;//班群ID即群号
    private String className;
    private String vali_type;

    @Override
    public void init() {
        mPresenter = new GroupApplyJoinPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getString(R.string.group_join_class));
        roundView.setImageBitmap(ImageUtils.toRound(BitmapFactory.decodeResource(getResources(), R.mipmap.portial)));
        initListener();
    }

    private void initListener() {
        etClassGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showOrHideView(s);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_join_class;
    }

    @OnClick({R.id.btn_join, R.id.ib_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                String groupId = etClassGroup.getText().toString();
                String uid = UserInfoHelper.getUserInfo().getUid();

//                int verifyCondition = SPUtils.getInstance().getInt(class_id, GroupConstant.CONDITION_ALL_ALLOW);
                int valiType = Integer.parseInt(vali_type);

                if (GroupConstant.CONDITION_ALL_FORBID == valiType) {//禁止所有人加入
                    ToastUtils.showShort(getString(R.string.forbid_join));
                } else {
                    mPresenter.applyJoinGroup(uid, groupId);
                }


//                mPresenter.joinGroup(groupId + "", tvClassName.getText().toString());
                break;
            case R.id.ib_delete:
                etClassGroup.setText("");
                break;
        }

    }


    public void showOrHideView(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            ibDelete.setVisibility(View.VISIBLE);
            mPresenter.queryGroupById(this, "", s.toString());
        } else {
            ibDelete.setVisibility(View.GONE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
            mTvTint.setVisibility(View.GONE);
        }


    }

    /**
     * 根据群号查询班群
     *
     * @param
     */
    @Override
    public void showGroup(ClassInfo classInfo) {
        if (classInfo != null) {
            class_id = classInfo.getClass_id();
            vali_type = classInfo.getVali_type();
            className = classInfo.getClassName();
            tvClassName.setText(className);
            llClassName.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
            mTvTint.setVisibility(View.GONE);
        } else {
            mTvTint.setVisibility(View.VISIBLE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
        }
    }

    @Override
    public void apply(int type) {
        if (type == GroupConstant.CONDITION_ALL_ALLOW) {
            ToastUtils.showShort(getString(R.string.congratulation_join_group));

        } else if (type == GroupConstant.CONDITION_VERIFY_JOIN) {
            ToastUtils.showShort(getString(R.string.commit_apply_join));
        }
        finish();
    }
}
