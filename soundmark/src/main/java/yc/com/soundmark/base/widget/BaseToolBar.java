package yc.com.soundmark.base.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseView;
import yc.com.soundmark.R;
import yc.com.soundmark.base.fragment.BasePayFragment;
import yc.com.soundmark.index.fragment.VipEquitiesFragment;


public abstract class BaseToolBar extends BaseView {
    private BaseActivity mActivity;
    protected boolean isShowNavigationIcon;

    Toolbar mToolbar;

    FrameLayout mtoolbarWarpper;

    LinearLayout toolbarIntroduce;

    ImageView ivRightIcon;

    TextView tvRightTitle;


    ImageView ivLeftIcon;
    protected TextView mTitleTextView;

    public FrameLayout getToolbarWarpper() {
        return mtoolbarWarpper;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }



    private Context mContext;

    public BaseToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void init() {
        mToolbar = findViewById(R.id.toolbar_sub);
        mtoolbarWarpper = findViewById(R.id.toolbarWarpper);
        toolbarIntroduce = findViewById(R.id.toolbar_introduce);
        ivRightIcon = findViewById(R.id.iv_right_icon);
        tvRightTitle = findViewById(R.id.tv_right_title);
        ivLeftIcon = findViewById(R.id.iv_left_icon);
        mTitleTextView = findViewById(R.id.tv_tb_title);
    }

    public void init(final BaseActivity activity, final Class clazz) {


        mToolbar.setTitle("");
        mActivity = activity;
        activity.setSupportActionBar(mToolbar);
        if (isShowNavigationIcon) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }

        if (backClickListener != null) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    backClickListener.onClick(v);
                }
            });
        }

//        Glide.with(mActivity).load(R.mipmap.ic_launcher).asBitmap().centerCrop().into(ivLeftIcon);

        RxView.clicks(toolbarIntroduce).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (null != clazz) {//&& clazz == WebActivity.class
                    MobclickAgent.onEvent(mContext, "book-read-click", "课本点读");

                    Intent intent = new Intent(activity, clazz);
                    activity.startActivity(intent);

//                    if (clazz == VipEquitiesActivity.class) {
//
//                    } else if (clazz == WebActivity.class) {
//
//                    }

                } else {
                    MobclickAgent.onEvent(mContext, "VIP-click", "会员权益");
                    VipEquitiesFragment vipEquitiesFragment = new VipEquitiesFragment();
                    vipEquitiesFragment.show(mActivity.getSupportFragmentManager(), "");

                }
            }
        });
        RxView.clicks(ivLeftIcon).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                BasePayFragment basePayFragment = new BasePayFragment();
                basePayFragment.show(mActivity.getSupportFragmentManager(), "");
            }
        });


    }


    public void init(final BaseActivity activity) {
        this.init(activity, null);
    }

    private OnClickListener backClickListener;

    public void setBackOnClickListener(final OnClickListener onClickListener) {
        backClickListener = onClickListener;
    }

    public void setOnMenuItemClickListener() {
        if (onItemClickLisener != null) {
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onItemClickLisener.onClick();
                    return false;
                }
            });
        }
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setTitleSize(float titleSize) {
        mTitleTextView.setTextSize(ScreenUtil.dip2px(mContext, titleSize));
    }


    public void setTvRightTitleAndIcon(String title, int resId) {
        tvRightTitle.setText(title);
        ivRightIcon.setImageResource(resId);
    }

    public void setTitleGravity(int gravity) {
        mTitleTextView.setGravity(gravity);
    }

    public void showNavigationIcon() {
        mToolbar.setNavigationIcon(R.mipmap.base_back);
        isShowNavigationIcon = true;
        ivLeftIcon.setVisibility(GONE);
    }

    public void clear() {
        mToolbar.getMenu().clear();
    }

    protected boolean hasMenu;

    protected int mIconResid = 0;

    protected String mMenuTitle;

    private OnItemClickLisener onItemClickLisener;

    public int getmIconResid() {
        return mIconResid;
    }

    public void setMenuIcon(int iconResid) {
        hasMenu = true;
        this.mIconResid = iconResid;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String mMenuTitle) {
        hasMenu = true;
        this.mMenuTitle = mMenuTitle;
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    public void setMenuTitleColor(int color) {

    }

    public interface OnItemClickLisener {
        void onClick();
    }

    /**
     * 设置音标简介隐藏
     *
     * @param flag
     */

    public void setRightContainerVisible(boolean flag) {
        toolbarIntroduce.setVisibility(flag ? VISIBLE : GONE);
    }


}
