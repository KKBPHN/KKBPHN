package com.android.camera.fragment.manually;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.DurationConstant;
import com.android.camera.data.data.AmbilightDescriptionItem;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.fragment.manually.adapter.AmbilightDescriptionAdapter;
import com.android.camera.log.Log;
import com.android.camera.module.AmbilightModule;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentAmbilightDescription extends BaseDialogFragment {
    public static final String TAG = "FragmentAmbilightDescription";
    private ArrayList mDataItems;

    private ArrayList getParameterData() {
        ArrayList arrayList = this.mDataItems;
        if (arrayList != null) {
            return arrayList;
        }
        AmbilightDescriptionItem[] ambilightDescriptionItemArr = new AmbilightDescriptionItem[6];
        AmbilightDescriptionItem ambilightDescriptionItem = new AmbilightDescriptionItem(R.string.ambilight_scene_crowd_moving, R.drawable.ambilight_intro_crowd_moving_part1, getResources().getString(R.string.ambilight_intro_crowd_moving_part1), R.drawable.ambilight_intro_crowd_moving_part2, getResources().getString(R.string.ambilight_intro_crowd_moving_part2));
        ambilightDescriptionItemArr[0] = ambilightDescriptionItem;
        AmbilightDescriptionItem ambilightDescriptionItem2 = new AmbilightDescriptionItem(R.string.ambilight_scene_traffic_light, R.drawable.ambilight_intro_traffic_light, getResources().getString(R.string.ambilight_intro_traffic_light), -1, null);
        ambilightDescriptionItemArr[1] = ambilightDescriptionItem2;
        AmbilightDescriptionItem ambilightDescriptionItem3 = new AmbilightDescriptionItem(R.string.ambilight_scene_silky_water, R.drawable.ambilight_intro_silky_water_part1, getResources().getString(R.string.ambilight_intro_silky_water_part1), R.drawable.ambilight_intro_silky_water_part2, getResources().getString(R.string.ambilight_intro_silky_water_part2));
        ambilightDescriptionItemArr[2] = ambilightDescriptionItem3;
        AmbilightDescriptionItem ambilightDescriptionItem4 = new AmbilightDescriptionItem(R.string.ambilight_scene_light_track, R.drawable.ambilight_intro_light_track, getResources().getString(R.string.ambilight_intro_light_track), -1, null);
        ambilightDescriptionItemArr[3] = ambilightDescriptionItem4;
        AmbilightDescriptionItem ambilightDescriptionItem5 = new AmbilightDescriptionItem(R.string.ambilight_scene_magic_star, R.drawable.ambilight_intro_magic_star, getResources().getString(AmbilightModule.mSupportAutoAe ? R.string.ambilight_intro_magic_star_ae : R.string.ambilight_intro_magic_star, new Object[]{Integer.valueOf(DurationConstant.DURATION_AMBILIGHT_MAGIC_STAR_CAPTURE / 1000)}), -1, null);
        ambilightDescriptionItemArr[4] = ambilightDescriptionItem5;
        AmbilightDescriptionItem ambilightDescriptionItem6 = new AmbilightDescriptionItem(R.string.ambilight_scene_star_track, R.drawable.ambilight_intro_star_track, getResources().getString(R.string.ambilight_intro_star_track_2, new Object[]{Integer.valueOf(30)}), -1, null);
        ambilightDescriptionItemArr[5] = ambilightDescriptionItem6;
        ArrayList arrayList2 = new ArrayList(Arrays.asList(ambilightDescriptionItemArr));
        this.mDataItems = arrayList2;
        return arrayList2;
    }

    public /* synthetic */ void O00000oo(View view) {
        Log.u(TAG, "onClick back");
        FragmentUtils.removeFragmentByTag(getFragmentManager(), TAG);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        getDialog().setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) view.findViewById(R.id.ambilight_description_back);
        if (Util.isLayoutRTL(getContext())) {
            imageView.setRotation(180.0f);
        }
        imageView.setOnClickListener(new O000000o(this));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ambilight_description_list);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), "parameter_recycler_view"));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AmbilightDescriptionAdapter(getParameterData()));
        LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (!Util.isFullScreenNavBarHidden(getContext())) {
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.mimoji_edit_config_bottom);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_ambilight_description, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        dismiss();
    }
}
