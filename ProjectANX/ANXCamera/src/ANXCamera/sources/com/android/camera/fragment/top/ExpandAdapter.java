package com.android.camera.fragment.top;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.ui.ScrollTextview;
import java.util.ArrayList;
import java.util.List;

public class ExpandAdapter extends Adapter implements OnClickListener {
    private ComponentData mComponentData;
    private int mCurrentMode = ((DataItemGlobal) DataRepository.provider().dataGlobal()).getCurrentMode();
    private String mCurrentValue = this.mComponentData.getComponentValue(this.mCurrentMode);
    private List mDatas;
    private ExpandListener mExpandListener;
    private int mMaxWidthViewItem;

    public interface ExpandListener {
        ImageView getTopImage(int i);

        void onExpandValueChange(ComponentData componentData, String str, String str2);
    }

    public ExpandAdapter(ComponentData componentData, ExpandListener expandListener) {
        this.mComponentData = componentData;
        this.mDatas = new ArrayList(componentData.getItems());
        this.mExpandListener = expandListener;
    }

    public int getItemCount() {
        List list = this.mDatas;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public int getItemViewType(int i) {
        if (this.mDatas == null) {
            return 0;
        }
        return this.mComponentData.getDisplayTitleString();
    }

    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        ComponentDataItem componentDataItem = (ComponentDataItem) this.mDatas.get(i);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.text);
        String str = componentDataItem.mValue;
        textView.setOnClickListener(this);
        textView.setTag(str);
        String string = commonRecyclerViewHolder.itemView.getResources().getString(componentDataItem.mDisplayNameRes);
        int measureText = (int) textView.getPaint().measureText(string);
        LayoutParams layoutParams = (LayoutParams) commonRecyclerViewHolder.itemView.getLayoutParams();
        int dimensionPixelSize = (commonRecyclerViewHolder.itemView.getResources().getDimensionPixelSize(R.dimen.top_expanded_selected_padding_horizontal) * 2) + measureText;
        int i2 = this.mMaxWidthViewItem;
        if (dimensionPixelSize > i2) {
            dimensionPixelSize = i2;
        }
        layoutParams.width = dimensionPixelSize;
        commonRecyclerViewHolder.itemView.setLayoutParams(layoutParams);
        textView.setText(string);
        if (this.mCurrentValue.equals(componentDataItem.mValue)) {
            textView.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            textView.setBackgroundResource(R.drawable.bg_top_expanded_selected);
            StringBuilder sb = new StringBuilder();
            sb.append(textView.getText());
            sb.append(", ");
            sb.append(commonRecyclerViewHolder.itemView.getResources().getString(R.string.accessibility_selected));
            textView.setContentDescription(sb.toString());
            if (Util.isAccessible()) {
                textView.postDelayed(new O000000o(textView), 100);
                return;
            }
            return;
        }
        textView.setShadowLayer(4.0f, 0.0f, 0.0f, -1073741824);
        textView.setBackgroundResource(R.drawable.bg_top_expanded_normal);
        textView.setContentDescription(textView.getText());
    }

    public void onClick(View view) {
        String str = (String) view.getTag();
        if (str != null && !str.equals(this.mCurrentValue)) {
            this.mComponentData.setComponentValue(this.mCurrentMode, str);
            ExpandListener expandListener = this.mExpandListener;
            if (expandListener != null) {
                expandListener.onExpandValueChange(this.mComponentData, this.mCurrentValue, str);
                this.mExpandListener = null;
            }
            this.mCurrentValue = str;
            notifyDataSetChanged();
        }
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_setting_expanded_text_item, viewGroup, false);
        ((ScrollTextview) inflate.findViewById(R.id.text)).setMaxWidth(this.mMaxWidthViewItem);
        LayoutParams layoutParams = (LayoutParams) inflate.getLayoutParams();
        int dimensionPixelSize = viewGroup.getContext().getResources().getDimensionPixelSize(R.dimen.expanded_text_item_margin);
        layoutParams.setMargins(dimensionPixelSize, 0, dimensionPixelSize, 0);
        inflate.setLayoutParams(layoutParams);
        return new CommonRecyclerViewHolder(inflate);
    }

    public void setMaxWidthItemView(int i) {
        this.mMaxWidthViewItem = i;
    }
}
