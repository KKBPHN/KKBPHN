package com.android.camera.fragment.manually;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.fragment.manually.adapter.ParameterDescriptionAdapter;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentParameterDescription extends BaseDialogFragment {
    public static final String TAG = "FragmentParameterDescription";
    private int mCurrentMode;
    private List mManuallyDataItems;
    private List mProVideoDataItems;
    /* access modifiers changed from: private */
    public RecyclerView mRecyclerView;
    /* access modifiers changed from: private */
    public View mSplitView;

    private List getParameterData() {
        int i;
        StringBuilder sb;
        Context context;
        if (this.mCurrentMode == 167) {
            List list = this.mManuallyDataItems;
            if (list != null) {
                return list;
            }
        }
        if (this.mCurrentMode == 180) {
            List list2 = this.mProVideoDataItems;
            if (list2 != null) {
                return list2;
            }
        }
        CameraCapabilities capabilitiesByBogusCameraId = Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(DataRepository.dataItemGlobal().getCurrentCameraId(), this.mCurrentMode);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ComponentDataItem((int) R.drawable.ic_parameter_metering, -1, (int) R.string.parameter_metering_title, getContext().getString(R.string.parameter_metering_description)));
        int i2 = this.mCurrentMode;
        if (i2 == 167) {
            if (C0124O00000oO.Oo00oO0()) {
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_parameter_peak_focus, -1, (int) R.string.parameter_peak_focus_title, getContext().getString(R.string.parameter_peak_focus_description)));
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_parameter_exposure_feedback, -1, (int) R.string.parameter_exposure_feecback_title, getContext().getString(R.string.parameter_exposure_feecback_description)));
            }
            if (C0122O00000o.instance().O0oo0O() && capabilitiesByBogusCameraId.isSupportRaw()) {
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_parameter_raw, -1, (int) R.string.parameter_peak_raw_title, getContext().getString(R.string.parameter_peak_raw_description)));
            }
        } else if (i2 == 180) {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(new ComponentDataItem((int) R.drawable.ic_parameter_peak_focus, -1, (int) R.string.parameter_peak_focus_title, getContext().getString(R.string.parameter_peak_focus_description)));
            arrayList2.add(new ComponentDataItem((int) R.drawable.ic_parameter_exposure_feedback, -1, (int) R.string.parameter_exposure_feecback_title, getContext().getString(R.string.parameter_exposure_feecback_description)));
            arrayList2.add(new ComponentDataItem((int) R.drawable.ic_parameter_histogram, -1, (int) R.string.parameter_histogram_title, getContext().getString(R.string.parameter_histogram_description)));
            arrayList2.add(new ComponentDataItem((int) R.drawable.ic_parameter_slider, -1, (int) R.string.parameter_zoom_slider_title, getContext().getString(R.string.parameter_zoom_slider_description)));
            if (capabilitiesByBogusCameraId.isSupportedVideoLogFormat()) {
                arrayList2.add(new ComponentDataItem((int) R.drawable.ic_parameter_log, -1, (int) R.string.parameter_log_format_title, getContext().getString(R.string.parameter_log_format_description)));
            }
            arrayList.addAll(arrayList2);
        }
        String string = getContext().getString(R.string.parameter_lens_description);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(getContext().getString(R.string.parameter_lens_w));
        String sb3 = sb2.toString();
        if (C0122O00000o.instance().isSupportUltraWide()) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(sb3);
            sb4.append(getContext().getString(R.string.parameter_lens_uw));
            sb3 = sb4.toString();
        }
        if (C0122O00000o.instance().OOoOO0o() && C0122O00000o.instance().OOoOO()) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(sb3);
            sb5.append(getContext().getString(R.string.parameter_lens_marco));
            sb3 = sb5.toString();
        }
        if (CameraSettings.isSupportedOpticalZoom()) {
            if (C0122O00000o.instance().OOOOoOo()) {
                if (!C0122O00000o.instance().OOo000() || this.mCurrentMode != 180) {
                    sb = new StringBuilder();
                    sb.append(sb3);
                    context = getContext();
                    i = R.string.parameter_lens_tele_2x;
                }
            } else if (Camera2DataContainer.getInstance().getAuxCameraId() >= 0) {
                sb = new StringBuilder();
                sb.append(sb3);
                context = getContext();
                i = R.string.parameter_lens_t;
            }
            sb.append(context.getString(i));
            sb3 = sb.toString();
        }
        if (C0122O00000o.instance().OOOOoOo() && (this.mCurrentMode != 180 || C0122O00000o.instance().OOo000())) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append(sb3);
            sb6.append(getContext().getString(R.string.parameter_lens_tele_5x));
            sb3 = sb6.toString();
        }
        arrayList.addAll(Arrays.asList(new ComponentDataItem[]{new ComponentDataItem((int) R.drawable.ic_parameter_ef_split, -1, (int) R.string.parameter_ef_split_title, getContext().getString(R.string.parameter_ef_split_description)), new ComponentDataItem((int) R.drawable.ic_parameter_wb, -1, (int) R.string.parameter_wb_title, getContext().getString(R.string.parameter_wb_description)), new ComponentDataItem((int) R.drawable.ic_parameter_focus, -1, (int) R.string.parameter_focus_title, getContext().getString(R.string.parameter_focus_description)), new ComponentDataItem((int) R.drawable.ic_parameter_et, -1, (int) R.string.parameter_et_title, getContext().getString(R.string.parameter_et_description)), new ComponentDataItem((int) R.drawable.ic_parameter_ev, -1, (int) R.string.parameter_exposure_title, getContext().getString(R.string.parameter_exposure_description)), new ComponentDataItem((int) R.drawable.ic_parameter_iso, -1, (int) R.string.parameter_iso_title, getContext().getString(R.string.parameter_iso_description))}));
        if (CameraSettings.isSupportedOpticalZoom() || C0122O00000o.instance().isSupportUltraWide()) {
            arrayList.add(new ComponentDataItem((int) R.drawable.ic_parameter_lens, -1, (int) R.string.parameter_lens_title, sb3));
        }
        int i3 = this.mCurrentMode;
        if (i3 == 167) {
            this.mManuallyDataItems = arrayList;
        } else if (i3 == 180) {
            this.mProVideoDataItems = arrayList;
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initView(View view) {
        RecyclerView recyclerView;
        int i;
        getDialog().setCanceledOnTouchOutside(false);
        this.mCurrentMode = DataRepository.dataItemGlobal().getCurrentMode();
        ImageView imageView = (ImageView) view.findViewById(R.id.parameter_description_back);
        if (Util.isLayoutRTL(getContext())) {
            imageView.setRotation(180.0f);
        }
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.u(FragmentParameterDescription.TAG, "onClick back");
                FragmentUtils.removeFragmentByTag(FragmentParameterDescription.this.getFragmentManager(), FragmentParameterDescription.TAG);
            }
        });
        this.mSplitView = view.findViewById(R.id.parameter_description_split_view);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.manually_parameter_description_list);
        this.mRecyclerView.setFocusable(false);
        LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "parameter_recycler_view");
        linearLayoutManagerWrapper.setOrientation(1);
        this.mRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.setAdapter(new ParameterDescriptionAdapter(getContext(), getParameterData()));
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(@NonNull RecyclerView recyclerView, int i, int i2) {
                View access$100;
                int i3;
                if (!FragmentParameterDescription.this.mRecyclerView.canScrollVertically(-1)) {
                    access$100 = FragmentParameterDescription.this.mSplitView;
                    i3 = 4;
                } else if (i2 > 0) {
                    access$100 = FragmentParameterDescription.this.mSplitView;
                    i3 = 0;
                } else {
                    return;
                }
                access$100.setVisibility(i3);
            }
        });
        TextView textView = (TextView) view.findViewById(R.id.parameter_description_title);
        textView.setTypeface(Typeface.create("mipro-medium", 0));
        ((TextView) view.findViewById(R.id.parameter_description_sub_title)).setTypeface(Typeface.create("mipro-regular", 0));
        int i2 = this.mCurrentMode;
        if (i2 != 167) {
            if (i2 == 180) {
                i = R.string.parameter_description_pro_video_title;
            }
            recyclerView = this.mRecyclerView;
            if (recyclerView == null) {
                LayoutParams layoutParams = (LayoutParams) recyclerView.getLayoutParams();
                if (layoutParams == null) {
                    return;
                }
                if (!Util.isFullScreenNavBarHidden(getContext())) {
                    layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.mimoji_edit_config_bottom);
                    return;
                } else {
                    layoutParams.bottomMargin = 0;
                    return;
                }
            } else {
                return;
            }
        } else {
            i = R.string.parameter_description_pro_capture_title;
        }
        textView.setText(i);
        recyclerView = this.mRecyclerView;
        if (recyclerView == null) {
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_manually_description, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        dismiss();
    }
}
