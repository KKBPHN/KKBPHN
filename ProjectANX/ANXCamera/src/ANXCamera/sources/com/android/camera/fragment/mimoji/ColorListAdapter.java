package com.android.camera.fragment.mimoji;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.log.Log;
import com.android.camera.ui.CircleImageView;
import com.arcsoft.avatar.AvatarConfig.ASAvatarConfigInfo;
import java.util.ArrayList;
import java.util.List;

public class ColorListAdapter extends Adapter {
    public static final String TAG = "ColorListAdapter";
    /* access modifiers changed from: private */
    public ClickCheck clickCheck;
    /* access modifiers changed from: private */
    public AvatarConfigItemClick mAvatarConfigItemClick;
    private Context mContext;
    private List mDatas;
    public int mLastPosion = -1;
    /* access modifiers changed from: private */
    public LinearLayoutManagerWrapper mLinearLayoutManagerWrapper;
    /* access modifiers changed from: private */
    public RecyclerView mRootView;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        CircleImageView ivColor;

        public ViewHolder(View view) {
            super(view);
            this.ivColor = (CircleImageView) view.findViewById(R.id.iv_color);
        }
    }

    public ColorListAdapter(Context context, AvatarConfigItemClick avatarConfigItemClick, LinearLayoutManagerWrapper linearLayoutManagerWrapper) {
        this.mContext = context;
        this.mDatas = new ArrayList();
        this.mAvatarConfigItemClick = avatarConfigItemClick;
        this.mLinearLayoutManagerWrapper = linearLayoutManagerWrapper;
    }

    public int getItemCount() {
        List list = this.mDatas;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public float getSelectItem(int i) {
        return AvatarEngineManager.getInstance().getInnerConfigSelectIndex(i);
    }

    public LinearLayoutManagerWrapper getmLinearLayoutManagerWrapper() {
        return this.mLinearLayoutManagerWrapper;
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRootView = recyclerView;
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ASAvatarConfigInfo aSAvatarConfigInfo = (ASAvatarConfigInfo) this.mDatas.get(i);
        final CircleImageView circleImageView = viewHolder.ivColor;
        float selectItem = getSelectItem(aSAvatarConfigInfo.configType);
        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int intValue = ((Integer) argbEvaluator.evaluate(aSAvatarConfigInfo.continuousValue, Integer.valueOf(aSAvatarConfigInfo.startColorValue), Integer.valueOf(aSAvatarConfigInfo.endColorValue))).intValue();
        StringBuilder sb = new StringBuilder();
        sb.append(viewHolder.itemView.getContext().getString(R.string.accessibility_color));
        sb.append(i + 1);
        final String sb2 = sb.toString();
        if (selectItem == ((float) aSAvatarConfigInfo.configID)) {
            circleImageView.updateView(true, intValue);
            this.mLastPosion = i;
            View view = viewHolder.itemView;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(", ");
            sb3.append(viewHolder.itemView.getContext().getString(R.string.accessibility_selected));
            view.setContentDescription(sb3.toString());
        } else {
            circleImageView.updateView(false, intValue);
            viewHolder.itemView.setContentDescription(sb2);
        }
        View view2 = viewHolder.itemView;
        final int i2 = i;
        final ViewHolder viewHolder2 = viewHolder;
        AnonymousClass1 r1 = new OnClickListener() {
            public void onClick(View view) {
                int i;
                RecyclerView recyclerView;
                if (ColorListAdapter.this.clickCheck == null || ColorListAdapter.this.clickCheck.checkClickable()) {
                    ColorListAdapter colorListAdapter = ColorListAdapter.this;
                    if (colorListAdapter.mLastPosion == i2 || argbEvaluator == null || colorListAdapter.mRootView == null) {
                        if (Util.isAccessible()) {
                            view.postDelayed(new O00000Oo(view), 100);
                        }
                        return;
                    }
                    String str = ColorListAdapter.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onClick position=");
                    sb.append(i2);
                    Log.u(str, sb.toString());
                    if (aSAvatarConfigInfo.configType == 2) {
                        AvatarEngineManager.getInstance().setInnerConfigSelectIndex(19, (float) aSAvatarConfigInfo.configID);
                        AvatarEngineManager.getInstance().setInnerConfigSelectIndex(15, (float) aSAvatarConfigInfo.configID);
                    }
                    if (ColorListAdapter.this.mLinearLayoutManagerWrapper != null) {
                        if (i2 == ColorListAdapter.this.mLinearLayoutManagerWrapper.findFirstVisibleItemPosition() || i2 == ColorListAdapter.this.mLinearLayoutManagerWrapper.findFirstCompletelyVisibleItemPosition()) {
                            recyclerView = ColorListAdapter.this.mRootView;
                            i = Math.max(0, i2 - 1);
                        } else if (i2 == ColorListAdapter.this.mLinearLayoutManagerWrapper.findLastVisibleItemPosition() || i2 == ColorListAdapter.this.mLinearLayoutManagerWrapper.findLastCompletelyVisibleItemPosition()) {
                            recyclerView = ColorListAdapter.this.mRootView;
                            i = Math.min(i2 + 1, ColorListAdapter.this.getItemCount() - 1);
                        }
                        recyclerView.scrollToPosition(i);
                    }
                    AvatarEngineManager instance = AvatarEngineManager.getInstance();
                    ASAvatarConfigInfo aSAvatarConfigInfo = aSAvatarConfigInfo;
                    instance.setInnerConfigSelectIndex(aSAvatarConfigInfo.configType, (float) aSAvatarConfigInfo.configID);
                    ViewHolder viewHolder = (ViewHolder) ColorListAdapter.this.mRootView.findViewHolderForAdapterPosition(ColorListAdapter.this.mLastPosion);
                    if (viewHolder != null) {
                        viewHolder.ivColor.updateView(false);
                        View view2 = viewHolder.itemView;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(viewHolder2.itemView.getContext().getString(R.string.accessibility_color));
                        sb2.append(ColorListAdapter.this.mLastPosion + 1);
                        view2.setContentDescription(sb2.toString());
                    } else {
                        ColorListAdapter colorListAdapter2 = ColorListAdapter.this;
                        colorListAdapter2.notifyItemChanged(colorListAdapter2.mLastPosion);
                    }
                    circleImageView.updateView(true);
                    ColorListAdapter colorListAdapter3 = ColorListAdapter.this;
                    colorListAdapter3.mLastPosion = i2;
                    colorListAdapter3.mAvatarConfigItemClick.onConfigItemClick(aSAvatarConfigInfo, true, i2);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(sb2);
                    sb3.append(", ");
                    sb3.append(viewHolder2.itemView.getContext().getString(R.string.accessibility_selected));
                    view.setContentDescription(sb3.toString());
                    if (Util.isAccessible()) {
                        view.postDelayed(new O000000o(view), 100);
                    }
                }
            }
        };
        view2.setOnClickListener(r1);
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.item_mimoji_color, viewGroup, false));
    }

    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRootView = null;
    }

    public void setClickCheck(ClickCheck clickCheck2) {
        this.clickCheck = clickCheck2;
    }

    public void setData(List list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }
}
