package com.android.camera.dualvideo.user_guide;

import android.app.Dialog;
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
import com.android.camera.fragment.DefaultItemAnimator;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.dialog.BaseDialogFragment;
import com.android.camera.log.Log;
import java.util.Objects;

public class FragmentDualVideoUserGuide extends BaseDialogFragment {
    public static final String TAG = "FragmentDualVideoUserGuide";

    public /* synthetic */ void O00000Oo(View view) {
        Log.u(TAG, "onClick back");
        FragmentUtils.removeFragmentByTag(getFragmentManager(), TAG);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        ((Dialog) Objects.requireNonNull(getDialog())).setCanceledOnTouchOutside(false);
        ImageView imageView = (ImageView) view.findViewById(R.id.dual_video_back);
        if (Util.isLayoutRTL(getContext())) {
            imageView.setRotation(180.0f);
        }
        imageView.setOnClickListener(new O000000o(this));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_list);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(getContext(), "parameter_recycler_view"));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new DualVideoAdapter());
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
        View inflate = layoutInflater.inflate(R.layout.dual_video_user_guide, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        dismiss();
    }
}
