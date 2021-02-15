package com.android.camera.fragment.sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.android.camera.R;
import java.util.List;

public abstract class BaseSelectAdapter extends Adapter implements OnClickListener {
    protected Context mContext;
    private ItemSelectChangeListener mItemSelectChangeListener;
    protected int mLastSelectedItemPosition = -1;
    protected List mList;
    protected int mSelectedItemPosition = 0;

    public abstract class BaseSelectHolder extends ViewHolder {
        protected View mIVSelected;

        public BaseSelectHolder(View view) {
            super(view);
            view.setTag(R.id.item_root, this);
            this.mIVSelected = view.findViewById(R.id.iv_selected);
        }

        public abstract void bindView(int i);
    }

    public interface ItemSelectChangeListener {
        boolean onItemSelect(BaseSelectHolder baseSelectHolder, int i, boolean z);
    }

    public BaseSelectAdapter(Context context) {
        this.mContext = context;
    }

    private void setSelected(BaseSelectHolder baseSelectHolder, boolean z) {
        baseSelectHolder.itemView.setSelected(z);
        baseSelectHolder.mIVSelected.setVisibility(z ? 0 : 4);
    }

    public abstract BaseSelectHolder getHolder(View view);

    public int getItemCount() {
        List list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public Object getItemData(int i) {
        return this.mList.get(i);
    }

    public abstract int getLayoutId();

    public List getList() {
        return this.mList;
    }

    public int getSelectedItemPosition() {
        return this.mSelectedItemPosition;
    }

    public void initSelectItem(int i) {
        this.mSelectedItemPosition = i;
    }

    public void onBindViewHolder(BaseSelectHolder baseSelectHolder, int i) {
        baseSelectHolder.itemView.setOnClickListener(this);
        setSelected(baseSelectHolder, i == this.mSelectedItemPosition);
        baseSelectHolder.bindView(i);
    }

    public void onBindViewHolder(BaseSelectHolder baseSelectHolder, int i, List list) {
        if (list == null || list.size() <= 0) {
            super.onBindViewHolder(baseSelectHolder, i, list);
        } else {
            setSelected(baseSelectHolder, i == this.mSelectedItemPosition);
        }
    }

    public void onClick(View view) {
        BaseSelectHolder baseSelectHolder = (BaseSelectHolder) view.getTag(R.id.item_root);
        int adapterPosition = baseSelectHolder.getAdapterPosition();
        if (this.mSelectedItemPosition != adapterPosition) {
            ItemSelectChangeListener itemSelectChangeListener = this.mItemSelectChangeListener;
            if (itemSelectChangeListener != null && itemSelectChangeListener.onItemSelect(baseSelectHolder, adapterPosition, true)) {
                setSelectedItemPosition(adapterPosition);
                return;
            }
            return;
        }
        ItemSelectChangeListener itemSelectChangeListener2 = this.mItemSelectChangeListener;
        if (itemSelectChangeListener2 != null) {
            itemSelectChangeListener2.onItemSelect(baseSelectHolder, adapterPosition, false);
        }
    }

    public BaseSelectHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return getHolder(LayoutInflater.from(this.mContext).inflate(getLayoutId(), viewGroup, false));
    }

    public void setItemSelectChangeListener(ItemSelectChangeListener itemSelectChangeListener) {
        this.mItemSelectChangeListener = itemSelectChangeListener;
    }

    public void setList(List list) {
        this.mList = list;
    }

    public void setSelectedItemPosition(int i) {
        this.mLastSelectedItemPosition = this.mSelectedItemPosition;
        this.mSelectedItemPosition = i;
        int i2 = this.mLastSelectedItemPosition;
        Boolean valueOf = Boolean.valueOf(true);
        notifyItemChanged(i2, valueOf);
        notifyItemChanged(this.mSelectedItemPosition, valueOf);
    }
}
