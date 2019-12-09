package com.yc.junior.english.speak.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.utils.LogUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import yc.com.blankj.utilcode.util.TimeUtils;


/**
 * Created by wanglin  on 2017/10/27 13:49.
 */

public class IndexRecommendAdapter extends RecyclerView.Adapter<IndexRecommendAdapter.MyViewHolder> {
    private static final int TYPE_AD = 1;
    private static final int TYPE_DATA = 0;
    private Context mContext;

    private List<Object> mData;
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap;

    public IndexRecommendAdapter(Context context, List list, HashMap<NativeExpressADView, Integer> adViewPositionMap) {
        this.mContext = context;
        this.mData = list;
        this.mAdViewPositionMap = adViewPositionMap;
    }


    public void addADViewToPosition(int position, NativeExpressADView adView) {
        if (mData != null && position >= 0 && position < mData.size() && adView != null) {
            mData.add(position, adView);
            notifyDataSetChanged();
        }
    }

    // 移除NativeExpressADView的时候是一条一条移除的
    public void removeADView(int position, NativeExpressADView adView) {
        mData.remove(position);
        notifyItemRemoved(position); // position为adView在当前列表中的位置
        notifyItemRangeChanged(0, mData.size() - 1);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = (viewType == TYPE_AD) ? R.layout.item_express_ad : R.layout.index_aritle_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) instanceof NativeExpressADView ? TYPE_AD : TYPE_DATA;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (TYPE_AD == type) {
            final NativeExpressADView adView = (NativeExpressADView) mData.get(position);
            mAdViewPositionMap.put(adView, position); // 广告在列表中的位置是可以被更新的
            if (holder.container.getChildCount() > 0
                    && holder.container.getChildAt(0) == adView) {
                return;
            }

            if (holder.container.getChildCount() > 0) {
                holder.container.removeAllViews();
            }

            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }
            adView.render(); // 调用render方法后sdk才会开始展示广告
            holder.container.addView(adView);

        } else {
            CourseInfo item = (CourseInfo) mData.get(position);

            long addTime = Long.parseLong(item.getAdd_time()) * 1000;
            holder.tvTitle.setText(item.getTitle());
            holder.tvTime.setText(TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())));
            holder.ivMicroclassType.setVisibility(View.GONE);
            holder.tvPvNum.setText(String.format(mContext.getString(R.string.count), item.getPv_num()));

            GlideHelper.imageView(mContext, holder.ivIcon, item.getImg(), 0);
            if (mData.size() - 1 == position) {
                holder.line.setVisibility(View.GONE);
            } else {
                holder.line.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            });


        }
    }


    public void addNewData(List list) {

        this.mData = list;
        LogUtil.msg("list: " + list.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData != null) return mData.size();
        else {
            return 0;
        }
    }


    public CourseInfo getItem(int position) {

        return (CourseInfo) mData.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ViewGroup container;
        public TextView tvTitle;
        public TextView tvTime;
        public ImageView ivMicroclassType;

        public View line;
        public ImageView ivIcon;
        public TextView tvPvNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.express_ad_container);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMicroclassType = itemView.findViewById(R.id.iv_microclass_type);
            line = itemView.findViewById(R.id.line);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvPvNum = itemView.findViewById(R.id.tv_pv_num);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
