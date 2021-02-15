package com.android.camera.fragment.manually.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.customization.TintColor;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.ui.ColorImageView;

public class ExtraRecyclerViewAdapter extends Adapter implements OnClickListener {
    private final BaseFragment mBaseFragment;
    private final ComponentData mComponentData;
    private final int mCurrentMode;
    private int mDegree;
    private final int mItemWidth;
    private final ManuallyListener mManuallyListener;

    public ExtraRecyclerViewAdapter(ComponentData componentData, int i, ManuallyListener manuallyListener, int i2, int i3) {
        this(null, componentData, i, manuallyListener, i2, i3);
    }

    public ExtraRecyclerViewAdapter(BaseFragment baseFragment, ComponentData componentData, int i, ManuallyListener manuallyListener, int i2, int i3) {
        this.mBaseFragment = baseFragment;
        this.mComponentData = componentData;
        this.mCurrentMode = i;
        this.mManuallyListener = manuallyListener;
        this.mItemWidth = i2;
        this.mDegree = i3;
    }

    /* access modifiers changed from: protected */
    public boolean couldNewValueTakeEffect(String str) {
        return str != null && !str.equals(this.mComponentData.getComponentValue(this.mCurrentMode));
    }

    public int getItemCount() {
        return this.mComponentData.getItems().size();
    }

    public int getValuePosition() {
        String componentValue = this.mComponentData.getComponentValue(this.mCurrentMode);
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (componentValue.equals(((ComponentDataItem) this.mComponentData.getItems().get(i)).mValue)) {
                return i;
            }
        }
        return -1;
    }

    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        ComponentDataItem componentDataItem = (ComponentDataItem) this.mComponentData.getItems().get(i);
        String str = componentDataItem.mValue;
        commonRecyclerViewHolder.itemView.setOnClickListener(this);
        FolmeUtils.touchTint(commonRecyclerViewHolder.itemView);
        commonRecyclerViewHolder.itemView.setTag(str);
        commonRecyclerViewHolder.itemView.setRotation((float) this.mDegree);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.extra_item_text);
        ColorImageView colorImageView = (ColorImageView) commonRecyclerViewHolder.getView(R.id.extra_item_image);
        int tintColor = str.equals(this.mComponentData.getComponentValue(this.mCurrentMode)) ? TintColor.tintColor() : -1;
        colorImageView.setColor(tintColor);
        textView.setTextColor(tintColor);
        if (componentDataItem.mIconRes != -1) {
            colorImageView.setVisibility(0);
            colorImageView.setImageResource(componentDataItem.mIconRes);
            int i2 = componentDataItem.mIconShadowRes;
            if (i2 != -1) {
                colorImageView.setBackgroundResource(i2);
            } else {
                colorImageView.setBackground(null);
            }
        } else {
            colorImageView.setVisibility(8);
        }
        if (componentDataItem.mDisplayNameRes != -1) {
            textView.setVisibility(0);
            textView.setText(componentDataItem.mDisplayNameRes);
        } else {
            textView.setVisibility(8);
        }
        if (componentDataItem.mContentDescriptionRes > 0) {
            commonRecyclerViewHolder.itemView.setContentDescription(CameraAppImpl.getAndroidContext().getString(componentDataItem.mContentDescriptionRes));
        }
    }

    public void onClick(View view) {
        BaseFragment baseFragment = this.mBaseFragment;
        if (baseFragment == null || baseFragment.isEnableClick()) {
            CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
            if (cameraAction == null || !cameraAction.isDoingAction()) {
                String str = (String) view.getTag();
                if (couldNewValueTakeEffect(str)) {
                    String componentValue = this.mComponentData.getComponentValue(this.mCurrentMode);
                    this.mComponentData.setComponentValue(this.mCurrentMode, str);
                    notifyDataSetChanged();
                    ManuallyListener manuallyListener = this.mManuallyListener;
                    if (manuallyListener != null) {
                        manuallyListener.onManuallyDataChanged(this.mComponentData, componentValue, str, false, this.mCurrentMode);
                    }
                }
            }
        }
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_manually_extra_item, viewGroup, false);
        inflate.getLayoutParams().width = this.mItemWidth;
        return new CommonRecyclerViewHolder(inflate);
    }

    public void setDegree(int i) {
        this.mDegree = i;
    }
}
