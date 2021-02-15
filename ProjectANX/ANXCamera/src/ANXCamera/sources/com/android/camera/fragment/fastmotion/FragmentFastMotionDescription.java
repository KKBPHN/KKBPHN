package com.android.camera.fragment.fastmotion;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentFastMotionDescription extends BaseDialogFragment {
    public static final String TAG = "FragmentFastMotionDescription";
    private ArrayList mDataItems;
    FastMotionDescriptionAdapter mFastMotionDescriptionAdapter;
    LinearLayoutManagerWrapper mLayoutManager;
    RecyclerView mRecyclerView;

    class FastmotionDescriptionItemDecoration extends ItemDecoration {
        private FastmotionDescriptionItemDecoration() {
        }

        public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull State state) {
            super.getItemOffsets(rect, view, recyclerView, state);
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            if (childAdapterPosition == recyclerView.getAdapter().getItemCount() - 2) {
                rect.set(0, 0, 0, FragmentFastMotionDescription.this.getResources().getDimensionPixelOffset(R.dimen.fastmotion_description_item_text_second_margin_top));
            }
            if (childAdapterPosition == recyclerView.getAdapter().getItemCount() - 1) {
                rect.set(0, 0, 0, FragmentFastMotionDescription.this.getResources().getDimensionPixelOffset(R.dimen.fastmotion_description_item_text_second_margin_bottom));
            }
        }
    }

    private ArrayList getParameterData() {
        ArrayList arrayList = this.mDataItems;
        if (arrayList != null) {
            return arrayList;
        }
        FastMotionDescriptionItem fastMotionDescriptionItem = new FastMotionDescriptionItem(false, 0, "", 0, getString(R.string.pref_camera_fastmotion_speed), getString(R.string.pref_camera_fastmotion_speed_description));
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.pref_camera_fastmotion_speed_30x));
        sb.append(" | 4X-30X");
        FastMotionDescriptionItem fastMotionDescriptionItem2 = new FastMotionDescriptionItem(true, R.raw.fastmotion_30x, sb.toString(), R.drawable.fastmotion_cover_30x, "", "");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getString(R.string.pref_camera_fastmotion_speed_90x));
        sb2.append(" | 60X-90X");
        FastMotionDescriptionItem fastMotionDescriptionItem3 = new FastMotionDescriptionItem(true, R.raw.fastmotion_90x, sb2.toString(), R.drawable.fastmotion_cover_90x, "", "");
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getString(R.string.pref_camera_fastmotion_speed_150x));
        sb3.append(" | 120X-150X");
        FastMotionDescriptionItem fastMotionDescriptionItem4 = new FastMotionDescriptionItem(true, R.raw.fastmotion_150x, sb3.toString(), R.drawable.fastmotion_cover_150x, "", "");
        StringBuilder sb4 = new StringBuilder();
        sb4.append(getString(R.string.pref_camera_fastmotion_speed_750x));
        sb4.append(" | 300X-600X");
        FastMotionDescriptionItem fastMotionDescriptionItem5 = new FastMotionDescriptionItem(true, R.raw.fastmotion_750x, sb4.toString(), R.drawable.fastmotion_cover_750x, "", "");
        StringBuilder sb5 = new StringBuilder();
        sb5.append(getString(R.string.pref_camera_fastmotion_speed_1800x));
        sb5.append(" | 900X-1800X");
        FastMotionDescriptionItem fastMotionDescriptionItem6 = new FastMotionDescriptionItem(true, R.raw.fastmotion_1800x, sb5.toString(), R.drawable.fastmotion_cover_1800x, "", "");
        FastMotionDescriptionItem fastMotionDescriptionItem7 = new FastMotionDescriptionItem(false, 0, "", 0, getString(R.string.pref_camera_fastmotion_duration), getString(R.string.pref_camera_fastmotion_duration_description));
        ArrayList arrayList2 = new ArrayList(Arrays.asList(new FastMotionDescriptionItem[]{fastMotionDescriptionItem, fastMotionDescriptionItem2, fastMotionDescriptionItem3, fastMotionDescriptionItem4, fastMotionDescriptionItem5, fastMotionDescriptionItem6, fastMotionDescriptionItem7}));
        this.mDataItems = arrayList2;
        return arrayList2;
    }

    public /* synthetic */ void O00000oO(View view) {
        Log.u(TAG, "onClick back");
        FragmentUtils.removeFragmentByTag(getFragmentManager(), TAG);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        getDialog().setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) view.findViewById(R.id.fastmotion_description_back);
        if (Util.isLayoutRTL(getContext())) {
            imageView.setRotation(180.0f);
        }
        imageView.setOnClickListener(new O00000Oo(this));
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.fastmotion_description_list);
        this.mRecyclerView.setFocusable(false);
        this.mLayoutManager = new LinearLayoutManagerWrapper(getContext(), "parameter_recycler_view");
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addItemDecoration(new FastmotionDescriptionItemDecoration());
        this.mFastMotionDescriptionAdapter = new FastMotionDescriptionAdapter(this.mRecyclerView, getContext(), getParameterData());
        this.mRecyclerView.setAdapter(this.mFastMotionDescriptionAdapter);
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

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_fastmotion_description, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        dismiss();
    }
}
