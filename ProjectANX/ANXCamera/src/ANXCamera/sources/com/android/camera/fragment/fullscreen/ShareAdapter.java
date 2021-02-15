package com.android.camera.fragment.fullscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import java.util.List;

public class ShareAdapter extends Adapter {
    private int mItemWidth;
    private OnClickListener mOnClickListener;
    private List mShareInfoList;

    public ShareAdapter(List list, OnClickListener onClickListener, int i) {
        this.mShareInfoList = list;
        this.mOnClickListener = onClickListener;
        this.mItemWidth = i;
    }

    public int getItemCount() {
        return this.mShareInfoList.size();
    }

    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        ShareInfo shareInfo = (ShareInfo) this.mShareInfoList.get(i);
        commonRecyclerViewHolder.itemView.setTag(shareInfo);
        commonRecyclerViewHolder.itemView.setOnClickListener(this.mOnClickListener);
        ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.live_share_icon);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.live_share_name);
        imageView.setImageResource(shareInfo.drawableRes);
        textView.setText(shareInfo.textRes);
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_live_share_item, viewGroup, false);
        inflate.getLayoutParams().width = this.mItemWidth;
        return new CommonRecyclerViewHolder(inflate);
    }

    public void setShareInfoList(List list) {
        this.mShareInfoList = list;
    }
}
