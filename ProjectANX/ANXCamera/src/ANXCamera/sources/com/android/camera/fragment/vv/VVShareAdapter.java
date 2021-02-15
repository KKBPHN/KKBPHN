package com.android.camera.fragment.vv;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import java.util.List;

public class VVShareAdapter extends Adapter {
    private int mItemWidth;
    private OnClickListener mOnClickListener;
    private PackageManager mPackageManager;
    private List mShareInfoList;

    public VVShareAdapter(PackageManager packageManager, List list, OnClickListener onClickListener, int i) {
        this.mPackageManager = packageManager;
        this.mShareInfoList = list;
        this.mOnClickListener = onClickListener;
        this.mItemWidth = i;
    }

    public int getItemCount() {
        return this.mShareInfoList.size();
    }

    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        ResolveInfo resolveInfo = (ResolveInfo) this.mShareInfoList.get(i);
        commonRecyclerViewHolder.itemView.setTag(resolveInfo);
        commonRecyclerViewHolder.itemView.setOnClickListener(this.mOnClickListener);
        ImageView imageView = (ImageView) commonRecyclerViewHolder.getView(R.id.live_share_icon);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.live_share_name);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        imageView.setImageDrawable(resolveInfo.loadIcon(this.mPackageManager));
        textView.setText(resolveInfo.loadLabel(this.mPackageManager));
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_vv_share_item, viewGroup, false);
        inflate.getLayoutParams().width = this.mItemWidth;
        return new CommonRecyclerViewHolder(inflate);
    }

    public void setShareInfoList(List list) {
        this.mShareInfoList = list;
    }
}
