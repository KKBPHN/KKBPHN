package com.android.camera.features.mimoji2.fragment.bottomlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.widget.baseview.BaseItemAnimator;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerAdapter;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerViewHolder;
import java.util.List;

public class MimojiBgAdapter extends BaseRecyclerAdapter {
    private int mLastSelectedPosition = -1;

    class BgViewViewHolder extends BaseRecyclerViewHolder {
        private ImageView imageView;
        private ImageView mSelectItemView;

        public BgViewViewHolder(@NonNull View view) {
            super(view);
            FolmeUtils.handleListItemTouch(view, BaseItemAnimator.DEFAULT_LIST_DURATION);
            this.imageView = (ImageView) view.findViewById(R.id.mimoji_item_image);
            this.mSelectItemView = (ImageView) view.findViewById(R.id.mimoji_item_selected_indicator);
        }

        public View[] getRotateViews() {
            return new View[]{this.itemView};
        }

        public void setData(MimojiBgInfo mimojiBgInfo, int i) {
            ImageView imageView2;
            int i2;
            if (mimojiBgInfo.getResourceId() > 0) {
                this.itemView.setBackgroundResource(R.color.transparent);
                imageView2 = this.imageView;
                i2 = mimojiBgInfo.getResourceId();
            } else {
                View view = this.itemView;
                view.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_mimoji_item_off_gray));
                imageView2 = this.imageView;
                i2 = R.drawable.ic_mimoji_off;
            }
            imageView2.setImageResource(i2);
            String string = this.itemView.getResources().getString(mimojiBgInfo.getDescId());
            Util.correctionSelectView(this.imageView, mimojiBgInfo.isSelected());
            if (mimojiBgInfo.isSelected()) {
                this.mSelectItemView.setBackground(this.itemView.getContext().getResources().getDrawable(R.drawable.bg_mimoji_animal_selected));
                this.mSelectItemView.setVisibility(0);
                StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(", ");
                sb.append(this.imageView.getResources().getString(R.string.accessibility_selected));
                string = sb.toString();
            } else {
                this.mSelectItemView.setVisibility(8);
            }
            this.itemView.setContentDescription(string);
        }
    }

    public MimojiBgAdapter(List list) {
        super(list);
    }

    public void clearState() {
        if (getDataList() != null) {
            for (int i = 0; i < getItemCount(); i++) {
                MimojiBgInfo mimojiBgInfo = (MimojiBgInfo) getDataList().get(i);
                if (mimojiBgInfo.isSelected()) {
                    mimojiBgInfo.setSelected(false);
                    notifyItemChanged(i);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public BaseRecyclerViewHolder onCreateBaseRecyclerViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BgViewViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_mimoji_item_change_bg, viewGroup, false));
    }

    public boolean setSelectState(int i) {
        int i2 = this.mLastSelectedPosition;
        Boolean valueOf = Boolean.valueOf(false);
        if (i2 != i || !isAvailablePosion(i)) {
            this.mLastSelectedPosition = -1;
            if (isAvailablePosion(i)) {
                for (int i3 = 0; i3 < getItemCount(); i3++) {
                    MimojiBgInfo mimojiBgInfo = (MimojiBgInfo) getDataList().get(i3);
                    if (mimojiBgInfo.isSelected()) {
                        mimojiBgInfo.setSelected(false);
                        notifyItemChanged(i3);
                    }
                }
                ((MimojiBgInfo) getDataList().get(i)).setSelected(true);
                notifyItemChanged(i, valueOf);
                this.mLastSelectedPosition = i;
            }
            return true;
        }
        notifyItemChanged(i, valueOf);
        return false;
    }
}
