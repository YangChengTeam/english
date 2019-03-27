package com.yc.junior.english.composition.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ScreenUtil;
import com.yc.junior.english.composition.adapter.EssayItemAdapter;
import com.yc.junior.english.composition.contract.CompositionSearchContract;
import com.yc.junior.english.composition.model.bean.CompositionInfo;
import com.yc.junior.english.composition.presenter.CompositionSearchPresenter;
import com.yc.junior.english.composition.widget.FilterPopWindow;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.StateView;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.blankj.utilcode.util.KeyboardUtils;

/**
 * Created by wanglin  on 2019/3/25 11:41.
 */
public class CompositionSearchActivity extends BaseActivity<CompositionSearchPresenter> implements CompositionSearchContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search_content)
    EditText etSearchContent;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.iv_grade)
    ImageView ivGrade;
    @BindView(R.id.ll_grade)
    LinearLayout llGrade;
    @BindView(R.id.iv_topic)
    ImageView ivTopic;
    @BindView(R.id.ll_topic)
    LinearLayout llTopic;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.iv_theme)
    ImageView ivTheme;
    @BindView(R.id.ll_theme)
    LinearLayout llTheme;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.ll_guide)
    LinearLayout llGuide;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_topic)
    TextView tvTopic;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_theme)
    TextView tvTheme;
    private String grade = "";
    private String topic = "";
    private String type = "";
    private String theme = "";
    private String title = "";
    private EssayItemAdapter essayAdapter;

    private int page = 1;
    private int PAGESIZE = 10;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_composition_search;
    }

    @Override
    public void init() {
        mPresenter = new CompositionSearchPresenter(this, this);

        getData(title, grade, topic, theme, type);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        essayAdapter = new EssayItemAdapter(null, true);
        searchRecyclerView.setAdapter(essayAdapter);
        searchRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 8));


        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        RxView.clicks(llGrade).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.grade), ivGrade, tvGrade);
            }
        });
        RxView.clicks(llTopic).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.topic), ivTopic, tvTopic);
            }
        });
        RxView.clicks(llType).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.type), ivType, tvType);
            }
        });
        RxView.clicks(llTheme).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.theme), ivTheme, tvTheme);
            }
        });
        RxView.clicks(btnSearch).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                title = etSearchContent.getText().toString().trim();
                page = 1;
                getData(title, "", "", "", "");
                KeyboardUtils.hideSoftInput(CompositionSearchActivity.this);
            }
        });

        essayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CompositionInfo compositionInfo = essayAdapter.getItem(position);
                if (compositionInfo != null) {
                    CompositionDetailActivity.startActivity(CompositionSearchActivity.this, compositionInfo.getId());
                }
            }
        });

        essayAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(title, grade, topic, theme, type);
            }
        }, searchRecyclerView);

        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = searchRecyclerView.getChildAt(0);
                if (child.getTop() < 0) {
                    searchRecyclerView.setPadding(searchRecyclerView.getPaddingLeft(), 0, searchRecyclerView.getPaddingRight(), searchRecyclerView.getPaddingBottom());
                } else {
                    searchRecyclerView.setPadding(searchRecyclerView.getPaddingLeft(), ScreenUtil.dip2px(CompositionSearchActivity.this, 10f), searchRecyclerView.getPaddingRight(), searchRecyclerView.getPaddingBottom());
                }
            }
        });

    }

    private void showPopWindow(String name, final ImageView iv, final TextView tv) {
        final FilterPopWindow popWindow = new FilterPopWindow(this, name);
        popWindow.showAsDropDown(llGuide);
        iv.setImageResource(R.mipmap.up_arrow_icon);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv.setImageResource(R.mipmap.down_arrow_icon);
            }
        });
        popWindow.setOnPopClickListener(new FilterPopWindow.OnPopClickListener() {
            @Override
            public void onPopClick(String popName, String popId) {
                popWindow.dismiss();
                tv.setText(popName);
                resetData();
                if (tv == tvGrade) {
                    grade = popId;
                }
                if (tv == tvTopic) {
                    topic = popId;
                }
                if (tv == tvType) {
                    type = popId;
                }
                if (tv == tvTheme) {
                    theme = popId;
                }

                page = 1;

                getData("", grade, topic, theme, type);

            }
        });

    }

    private void resetData() {
        grade = "";
        topic = "";
        type = "";
        theme = "";
    }

    private void getData(String title, String grade_id, String topic, String ticai, String type) {
        mPresenter.searchCompositionInfos(title, grade_id, topic, ticai, type, page, PAGESIZE);
    }


    @Override
    public void showSearchResult(List<CompositionInfo> list) {
        if (page == 1)
            essayAdapter.setNewData(list);
        else {
            essayAdapter.addData(list);
        }
        if (list.size() == PAGESIZE) {
            essayAdapter.loadMoreComplete();
            page++;
        } else {
            essayAdapter.loadMoreEnd();
        }
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(searchRecyclerView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(searchRecyclerView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(searchRecyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(title, grade, topic, theme, type);
            }
        });
    }
}
