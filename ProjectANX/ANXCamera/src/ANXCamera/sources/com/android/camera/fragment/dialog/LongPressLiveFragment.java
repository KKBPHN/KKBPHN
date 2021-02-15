package com.android.camera.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.log.Log;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Other;
import miui.app.AlertDialog.Builder;

public class LongPressLiveFragment extends DialogFragment {
    public static final String TAG = "ThermalDialog";
    @StringRes
    private int mId = R.string.pref_camera_long_pressing_burst_shooting_entryvalue_record;

    private void selectLpFeature(@StringRes int i) {
        String str;
        DataRepository.dataItemGlobal().editor().putString(CameraSettings.KEY_LONG_PRESS_SHUTTER_FEATURE, getString(i)).apply();
        String str2 = "ThermalDialog";
        if (this.mId == R.string.pref_camera_long_pressing_burst_shooting_entryvalue_record) {
            Log.u(str2, "selectLpFeature key_lp_dialog_record");
            str = Other.KEY_LONG_PRESS_DIALOG_RECORD;
        } else {
            Log.u(str2, "selectLpFeature key_lp_dialog_burst");
            str = Other.KEY_LONG_PRESS_DIALOG_BURST;
        }
        CameraStatUtils.trackLongPressDialogSelect(str);
        DataRepository.dataItemGlobal().editor().putBoolean(DataItemGlobal.DATA_COMMON_LPL_SELECTOR_USE_HINT_SHOWN, false).apply();
    }

    public /* synthetic */ void O000000o(View view, View view2) {
        view.findViewById(R.id.lpl_icon_record).setSelected(false);
        view.findViewById(R.id.lpl_icon_burst).setSelected(true);
        this.mId = R.string.pref_camera_long_pressing_burst_shooting_entryvalue_burst;
    }

    public /* synthetic */ void O00000Oo(View view, View view2) {
        view.findViewById(R.id.lpl_icon_record).setSelected(true);
        view.findViewById(R.id.lpl_icon_burst).setSelected(false);
        this.mId = R.string.pref_camera_long_pressing_burst_shooting_entryvalue_record;
    }

    public /* synthetic */ void O00000oo(DialogInterface dialogInterface, int i) {
        selectLpFeature(this.mId);
        dismiss();
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lpl_dialog, null, false);
        inflate.findViewById(R.id.lpl_icon_burst).setOnClickListener(new O0000o00(this, inflate));
        FolmeUtils.touchScaleTint(inflate.findViewById(R.id.lpl_icon_burst));
        inflate.findViewById(R.id.lpl_icon_record).setOnClickListener(new C0301O0000Ooo(this, inflate));
        FolmeUtils.touchScaleTint(inflate.findViewById(R.id.lpl_icon_record));
        inflate.findViewById(R.id.lpl_icon_record).setSelected(true);
        inflate.findViewById(R.id.lpl_icon_burst).setSelected(false);
        inflate.setPadding(0, 0, 0, Display.getNavigationBarHeight(getActivity()));
        return new Builder(getActivity()).setTitle((int) R.string.lpl_dialog_title).setView(inflate).setPositiveButton((int) R.string.dialog_ok, (OnClickListener) new C0300O0000OoO(this)).create();
    }
}
