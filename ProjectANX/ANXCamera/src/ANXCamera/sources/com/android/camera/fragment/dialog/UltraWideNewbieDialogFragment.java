package com.android.camera.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;

public class UltraWideNewbieDialogFragment extends AiSceneNewbieDialogFragment {
    public static final String TAG = "UltraWideHint";

    public boolean onBackEvent(int i) {
        return super.onBackEvent(i);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_ultra_wide_hint, viewGroup, false);
        if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            ((TextView) inflate.findViewById(R.id.ultra_wide_use_hint_message_body)).setText(R.string.ultra_wide_mode_use_hint_text_sat);
        }
        initViewOnTouchListener(inflate);
        adjustViewHeight(inflate.findViewById(R.id.ultra_wide_use_hint_layout));
        return inflate;
    }
}
