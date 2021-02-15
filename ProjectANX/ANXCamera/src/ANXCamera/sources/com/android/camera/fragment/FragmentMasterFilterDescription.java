package com.android.camera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.AmbilightDescriptionItem;
import com.android.camera.data.data.config.ComponentRunningMasterFilter;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.fragment.manually.adapter.AmbilightDescriptionAdapter;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentMasterFilterDescription extends BaseDialogFragment {
    public static final String TAG = "FragmentMasterFilterDescription";
    private ArrayList mDataItems;
    ComponentRunningMasterFilter mFilter;
    private RecyclerView mRecyclerView;
    private TextView mTitle;

    private ArrayList getParameterData() {
        ArrayList arrayList = new ArrayList(Arrays.asList(new AmbilightDescriptionItem[]{new AmbilightDescriptionItem(R.string.video_effect_entry_color_retention, R.drawable.master_filter_info_color_retention, getResources().getString(R.string.video_effect_entry_color_retention_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_bbp, R.drawable.master_filter_info_bbp, getResources().getString(R.string.color_effect_entry_new_bbp_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_1, R.drawable.master_filter_info_wind_sing, getResources().getString(R.string.color_effect_entry_new_1_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_orange, R.drawable.master_filter_info_orange, getResources().getString(R.string.color_effect_entry_orange_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_2, R.drawable.master_filter_info_fantasy, getResources().getString(R.string.color_effect_entry_new_2_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_3, R.drawable.master_filter_info_lost, getResources().getString(R.string.color_effect_entry_new_3_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_4, R.drawable.master_filter_info_memory, getResources().getString(R.string.color_effect_entry_new_4_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_new_5, R.drawable.master_filter_info_central, getResources().getString(R.string.color_effect_entry_new_5_describe)), new AmbilightDescriptionItem(R.string.video_effect_entry_summer_day, R.drawable.master_filter_info_summer_day, getResources().getString(R.string.video_effect_entry_summer_day_describe)), new AmbilightDescriptionItem(R.string.color_effect_entry_blackgold, R.drawable.master_filter_info_blackgold, getResources().getString(R.string.color_effect_entry_blackgold_describe)), new AmbilightDescriptionItem(R.string.video_effect_entry_meet, R.drawable.master_filter_info_meet, getResources().getString(R.string.video_effect_entry_meet_describe)), new AmbilightDescriptionItem(R.string.video_effect_entry_northern_europe, R.drawable.master_filter_info_northern_europe, getResources().getString(R.string.video_effect_entry_northern_europe_describe)), new AmbilightDescriptionItem(R.string.video_effect_entry_rome, R.drawable.master_filter_info_rome, getResources().getString(R.string.video_effect_entry_rome_describe))}));
        if (!this.mFilter.IsSupportColorRentention()) {
            arrayList.remove(0);
        }
        return arrayList;
    }

    public /* synthetic */ void O00000o0(View view) {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), TAG);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        getDialog().setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) view.findViewById(R.id.ambilight_description_back);
        this.mTitle = (TextView) view.findViewById(R.id.ambilight_description_title);
        if (Util.isLayoutRTL(getContext())) {
            imageView.setRotation(180.0f);
        }
        this.mTitle.setText(R.string.masterfilter_description_title);
        imageView.setOnClickListener(new C0292O0000oo0(this));
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.ambilight_description_list);
        this.mRecyclerView.setFocusable(false);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), "parameter_recycler_view"));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.setAdapter(new AmbilightDescriptionAdapter(getParameterData()));
        LayoutParams layoutParams = (LayoutParams) this.mRecyclerView.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (!Util.isFullScreenNavBarHidden(getContext())) {
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.mimoji_edit_config_bottom);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        CameraSettings.getShaderEffect();
        this.mRecyclerView.scrollToPosition(Math.max(0, this.mFilter.findIndexOfValue(this.mFilter.getComponentValue(DataRepository.dataItemGlobal().getCurrentMode())) - 1));
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_ambilight_description, viewGroup, false);
        this.mFilter = DataRepository.dataItemRunning().getComponentRunningMasterFilter();
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        dismiss();
    }
}
