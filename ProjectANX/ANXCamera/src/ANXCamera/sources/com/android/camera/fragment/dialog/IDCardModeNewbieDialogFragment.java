package com.android.camera.fragment.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class IDCardModeNewbieDialogFragment extends AiSceneNewbieDialogFragment {
    public static final String TAG = "IDCardModeHint";

    public /* synthetic */ void O00000o(View view) {
        onBackEvent(5);
    }

    public boolean onBackEvent(int i) {
        Log.u(TAG, "onBackEvent");
        return super.onBackEvent(i);
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_id_card_mode_hint, viewGroup, false);
        initViewOnTouchListener(inflate);
        adjustViewHeight(inflate.findViewById(R.id.id_card_mode_use_hint_layout));
        Rect displayRect = Util.getDisplayRect(0);
        LayoutParams layoutParams = (LayoutParams) inflate.findViewById(R.id.id_card_drawable_layout).getLayoutParams();
        layoutParams.topMargin = (int) (getResources().getDimension(R.dimen.id_card_drawable_margin_top) - ((float) displayRect.top));
        if (Util.isAccessible()) {
            inflate.findViewById(R.id.id_card_mode_use_hint_layout).setOnClickListener(new O0000Oo(this));
        }
        return inflate;
    }
}
