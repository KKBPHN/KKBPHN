package com.android.camera.features.mimoji2.fragment.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.features.mimoji2.bean.MimojiEmoticonInfo;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerAdapter;
import com.android.camera.features.mimoji2.widget.baseview.BaseRecyclerViewHolder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MimojiEmoticonAdapter extends BaseRecyclerAdapter {
    private AtomicBoolean mIsAllSelected = new AtomicBoolean(true);
    private RecyclerView mRootView;
    private OnAllSelectStateChangeListener onAllSelectStateChangeListener;

    class EmoticonViewViewHolder extends BaseRecyclerViewHolder {
        private ImageView imageMimojiEmoticon;
        private ImageView imageMimojiSelect;

        public EmoticonViewViewHolder(@NonNull View view) {
            super(view);
            this.imageMimojiEmoticon = (ImageView) view.findViewById(R.id.image_item_mimoji_emoticon);
            this.imageMimojiSelect = (ImageView) view.findViewById(R.id.image_item_select);
        }

        public void setData(MimojiEmoticonInfo mimojiEmoticonInfo, int i) {
            if (mimojiEmoticonInfo.getBitmap() != null) {
                this.imageMimojiEmoticon.setImageBitmap(mimojiEmoticonInfo.getBitmap());
            }
            this.imageMimojiSelect.setImageResource(mimojiEmoticonInfo.isSelected() ? R.drawable.ic_mimoji_emoticon_item_selected : R.drawable.ic_mimoji_emoticon_item_unselected);
            String string = this.itemView.getContext().getString(mimojiEmoticonInfo.getName() > 0 ? mimojiEmoticonInfo.getName() : R.string.lighting_pattern_null);
            if (mimojiEmoticonInfo.isSelected()) {
                View view = this.itemView;
                StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(", ");
                sb.append(this.itemView.getContext().getString(R.string.accessibility_selected));
                view.setContentDescription(sb.toString());
                return;
            }
            this.itemView.setContentDescription(string);
        }
    }

    interface OnAllSelectStateChangeListener {
        void onAllSelectStateChange(boolean z);
    }

    public MimojiEmoticonAdapter(List list) {
        super(list);
    }

    private void checkAllSelectedState() {
        if (getDataList() != null) {
            boolean z = true;
            Iterator it = getDataList().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (!((MimojiEmoticonInfo) it.next()).isSelected()) {
                        z = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (this.mIsAllSelected.get() != z) {
                this.mIsAllSelected.set(z);
                OnAllSelectStateChangeListener onAllSelectStateChangeListener2 = this.onAllSelectStateChangeListener;
                if (onAllSelectStateChangeListener2 != null) {
                    onAllSelectStateChangeListener2.onAllSelectStateChange(z);
                }
            }
        }
    }

    public void clearState() {
        if (getDataList() != null) {
            for (MimojiEmoticonInfo selected : getDataList()) {
                selected.setSelected(false);
            }
            notifyDataSetChanged();
            checkAllSelectedState();
        }
    }

    public boolean getIsAllSelected() {
        return this.mIsAllSelected.get();
    }

    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRootView = recyclerView;
    }

    /* access modifiers changed from: protected */
    public BaseRecyclerViewHolder onCreateBaseRecyclerViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new EmoticonViewViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_mimoji_emoticon_item, viewGroup, false));
    }

    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mRootView = null;
    }

    public void selectAll() {
        if (getDataList() != null) {
            for (MimojiEmoticonInfo selected : getDataList()) {
                selected.setSelected(true);
            }
            notifyDataSetChanged();
            checkAllSelectedState();
        }
    }

    public void setOnAllSelectStateChangeListener(OnAllSelectStateChangeListener onAllSelectStateChangeListener2) {
        this.onAllSelectStateChangeListener = onAllSelectStateChangeListener2;
    }

    public void setSelectState(MimojiEmoticonInfo mimojiEmoticonInfo, int i) {
        View view;
        if (getDataList() != null && i < getItemCount()) {
            getDataList().set(i, mimojiEmoticonInfo);
            notifyItemChanged(i);
            checkAllSelectedState();
            EmoticonViewViewHolder emoticonViewViewHolder = (EmoticonViewViewHolder) this.mRootView.findViewHolderForAdapterPosition(i);
            if (emoticonViewViewHolder != null) {
                String string = emoticonViewViewHolder.itemView.getContext().getString(mimojiEmoticonInfo.getName() > 0 ? mimojiEmoticonInfo.getName() : R.string.lighting_pattern_null);
                if (mimojiEmoticonInfo.isSelected()) {
                    view = emoticonViewViewHolder.itemView;
                    StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    sb.append(", ");
                    sb.append(emoticonViewViewHolder.itemView.getContext().getString(R.string.accessibility_selected));
                    string = sb.toString();
                } else {
                    view = emoticonViewViewHolder.itemView;
                }
                view.setContentDescription(string);
                if (Util.isAccessible()) {
                    emoticonViewViewHolder.itemView.sendAccessibilityEvent(128);
                }
            }
        }
    }
}
