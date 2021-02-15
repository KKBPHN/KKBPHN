package com.android.camera.fragment.aiwatermark.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.R;
import com.android.camera.aiwatermark.data.WatermarkItem;
import com.android.camera.fragment.aiwatermark.holder.LocationItemHolder;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.List;

public class WatermarkLocationAdapter extends Adapter implements OnClickListener {
    private static final String TAG = "WatermarkLocationAdapter";
    private Context mContext = null;
    protected List mItems = new ArrayList();
    private int mSelectedIndex = -1;
    private WatermarkItem mWatermarkItem = null;

    public WatermarkLocationAdapter(Context context, WatermarkItem watermarkItem) {
        this.mContext = context;
        this.mWatermarkItem = watermarkItem;
        this.mItems = this.mWatermarkItem.getLocationList();
    }

    public int getItemCount() {
        List list = this.mItems;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public String getSelectLocation() {
        try {
            return (String) this.mItems.get(this.mSelectedIndex);
        } catch (Exception unused) {
            Log.d(TAG, "error when getSelectLocation");
            return null;
        }
    }

    public void onBindViewHolder(@NonNull LocationItemHolder locationItemHolder, int i) {
        int i2;
        TextView textView;
        locationItemHolder.bindHolder(i, (String) this.mItems.get(i));
        locationItemHolder.itemView.setTag(Integer.valueOf(i));
        locationItemHolder.itemView.setOnClickListener(this);
        Context context = locationItemHolder.itemView.getContext();
        if (this.mSelectedIndex == i) {
            locationItemHolder.itemView.setBackgroundColor(context.getColor(R.color.watermark_location_selected_background));
            locationItemHolder.getSelectedIndicator().setVisibility(0);
            textView = locationItemHolder.getTextView();
            i2 = R.color.watermark_location_selected;
        } else {
            locationItemHolder.itemView.setBackgroundColor(context.getColor(R.color.watermark_location_unselected_background));
            locationItemHolder.getSelectedIndicator().setVisibility(4);
            textView = locationItemHolder.getTextView();
            i2 = R.color.watermark_location_unselected;
        }
        textView.setTextColor(context.getColor(i2));
    }

    public void onClick(View view) {
        try {
            this.mSelectedIndex = Integer.parseInt(view.getTag().toString());
            notifyDataSetChanged();
        } catch (NumberFormatException unused) {
            Log.e(TAG, "Object can not cast to Integer");
        }
    }

    @NonNull
    public LocationItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreateViewHolder i = ");
        sb.append(i);
        Log.d(str, sb.toString());
        return new LocationItemHolder(LayoutInflater.from(this.mContext).inflate(R.layout.watermark_location_rv_item, viewGroup, false));
    }

    public void setDefaultSelect(String str) {
        int i = 0;
        while (true) {
            if (i >= this.mItems.size()) {
                break;
            } else if (TextUtils.equals(str, (CharSequence) this.mItems.get(i))) {
                this.mSelectedIndex = i;
                break;
            } else {
                i++;
            }
        }
        notifyDataSetChanged();
    }

    public void updateSelectItem(LocationItemHolder locationItemHolder, int i) {
        View view = locationItemHolder.itemView;
        view.setBackgroundColor(view.getContext().getColor(R.color.watermark_location_selected));
        locationItemHolder.getSelectedIndicator().setVisibility(0);
    }
}
