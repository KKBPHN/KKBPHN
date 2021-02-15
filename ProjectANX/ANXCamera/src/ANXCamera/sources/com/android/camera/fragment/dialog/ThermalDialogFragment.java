package com.android.camera.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.android.camera.R;

public class ThermalDialogFragment extends BaseDialogFragment {
    public static final String TAG = "ThermalDialog";

    public /* synthetic */ boolean O000000o(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (getActivity() != null) {
            getActivity().finish();
        }
        return false;
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().setOnKeyListener(new O0000o0(this));
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_thermal_dialog, viewGroup, false);
        initViewOnTouchListener(inflate);
        ((TextView) inflate.findViewById(R.id.thermal_dialog_content)).setText(getResources().getString(R.string.ccc_temperature_warning_msg, new Object[]{Integer.valueOf(5)}));
        return inflate;
    }
}
