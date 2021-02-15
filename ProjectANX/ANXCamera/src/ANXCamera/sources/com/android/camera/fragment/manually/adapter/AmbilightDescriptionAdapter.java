package com.android.camera.fragment.manually.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.android.camera.CameraAppImpl;
import com.android.camera.R;
import com.android.camera.data.data.AmbilightDescriptionItem;
import com.android.camera.fragment.CommonRecyclerViewHolder;
import java.util.ArrayList;

public class AmbilightDescriptionAdapter extends Adapter {
    private ArrayList mParameterDescriptionList;

    public AmbilightDescriptionAdapter(ArrayList arrayList) {
        this.mParameterDescriptionList = arrayList;
    }

    public int getItemCount() {
        return this.mParameterDescriptionList.size();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0089, code lost:
        if (r8 == (getItemCount() - 1)) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0092, code lost:
        if (r8 == (getItemCount() - 1)) goto L_0x0096;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindViewHolder(CommonRecyclerViewHolder commonRecyclerViewHolder, int i) {
        AmbilightDescriptionItem ambilightDescriptionItem = (AmbilightDescriptionItem) this.mParameterDescriptionList.get(i);
        TextView textView = (TextView) commonRecyclerViewHolder.getView(R.id.ambilight_description_title);
        if (i == 0) {
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            layoutParams.topMargin = CameraAppImpl.getAndroidContext().getResources().getDimensionPixelSize(R.dimen.ambilight_description_title_margin_top);
            textView.setLayoutParams(layoutParams);
        }
        textView.setText(ambilightDescriptionItem.mDisplayNameRes);
        LinearLayout linearLayout = (LinearLayout) commonRecyclerViewHolder.getView(R.id.layout_ambilight_intro_part1);
        ((ImageView) linearLayout.findViewById(R.id.ambilight_description_img)).setImageResource(ambilightDescriptionItem.mImageRes);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.ambilight_description_content);
        textView2.setText(ambilightDescriptionItem.mDescriptionPart1);
        if (!TextUtils.isEmpty(ambilightDescriptionItem.mDescriptionPart2)) {
            LinearLayout linearLayout2 = (LinearLayout) commonRecyclerViewHolder.getView(R.id.layout_ambilight_intro_part2);
            textView2 = (TextView) linearLayout2.findViewById(R.id.ambilight_description_content);
            textView2.setText(ambilightDescriptionItem.mDescriptionPart2);
            ((ImageView) linearLayout2.findViewById(R.id.ambilight_description_img)).setImageResource(ambilightDescriptionItem.mImageRes2);
            linearLayout2.setVisibility(0);
        }
        textView2 = null;
        if (textView2 != null) {
            LayoutParams layoutParams2 = (LayoutParams) textView2.getLayoutParams();
            layoutParams2.bottomMargin += CameraAppImpl.getAndroidContext().getResources().getDimensionPixelSize(R.dimen.ambilight_description_title_margin_bottom);
            textView2.setLayoutParams(layoutParams2);
        }
    }

    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_ambilight_description_adapter, viewGroup, false));
    }
}
